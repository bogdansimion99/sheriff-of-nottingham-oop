package com.tema1.main;

import java.util.List;

public class GameInput {
    private static GameInput instance;
    // DO NOT MODIFY
    private final List<Integer> mAssetOrder;
    private final List<String> mPlayersOrder;
    private int mRounds;

    public GameInput() {
        mAssetOrder = null;
        mPlayersOrder = null;
        mRounds = -1;
    }

    public GameInput(final int rounds, final List<Integer> assets, final List<String> players) {
        mAssetOrder = assets;
        mPlayersOrder = players;
        mRounds = rounds;
    }

    public static GameInput getInstance() {
        if (instance == null) {
            instance = new GameInput();
        }
        return instance;
    }

    /**
     * @param instance
     */
    public void setInstance(GameInput instance) {
        GameInput.instance = instance;
    }

    public final List<Integer> getAssetIds() {
        return mAssetOrder;
    }

    public final List<String> getPlayerNames() {
        return mPlayersOrder;
    }

    public final int getRounds() {
        return mRounds;
    }

    public final boolean isValidInput() {
        boolean membersInstantiated = mAssetOrder != null && mPlayersOrder != null;
        boolean membersNotEmpty = mAssetOrder.size() > 0 && mPlayersOrder.size() > 0 && mRounds > 0;

        return membersInstantiated && membersNotEmpty;
    }
}
