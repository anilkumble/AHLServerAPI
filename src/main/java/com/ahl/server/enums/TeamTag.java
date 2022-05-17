package com.ahl.server.enums;

import com.ahl.server.AHLConstants;

public enum TeamTag {

    M_RED(1, 1, AHLConstants.MEN),

    M_BLUE(2, 2, AHLConstants.MEN),

    M_WHITE(3, 3, AHLConstants.MEN),

    M_YELLOW(4, 4, AHLConstants.MEN),

    M_GREEN(5, 5, AHLConstants.MEN),

    M_VIOLET(6, 6, AHLConstants.MEN),

    W_RED(7, 1, AHLConstants.WOMEN),

    W_BLUE(8, 2, AHLConstants.WOMEN),

    W_WHITE(9, 3, AHLConstants.WOMEN),

    W_YELLOW(10, 4, AHLConstants.WOMEN),

    W_GREEN(11, 5, AHLConstants.WOMEN),

    W_VIOLET(12, 6, AHLConstants.WOMEN),

    M_BLACK(13, -1, AHLConstants.MEN),

    Other(14, -1, AHLConstants.OTHER),

    W_RANGERS(15, 7, AHLConstants.WOMEN),

    W_FLICKERS(16, 8, AHLConstants.WOMEN),

    W_SMASHING(17, 9, AHLConstants.WOMEN);


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
