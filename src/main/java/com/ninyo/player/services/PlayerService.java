package com.ninyo.player.services;

import com.ninyo.player.exceptions.PlayerException;
import com.ninyo.player.exceptions.PlayerNotFoundException;
import com.ninyo.player.model.Player;
import com.ninyo.player.repositories.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class PlayerService {

    private JobLauncher jobLauncher;

    private Job job;

    private PlayerRepository playerRepository;

    public PlayerService(JobLauncher jobLauncher, Job job, PlayerRepository playerRepository) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.playerRepository = playerRepository;
    }

    public BatchStatus load() throws PlayerException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("timestamp", Calendar.getInstance().getTime())
                .toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            BatchStatus batchStatus = jobExecution.getStatus();
            if (batchStatus == BatchStatus.FAILED) {
                throw new PlayerException("Job Execution failed");
            }
            return batchStatus;
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new PlayerException("Could not load the CSV file", e);
        }
    }

    public Player findById(String playerId) {
        Player player = playerRepository.findByPlayerID(playerId).stream().findFirst().orElse(null);
        if (player == null) {
            throw new PlayerNotFoundException("Player with id " + playerId + " could not be found");
        }
        return player;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Page<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

}
