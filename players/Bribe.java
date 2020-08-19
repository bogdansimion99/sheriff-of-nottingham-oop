package com.tema1.players;

import com.tema1.cards.FrequencyCards;
import com.tema1.goods.GoodsFactory;
import com.tema1.helpers.Constants;
import com.tema1.main.GameInput;

public class Bribe extends Basic {
    private Basic player;
    public Bribe(final int number, final boolean role, final String type) {
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

    public Bribe() {
        super();
    }

    /**
     * @param seller
     * @param counter for distributing cards in order
     * @param round
     * @param input
     */
    @Override
    public void pickGoods(final Player seller, final int counter, final int round,
                          final GameInput input) {
        if (seller.getPoints() < 5) {
            super.pickGoods(seller, counter, round, input);
        } else {
            int[] frequency = new int[Constants.NO_GOODS];
            int[] goods = new int[Constants.NO_GOODS];
            boolean onlyLegal = true;
            seller.setRole(false);
            frequency = FrequencyCards.getInstance().getFrequencyCards(input.getAssetIds(),
                    counter);
            for (int x = 0; x < Constants.NO_GOODS; x++) {
                if (frequency[x] > 0 && x >= Constants.MINIMUM_ID_ILLEGAL) {
                    onlyLegal = false;
                }
            }
            if (onlyLegal) {
                super.pickGoods(seller, counter, round, input);
                return;
            }
            goods = FrequencyCards.getInstance().getBribeCards(frequency, seller.getPoints());
            seller.setBag(goods);
            int illegalCount = 0;
            for (int i = Constants.MINIMUM_ID_ILLEGAL; i < Constants.NO_GOODS; i++) {
                illegalCount += goods[i];
            }
            if (illegalCount > 2) {
                setBribe(10);
            } else if (illegalCount > 0) {
                setBribe(5);
            }
        }
    }

    /**
     * @param seller
     * @param sheriff
     * @return
     */
    @Override
    public boolean verifying(final Player seller, final Player sheriff, final int noPlayers) {
        if (sheriff.getNumber() == 0 && seller.getNumber() == noPlayers - 1) {
            return true;
        } else if (sheriff.getNumber() == noPlayers - 1 && seller.getNumber() == 0) {
            return true;
        } else {
            return Math.abs(seller.getNumber() - sheriff.getNumber()) == 1;
        }
    }

    /**
     * @param seller
     * @return
     */
    @Override
    public int declaring(final Player seller) {
        for(int i = Constants.NO_GOODS - 1; i >= 0; i--) {
            if(seller.getBag()[i] > 0 && i >= 20) {
                return 0;
            } else if (seller.getBag()[i] > 0 && i <= 9) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @param seller
     * @param sheriff
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
                    /*if(declaration) {
                        for(int j = 0; j < i; j++) {
                            points -= seller.getBag()[j] * GoodsFactory.getInstance().getGoodsById(j).
                                    getPenalty();
                        }
                    }*/
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
     */
    @Override
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
