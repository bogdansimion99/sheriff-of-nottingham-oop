package com.tema1.cards;

import com.tema1.goods.*;
import com.tema1.helpers.Constants;

import java.util.List;

public final class FrequencyCards {
    private static FrequencyCards instance;

    public static FrequencyCards getInstance() {
        if (instance == null) {
            instance = new FrequencyCards();
        }
        return instance;
    }

    public static void setInstance(final FrequencyCards instance) {
        FrequencyCards.instance = instance;
    }

    public int[] getFrequencyCards(final List<Integer> total, final int counter) {
        int[] frequency = new int[Constants.NO_GOODS];
        for (int i = 0; i < Constants.NO_GOODS; i++) {
            frequency[i] = 0;
        }
        for (int i = counter * Constants.NO_CARDS; i < counter * Constants.NO_CARDS
                + Constants.NO_CARDS; i++) {
            frequency[total.get(i)]++;
        }
        return frequency;
    }
    public int[] getMax(final int[] frequency) {
        int maxFrequency = 0;
        int maxId = 0;
        // Pastrez ideea cu legal daca nu merge cu conditia
        for (int i = 0; i <= Constants.MAXIMUM_ID_LEGAL; i++) {
            if (maxFrequency < frequency[i]) {
                maxFrequency = frequency[i];
                maxId = i;
            } else if (maxFrequency == frequency[i] && maxFrequency != 0) {
                if (GoodsFactory.getInstance().getGoodsById(i).getProfit()
                        > GoodsFactory.getInstance().getGoodsById(maxId).getProfit()) {
                    maxId = i;
                } else if (GoodsFactory.getInstance().getGoodsById(i).getProfit() == GoodsFactory.
                        getInstance().getGoodsById(maxId).getProfit()) {
                    if (GoodsFactory.getInstance().getGoodsById(i).getId() > GoodsFactory.
                            getInstance().getGoodsById(maxId).getId()) {
                        maxId = i;
                    }
                }
            }
        }
        if (maxFrequency == 0) {
            maxFrequency = 1;
            int maxProfit = 0;
            for (int i = Constants.MINIMUM_ID_ILLEGAL; i < Constants.NO_GOODS; i++) {
                if (frequency[i] > 0 && maxProfit
                        < GoodsFactory.getInstance().getGoodsById(i).getProfit()) {
                    maxProfit = GoodsFactory.getInstance().getGoodsById(i).getProfit();
                    maxId = i;
                }
            }
        }
        if (maxFrequency > Constants.MAXIMUM_GOODS) {
            maxFrequency = Constants.MAXIMUM_GOODS;
        }
        int[] max = new int[2];
        max[0] = maxFrequency;
        max[1] = maxId;
        return max;
    }

    public int[] getBribeCards(final int[] frequency, int points) {
        int maxProfit = 0;
        int maxId = 0;
        int[] goods = new int[Constants.NO_GOODS];
        for (int i = Constants.MINIMUM_ID_ILLEGAL; i < Constants.NO_GOODS; i++) {
            if (maxProfit < GoodsFactory.getInstance().getGoodsById(i).getProfit()
                    && frequency[i] > 0) {
                maxProfit = GoodsFactory.getInstance().getGoodsById(i).getProfit();
                maxId = i;
            }
        }
        if (Constants.MAXIMUM_GOODS <= frequency[maxId] && points > Constants.MAXIMUM_GOODS
                * GoodsFactory.getInstance().getGoodsById(maxId).getPenalty()) {
            goods[maxId] = Constants.MAXIMUM_GOODS;
            return goods;
        } else if (points < Constants.MAXIMUM_GOODS * GoodsFactory.getInstance().
                getGoodsById(maxId).getPenalty()) {
            int maxCards = points / GoodsFactory.getInstance().getGoodsById(maxId).getPenalty();
            if (maxCards <= frequency[maxId]) {
                goods[maxId] = maxCards;
                maxCards = 0;
                return goods;
            } else {
                maxCards -= frequency[maxId];
                goods[maxId] = frequency[maxId];
            }
            if (maxCards > 0) {
                for (int i = Constants.MINIMUM_ID_ILLEGAL; i < Constants.NO_GOODS; i++) {
                    if (frequency[i] > 0 && maxCards > frequency[i]) {
                        maxCards -= frequency[i];
                        goods[i] = frequency[i];
                    } else if (frequency[i] > 0 && maxCards <= frequency[i]) {
                        goods[i] = maxCards;
                        return goods;
                    }
                }
            }
            if (maxCards > 0) {
                maxProfit = 0;
                maxId = 0;
                maxCards *= 2;
                for (int i = 0; i <= Constants.MAXIMUM_ID_LEGAL; i++) {
                    if (frequency[i] > 0 && maxProfit < GoodsFactory.getInstance().getGoodsById(i).
                            getProfit()) {
                        maxProfit = GoodsFactory.getInstance().getGoodsById(i).getProfit();
                        maxId = i;
                    }
                }
                if (maxCards <= frequency[maxId]) {
                    goods[maxId] = maxCards;
                    return goods;
                } else {
                    goods[maxId] = frequency[maxId];
                    return goods;
                }
            }
        } else if (Constants.MAXIMUM_GOODS > frequency[maxId]) {
            goods[maxId] = frequency[maxId];
            int maxCards = Constants.MAXIMUM_GOODS - frequency[maxId];
            for (int i = Constants.MINIMUM_ID_ILLEGAL; i < Constants.NO_GOODS; i++) {
                if (i == maxId) {
                    continue;
                }
                if (frequency[i] > 0 && maxCards > frequency[i]) {
                    maxCards -= frequency[i];
                    goods[i] = frequency[i];
                } else if (frequency[i] > 0 && maxCards <= frequency[i]) {
                    goods[i] = maxCards;
                    return goods;
                }
            }
            while (maxCards > 0) {
                maxProfit = 0;
                maxId = 0;
                for (int i = 0; i <= Constants.MAXIMUM_ID_LEGAL; i++) {
                    if (frequency[i] > 0 && maxProfit < GoodsFactory.getInstance().getGoodsById(i).
                            getProfit()) {
                        maxProfit = GoodsFactory.getInstance().getGoodsById(i).getProfit();
                        maxId = i;
                    }
                }
                if (maxCards <= frequency[maxId]) {
                    goods[maxId] = maxCards;
                    return goods;
                } else {
                    goods[maxId] = frequency[maxId];
                    maxCards -= frequency[maxId];
                    frequency[maxId] = 0;
                }
            }
        }
        return goods;
    }

    public boolean legal(final int[] max) {
        return max[1] <= Constants.MAXIMUM_ID_LEGAL;
    }
}
