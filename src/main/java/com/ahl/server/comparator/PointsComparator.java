package com.ahl.server.comparator;

import com.ahl.server.entity.Points;

import java.util.Comparator;

public class PointsComparator implements Comparator<Points> {

    @Override
    public int compare(Points p1, Points p2) {
        if (p1.getPoints() > p2.getPoints()) {
            return 1;
        } else if (p1.getPoints() < p2.getPoints()) {
            return -1;
        } else {
            if (p1.getGoalDifference() > p2.getGoalDifference()) {
                return 1;
            } else
                return -1;
        }

    }
}
