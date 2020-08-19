package com.tema1.players;

import com.tema1.cards.FrequencyCards;
import com.tema1.goods.*;
import com.tema1.helpers.Constants;
import com.tema1.main.GameInput;

public class Greedy extends Basic {
    private Basic player;
    public Greedy(final int number, final boolean role, final String type) {
        super(number, role, type);
    }

    /**
     * @return
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player
     */
    public void setPlayer(Basic player) {
        this.player = player;
    }

    /**
     * @param seller
     * @param counter
     * @param round
     */

    @Override
    public void pickGoods(final Player seller, final int counter, final int round,
                          final GameInput input) {
        if (round % 2 == 1) {
            super.pickGoods(seller, counter, round, input);
        } else {
            int[] frequency = new int[Constants.NO_GOODS];
            for (int x : frequency) {
                frequency[x] = 0;
            }
            int[] goods = new int[Constants.NO_GOODS];
            for (int x : goods) {
                goods[x] = 0;
            }
            boolean legal = false;
            seller.setRole(false);
            if (!seller.getRole()) {
                frequency = FrequencyCards.getInstance().
                        getFrequencyCards(input.getAssetIds(), counter);
                for (int x = 0; x < Constants.NO_GOODS; x++) {
                    if (frequency[x] > 0 && x <= Constants.MAXIMUM_ID_LEGAL) {
                        legal = true;
                    }
                }
                int maxId = FrequencyCards.getInstance().getMax(frequency)[1];
                int max = FrequencyCards.getInstance().getMax(frequency)[0];
                if (legal || seller.getPoints() >= 4) {
                    goods[maxId] = max;
                }
                int maxProfit = 0;
                // Sa tin minte ca daca nu merg testele sa fac cu mai mic strict
                if ((maxId <= Constants.MAXIMUM_ID_LEGAL && seller.getPoints() >= 4)
                        || seller.getPoints() >= 8) {
                    for (int j = Constants.MINIMUM_ID_ILLEGAL; j < Constants.NO_GOODS; j++) {
                        if (frequency[j] == 0 || (j == maxId && max == 1)) {
                            continue;
                        }
                        if (maxProfit
                                < GoodsFactory.getInstance().getGoodsById(j).getProfit()) {
                            maxProfit = GoodsFactory.getInstance().getGoodsById(j).getProfit();
                            maxId = j; // refolosesc maxId
                        }
                    }
                    goods[maxId] = 1;
                }
            }
            seller.setBag(goods);
        }
    }

    /**
     * @param seller
     * @return
     */

    @Override
    public boolean verifying(final Player seller, final Player sheriff, final int noPlayers) {
        if (seller.getBribe() > 0) {
            return false;
        }
        return true;
    }

    /**
     * @param seller
     * @return
     */

    @Override
    public int declaring(final Player seller) {
        for (int i = 0; i < Constants.NO_GOODS; i++) {
            if (seller.getBag()[i] > 0 && GoodsFactory.getInstance().getGoodsById(i).getType()
                    == GoodsType.Legal) {
                return GoodsFactory.getInstance().getGoodsById(i).getId();
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
                    points -= seller.getBag()[i] * GoodsFactory.getInstance().getGoodsById(i).
                            getPenalty();
                    declaration = false;
                    // se confisca bunurile
                    // acelasi comentariu ca la basic
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
            if (seller.getBribe() > 0) {
                points -= seller.getBribe();
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
    public void getPointsSheriff(final Player sheriff, final Player seller, final int noPlayers) {
        int points = sheriff.getPoints();
        if (sheriff.verifying(seller, sheriff, noPlayers)) {
            boolean declaration = true;
            if (points < Constants.MINIMUM_MONEY_REQUIRED) {
                return;
            }
            for (int j = 0; j < Constants.NO_GOODS; j++) {
                if (seller.getBag()[j] > 0 && j != declaring(seller)) {
                    points += seller.getBag()[j] * GoodsFactory.getInstance().getGoodsById(j).
                            getPenalty();
                    declaration = false;
                }
            }
            if (declaration) {
                points -= seller.getBag()[seller.declaring(seller)] * GoodsFactory.getInstance().
                        getGoodsById(seller.declaring(seller)).getPenalty();
            }
        } else {
            points += seller.getBribe();
        }
        sheriff.setPoints(points);
    }
}
