package com.ahl.server.enums;

public enum Position {

    FORWARD (1),

    DEFENCE(2),

    MIDFIELDER(3),

    GOALKEEPER(4);

    private int id;

    Position(int id) {
        this.id = id;
    }
}
