package com.ninyo.player.config.batch;

import com.ninyo.player.model.Player;
import com.ninyo.player.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private static final String SRC_TEST_RESOURCES = "src/test/resources";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EmptyFileHandler emptyFileHandler;

    @Value("${inputFile:/input/player.csv}")
    private String inputFile;

    @Value("${executor-corePoolSize:50}")
    private int corePoolSize;

    @Value("${executor-maxPoolSize:100}")
    private int maxPoolSize;

    @Bean
    public Job importPlayerJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("importPlayerJob", jobRepository)
                .start(importPlayerStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step importPlayerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importPlayerStep", jobRepository)
                .<Player, Player>chunk(100, transactionManager)
                .reader(reader())
                .processor(itemProcessor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .listener(emptyFileHandler)
                .build();
    }

    @Bean
    public FlatFileItemReader<Player> reader() {
        FlatFileItemReader<Player> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(new PathResource(new File(SRC_TEST_RESOURCES).getAbsolutePath() + inputFile));
        flatFileItemReader.setLineMapper(linMapper());
        return flatFileItemReader;
    }

    @Bean
    public ItemProcessor<Player, Player> itemProcessor() {
        return new PlayerItemProcessor();
    }

    @Bean
    public ItemWriter<Player> writer() {
        return playerRepository::saveAll;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        //leave queueCapacity as default, which is Integer.MAX_VALUE.
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public LineMapper<Player> linMapper() {
        DefaultLineMapper<Player> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        String[] names = {"playerID", "birthYear", "birthMonth", "birthDay", "birthCountry", "birthState", "birthCity", "deathYear", "deathMonth", "deathDay", "deathCountry", "deathState", "deathCity", "nameFirst", "nameLast", "nameGiven", "weight", "height", "bats", "throwz", "debutStr", "finalGameStr", "retroID", "bbrefID"};
        lineTokenizer.setNames(names);
        lineTokenizer.setStrict(false);
        defaultLineMapper.setLineTokenizer(lineTokenizer);

        // Parsing logic
        Map<Class<?>, PropertyEditor> editors = new HashMap<>();
        editors.put(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (!StringUtils.hasLength(text)) {
                    setValue(null);
                } else {
                    setValue(text);
                }
            }
        });
        editors.put(Integer.TYPE, new CustomNumberEditor(Integer.class, NumberFormat.getInstance(), true));

        BeanWrapperFieldSetMapper<Player> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Player.class);
        fieldSetMapper.setCustomEditors(editors);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

}