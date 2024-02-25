package com.ninyo.player.config.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmptyFileHandler implements StepExecutionListener {

    @AfterStep
    public ExitStatus afterStep(StepExecution execution) {
        if (execution.getReadCount() > 0) {
            return execution.getExitStatus();
        } else {
            log.error("Empty CSV file!");
            return ExitStatus.FAILED;
        }
    }
}