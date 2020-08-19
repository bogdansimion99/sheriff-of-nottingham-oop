package com.tema1.main;

import com.tema1.cards.Bonuses;
import com.tema1.helpers.PointsComparator;
import com.tema1.players.Basic;
import com.tema1.players.Bribe;
import com.tema1.players.Greedy;
import com.tema1.players.Player;

import java.util.ArrayList;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        /*GameInputLoader gameInputLoader =
                new GameInputLoader("checker\\tests\\in\\1round2players-illegal-only-test2.in",
                        "out\\test1.out");*/
        GameInput gameInput = gameInputLoader.load();
        //TODO implement homework logic
        ArrayList<Player> players;
        players = new ArrayList<>();
        for (int i = 0; i < gameInput.getPlayerNames().size(); i++) {
            //player.set(i, new Player(i, false));
            if (gameInput.getPlayerNames().get(i).equals("basic")) {
                players.add(i, new Basic(i, false, "BASIC"));
            } else if (gameInput.getPlayerNames().get(i).equals("greedy")) {
                players.add(i, new Greedy(i, false, "GREEDY"));
            } else if (gameInput.getPlayerNames().get(i).equals("bribed")) {
                players.add(i, new Bribe(i, false, "BRIBED"));
            }
        }
        int counter = 0; // for distributing cards
        // The functionality of the whole game (almost) is here!!!
        for (int i = 1; i <= gameInput.getRounds(); i++) {
            for (int j = 0; j < gameInput.getPlayerNames().size(); j++) {
                for (int k = 0; k < gameInput.getPlayerNames().size(); k++) {
                    if (j == k) {
                        players.get(k).setRole(true);
                        continue;
                    }
                    // The current player is player[k], the current sub round is j,
                    // and the current round is i
                    players.get(k).pickGoods(players.get(k), counter, i, gameInput);
                    counter++;
                }
                for (int k = 0; k < gameInput.getPlayerNames().size(); k++) {
                    if (j == k) {
                        continue;
                    }
                    players.get(j).getPointsSheriff(players.get(j), players.get(k), gameInput.
                            getPlayerNames().size());
                    players.get(k).getPointsSeller(players.get(k), players.get(j), gameInput.
                            getPlayerNames().size());
                }
            }
        }
        // Must apply bonuses for each player
        for (int i = 0; i < gameInput.getPlayerNames().size(); i++) {
            Bonuses.getInstance().loadTotalGoods(players.get(i));
        }
        for (int i = 0; i < gameInput.getPlayerNames().size(); i++) {
            Bonuses.getInstance().selling(players.get(i));
        }
        Bonuses.getInstance().getBonuses(players);
        // Print the ranking :)
        PointsComparator pointsComparator = new PointsComparator();
        players.sort(pointsComparator);
        for (Player player : players) {
            System.out.println(player.getNumber() + " " + player.getType() + " " + player.
                    getPoints());
        }
    }
}
