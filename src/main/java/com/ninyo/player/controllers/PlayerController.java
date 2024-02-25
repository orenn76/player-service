package com.ninyo.player.controllers;

import com.ninyo.player.exceptions.PlayerException;
import com.ninyo.player.model.Player;
import com.ninyo.player.services.PlayerService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/players")
public class PlayerController {

    public static final String ID_PATTERN = "/{playerID}";

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/load")
    public BatchStatus load() throws PlayerException {
        return playerService.load();
    }

    @GetMapping(ID_PATTERN)
    @ResponseStatus(HttpStatus.OK)
    public Player findById(@PathVariable String playerID) {
        return playerService.findById(playerID);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Player> findAll() {
        return playerService.findAll();
    }

    @GetMapping("/listPageable")
    Page<Player> findAllPageable(Pageable pageable) {
        return playerService.findAll(pageable);

    }

}
