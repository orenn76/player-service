package com.ninyo.player.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlayerControllerIntegrationTest {

    private static final String PLAYER_ID = "aaronto01";
    private static final String PLAYER_FAKE_ID = "fakeId";
    private static final String LOAD = "/players/load";
    private static final String FIND_ALL_URL = "/players";
    private static final String FIND_BY_ID = FIND_ALL_URL + "/" + PLAYER_ID;
    private static final String FIND_BY_FAKE_ID = FIND_ALL_URL + "/" + PLAYER_FAKE_ID;
    private static final String FIND_ALL_PAGEABLE_URL = "/players/listPageable?page=0&size=3";

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void testLoadAndFindById() throws Exception {
        this.mockMvc.perform(get(contextPath + LOAD).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        this.mockMvc.perform(get(contextPath + FIND_BY_ID).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.playerID").value(PLAYER_ID));
    }

    @Test
    void testLoadAndFindAll() throws Exception {
        this.mockMvc.perform(get(contextPath + LOAD).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        this.mockMvc.perform(get(contextPath + FIND_ALL_URL).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$", hasSize(19370)));
    }

    @Test
    void testLoadAndFindAllPageable() throws Exception {
        this.mockMvc.perform(get(contextPath + LOAD).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        this.mockMvc.perform(get(contextPath + FIND_ALL_PAGEABLE_URL).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.numberOfElements").value(3));
    }

    @Test
    void testLoadAndFindByNoneExistId() throws Exception {
        this.mockMvc.perform(get(contextPath + LOAD).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        this.mockMvc.perform(get(contextPath + FIND_BY_FAKE_ID).contextPath(contextPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
    }

}