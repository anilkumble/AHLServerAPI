package com.ahl.server.entity;

public enum TeamTag {

    M_RED(1, 1),

    M_BLUE(2, 2),

    M_WHITE(3, 3),

    M_YELLOW(4, 4),

    M_GREEN(5, 5),

    M_VIOLET(6, 6),

    W_RED(7, 1),

    W_BLUE(8, 2),

    W_WHITE(9, 3),

    W_YELLOW(10, 4),

    W_GREEN(11, 5),

    W_VIOLET(12, 6),

    M_BLACK(13, -1),

    Other(14, -1);


    private int teamTagId;
    private int mapId;

    TeamTag(int teamTagId, int mapId) {
        this.teamTagId = teamTagId;
        this.mapId = mapId;
    }

    public int getTeamTagId() {
        return teamTagId;
    }

    public int getMapId() {
        return mapId;
    }
}
