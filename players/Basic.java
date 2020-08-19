package com.tema1.players;

import com.tema1.cards.FrequencyCards;
import com.tema1.goods.*;
import com.tema1.helpers.Constants;
import com.tema1.main.GameInput;

public class Basic extends Player {
    private Player player;
    public Basic() {
        super();
    }

    public Basic(final int number, final boolean role, final String type) {
        super(number, role, type);
    }

    /**
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player
     */
    public void setPlayer(final Player player) {
        this.player = player;
    }

    /**
     * @param seller
     * @param counter for distributing cards in order
     */

    @Override
    public void pickGoods(final Player seller, final int counter, final int round,
                          final GameInput input) {
        int[] frequency = new int[Constants.NO_GOODS];
        int[] goods = new int[Constants.NO_GOODS];
        boolean legal = false;
        seller.setRole(false);
        frequency = FrequencyCards.getInstance().getFrequencyCards(input.getAssetIds(),
                counter);
        for (int x = 0; x < frequency.length; x++) {
            if (frequency[x] > 0 && x <= Constants.MAXIMUM_ID_LEGAL) {
                legal = true;
            }
        }
        int maxId = FrequencyCards.getInstance().getMax(frequency)[1];
        int max = FrequencyCards.getInstance().getMax(frequency)[0];
        if (legal || seller.getPoints() >= 4) {
            goods[maxId] = max;
        }
        seller.setBag(goods);
    }

    /**
     * @return
     */
    public boolean verifying(final Player seller, final Player sheriff, final int noPlayers) {
        return true;
    }

    /**
     * @param seller
     * @return
     */

    @Override
    public int declaring(final Player seller) {
        for (int i = 0; i < Constants.NO_GOODS; i++) {
            if (seller.getBag()[i] > 0 && i <= Constants.MAXIMUM_ID_LEGAL) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @param seller
     * @param sheriff
     * @return
     */

    @Override
    public void getPointsSeller(final Player seller, final Player sheriff, final int noPlayers) {
        int points = seller.getPoints();
        boolean declaration = true;
        int[] aux = seller.getTotalGoods();
        if (sheriff.verifying(seller, sheriff, noPlayers)) {
            for (int i = 0; i < Constants.NO_GOODS; i++) {
                if (seller.getBag()[i] > 0 && i != declaring(seller)) {
                    points -= seller.getBag()[i]
                            * GoodsFactory.getInstance().getGoodsById(i).getPenalty();
                    declaration = false;
                    // se confisca bunurile
                    // nu am nevoie de confiscare, deoarece seller.bag se actualizeaza cand isi ia
                    // urmatoarele bunuri
                }
            }
            if (declaration) {
                points += seller.getBag()[seller.declaring(seller)] * GoodsFactory.getInstance().
                        getGoodsById(seller.declaring(seller)).getPenalty();
                aux[seller.declaring(seller)] += seller.getBag()[seller.declaring(seller)];
            }
        } else {
            for (int i = 0; i < Constants.NO_GOODS; i++) {
                aux[i] += seller.getBag()[i];
            }
        }
        seller.setTotalGoods(aux);
        seller.setPoints(points);
    }

    /**
     * @param sheriff
     * @param seller
     * @return
     */

    @Override
    public void getPointsSheriff(final Player sheriff, final Player seller, final int noPlayers) {
        boolean declaration = true;
        int points = sheriff.getPoints();
        if (points < Constants.MINIMUM_MONEY_REQUIRED) {
            return;
        }
        for (int j = 0; j < Constants.NO_GOODS; j++) {
            if (seller.getBag()[j] > 0 && j != declaring(seller)) {
                points += seller.getBag()[j] * GoodsFactory.getInstance().getGoodsById(j).
                        getPenalty();
                if(declaration) {
                    for(int i = 0; i < j; i++) {
                        if(i >= 10 && i < 20) {
                            continue;
                        }
                        points += seller.getBag()[i] * GoodsFactory.getInstance().getGoodsById(i).
                                getPenalty();
                    }
                }
                declaration = false;
            }
        }
        if (declaration) {
            points -= seller.getBag()[seller.declaring(seller)] * GoodsFactory.getInstance().
                    getGoodsById(seller.declaring(seller)).getPenalty();
        }
        sheriff.setPoints(points);
    }
}
