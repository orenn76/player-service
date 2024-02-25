package com.ninyo.player.services;

import com.ninyo.player.model.Player;
import com.ninyo.player.repositories.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    private static final String PLAYER_ID = "playerId";

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

    @Mock
    private PlayerRepository playerRepository;

    @Captor
    private ArgumentCaptor<Job> jobArgumentCaptor;

    @Captor
    private ArgumentCaptor<JobParameters> jobParametersArgumentCaptor;

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void testLoad() throws Exception {
        String mockedJobName = "mockedJob";
        when(job.getName()).thenReturn(mockedJobName);
        BatchStatus expectedResult = BatchStatus.COMPLETED;
        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        when(jobExecution.getStatus()).thenReturn(expectedResult);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);

        BatchStatus result = playerService.load();

        verify(jobLauncher).run(jobArgumentCaptor.capture(), jobParametersArgumentCaptor.capture());
        Assertions.assertEquals(mockedJobName, jobArgumentCaptor.getValue().getName());
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindById() {
        Player player = new Player();
        player.setPlayerID(PLAYER_ID);
        when(playerRepository.findByPlayerID(PLAYER_ID)).thenReturn(List.of(player));

        Player result = playerService.findById(PLAYER_ID);

        verify(playerRepository).findByPlayerID(PLAYER_ID);
        Assertions.assertEquals(player, result);
    }

    @Test
    public void testFindAll() {
        List<Player> players = createPlayers();
        when(playerRepository.findAll()).thenReturn(players);

        List<Player> result = playerService.findAll();

        verify(playerRepository).findAll();
        Assertions.assertEquals(players, result);
    }

    private List<Player> createPlayers() {
        Player player1 = new Player();
        player1.setPlayerID("playerId1");
        Player player2 = new Player();
        player1.setPlayerID("playerId2");
        return List.of(player1, player2);
    }

}
