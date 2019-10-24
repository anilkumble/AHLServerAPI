package com.ahl.server.enums;

public enum MatchStatus {

    UPCOMING (1),

    LIVE_MATCH (2),

    COMPLETED (3);

    private int id;

    MatchStatus(int id) {
        this.id = id;
    }
}
