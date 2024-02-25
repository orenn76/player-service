package com.ninyo.player.config.batch;

import com.ninyo.player.model.Player;
import com.ninyo.player.utils.DateUtils;
import org.springframework.batch.item.ItemProcessor;

public class PlayerItemProcessor implements ItemProcessor<Player, Player> {

    @Override
    public Player process(Player player) {
        player.setDebut(DateUtils.parseDate(player.getDebutStr()));
        player.setFinalGame(DateUtils.parseDate(player.getFinalGameStr()));
        return player;
    }

}