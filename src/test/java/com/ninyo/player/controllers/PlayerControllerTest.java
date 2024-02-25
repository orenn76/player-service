package com.ninyo.player.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninyo.player.model.Player;
import com.ninyo.player.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest {

    private static final String PLAYER_ID = "playerId";
    private static final String LOAD = "/players/load";
    private static final String FIND_BY_ID = "/players/" + PLAYER_ID;
    private static final String FIND_ALL_URL = "/players";

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @Test
    void testLoad() throws Exception {
        this.mockMvc.perform(get(contextPath + LOAD).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void testFindById() throws Exception {
        Player player = new Player();
        player.setPlayerID(PLAYER_ID);
        when(playerService.findById(PLAYER_ID)).thenReturn(player);

        this.mockMvc.perform(get(contextPath + FIND_BY_ID).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonStr(player)));
    }

    @Test
    void testFindAll() throws Exception {
        List<Player> players = createPlayers();
        when(playerService.findAll()).thenReturn(players);

        this.mockMvc.perform(get(contextPath + FIND_ALL_URL).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonStr(players)));
    }

    private List<Player> createPlayers() {
        Player player1 = new Player();
        player1.setPlayerID("playerId1");
        Player player2 = new Player();
        player1.setPlayerID("playerId2");
        return List.of(player1, player2);
    }

    private String jsonStr(Object obj) throws JsonProcessingException {
        return objectMapper
                .writer()
                .withDefaultPrettyPrinter()
                .writeValueAsString(obj);
    }

}