package com.ahl.server.entity;

public enum TeamTag {

    M_RED(1, 1, "M"),

    M_BLUE(2, 2, "M"),

    M_WHITE(3, 3, "M"),

    M_YELLOW(4, 4, "M"),

    M_GREEN(5, 5, "M"),

    M_VIOLET(6, 6, "M"),

    W_RED(7, 1, "W"),

    W_BLUE(8, 2, "W"),

    W_WHITE(9, 3, "W"),

    W_YELLOW(10, 4, "W"),

    W_GREEN(11, 5, "W"),

    W_VIOLET(12, 6, "W"),

    M_BLACK(13, -1, "M"),

    Other(14, -1, "nil");


    private int teamTagId;
    private int teamId;
    private String category;

    TeamTag(int teamTagId, int teamId, String category) {
        this.teamTagId = teamTagId;
        this.teamId = teamId;
        this.category = category;
    }

    public int getTeamTagId() {
        return teamTagId;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getCategory() {
        return category;
    }
}
