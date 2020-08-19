package com.tema1.players;

import com.tema1.helpers.Constants;
import com.tema1.main.GameInput;

public class Player {
    private int number;
    private String type;
    private boolean role; //false = seller, true = sheriff
    private int points;
    private int[] totalGoods = new int[Constants.NO_GOODS];
    private int[] bag = new int[Constants.NO_GOODS];
    private int bribe;
    /**
     * @return
     */
    public int getNumber() {
        return number;
    }
    /**
     * @param number
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return
     */
    public boolean getRole() {
        return role;
    }

    /**
     * @param role
     */
    public void setRole(final boolean role) {
        this.role = role;
    }

    /**
     * @return
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points
     */
    public void setPoints(final int points) {
        this.points = points;
    }

    /**
     * @return
     */
    public int[] getTotalGoods() {
        return totalGoods;
    }

    /**
     * @param totalGoods
     */
    public void setTotalGoods(final int[] totalGoods) {
        this.totalGoods = totalGoods;
    }

    /**
     * @return
     */
    public int[] getBag() {
        return bag;
    }

    /**
     * @param bag
     */
    public void setBag(final int[] bag) {
        this.bag = bag;
    }

    /**
     * @return
     */
    public int getBribe() {
        return bribe;
    }

    /**
     * @param bribe
     */
    public void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    public Player() {
        this.number = -1;
        this.type = "";
        this.role = false;
        this.points = 0;
        this.bribe = 0;
    }

    public Player(final int number, final boolean role, final String type) {
        this.number = number;
        this.type = type;
        this.role = role;
        this.points = Constants.INITIAL_MONEY;
        for (int i : this.totalGoods) {
            this.totalGoods[i] = 0;
        }
        for (int i: this.bag) {
            this.bag[i] = 0;
        }
        this.bribe = 0;
    }


    /**
     * @param seller
     * @param sheriff
     * @param noPlayers
     * @return
     */
    public boolean verifying(final Player seller, final Player sheriff, final int noPlayers) {
        return false;
    }


    /**
     * @param seller
     * @param counter
     * @param round
     * @param input
     */
    public void pickGoods(final Player seller, final int counter, final int round,
                          final GameInput input) {
    }


    /**
     * @param seller
     * @return
     */
    public int declaring(final Player seller) {
        return 0;
    }

    /**
     * @param seller
     * @param sheriff
     * @param noPlayers
     */
    public void getPointsSeller(final Player seller, final Player sheriff, final int noPlayers) {
    }

    /**
     * @param sheriff
     * @param seller
     * @param noPlayers
     */
    public void getPointsSheriff(final Player sheriff, final Player seller, final int noPlayers) {
    }
}
