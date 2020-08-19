package com.tema1.cards;

import com.tema1.goods.*;
import com.tema1.helpers.Constants;
import com.tema1.players.Player;

import java.util.ArrayList;

public class Bonuses {
    private static Bonuses instance;

    public static Bonuses getInstance() {
        if (instance == null) {
            instance = new Bonuses();
        }
        return instance;
    }

    public static void setInstance(Bonuses instance) {
        Bonuses.instance = instance;
    }

    /**
     * @param player
     */
    public void loadTotalGoods(final Player player) {
        int[] assets = player.getTotalGoods();
        for (int i = Constants.MINIMUM_ID_ILLEGAL; i < Constants.NO_GOODS; i++) {
            if (player.getTotalGoods()[i] > 0) {
                for (Goods goods: ((IllegalGoods) GoodsFactory.getInstance().getGoodsById(i))
                        .getIllegalBonus().keySet()) {
                    assets[goods.getId()] += ((IllegalGoods) GoodsFactory.getInstance()
                            .getGoodsById(i)).getIllegalBonus().get(goods) * player.
                            getTotalGoods()[i];
                }
            }
        }
        player.setTotalGoods(assets);
    }

    /**
     * @param player
     */
    public void selling(final Player player) {
        for (int x = 0; x < Constants.NO_GOODS; x++) {
            if (player.getTotalGoods()[x] > 0) {
                player.setPoints(player.getPoints() + GoodsFactory.getInstance().getGoodsById(x).
                        getProfit() * player.getTotalGoods()[x]);
            }
        }
    }

    /**
     * @param players
     */
    public void getBonuses(final ArrayList<Player> players) {
        int[][] bonus;
        bonus = new int[2][Constants.MAXIMUM_ID_LEGAL + 1];
        // i = 0 is for the King bonus; i = 1 is for the Queen bonus
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= Constants.MAXIMUM_ID_LEGAL; j++) {
                bonus[i][j] = Constants.MAXIMUM_PLAYERS;
            }
        }
        int max0 = 0;
        int max1 = 0;
        for (int i = 0; i <= Constants.MAXIMUM_ID_LEGAL; i++) {
            for (int j = 0; j < players.size(); j++) {
                if (max1 < players.get(j).getTotalGoods()[i]) {
                    if (max0 < players.get(j).getTotalGoods()[i]) {
                        max1 = max0;
                        max0 = players.get(j).getTotalGoods()[i];
                        bonus[1][i] = bonus[0][i];
                        bonus[0][i] = j;
                    } else if (max0 == players.get(j).getTotalGoods()[i] && max1 < max0) {
                        max1 = max0;
                        bonus[1][i] = j;
                    } else if (max0 > players.get(j).getTotalGoods()[i]) {
                        max1 = players.get(j).getTotalGoods()[i];
                        bonus[1][i] = j;
                    }
                }
            }
            max0 = 0;
            max1 = 0;
            if (bonus[0][i] != Constants.MAXIMUM_PLAYERS) {
                players.get(bonus[0][i]).setPoints(players.get(bonus[0][i]).getPoints()
                        + ((LegalGoods) GoodsFactory.getInstance().getGoodsById(i)).getKingBonus());

            }
            if (bonus[1][i] != Constants.MAXIMUM_PLAYERS) {
                players.get(bonus[1][i]).setPoints(players.get(bonus[1][i]).getPoints()
                        + ((LegalGoods) GoodsFactory.getInstance().getGoodsById(i)).
                        getQueenBonus());
            }
        }
    }
}
