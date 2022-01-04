package com.gabrielhd.common;

import lombok.Getter;

public class TopPlayer {

    @Getter
    private final String player;
    @Getter
    private final int credits;

    public TopPlayer(String player, int credits) {
        this.player = player;
        this.credits = credits;
    }
}
