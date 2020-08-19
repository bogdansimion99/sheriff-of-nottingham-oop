package com.tema1.helpers;

import com.tema1.players.Player;

import java.util.Comparator;

public class PointsComparator implements Comparator<Player> {
    /**
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(final Player o1, final Player o2) {
        return o2.getPoints() - o1.getPoints();
    }
}
