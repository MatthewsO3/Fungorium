package Controller;

import Player.Insect;
import Player.Player;
import Player.Mycologist;
import Spore.Spore;
import View.MainView;
import Game.Map.Tecton;
import Player.Mushroom.MushroomBody;
import Player.Mushroom.MushroomYarn;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;


/**
 * The `MainController` class serves as the central controller for managing
 * the game's state and interactions. It handles various game operations,
 * including player actions, game progression, and object management.
 */
public class MainController {
    private static boolean isGameOver = false;
    private static MainView view;
    private static Player currentPlayer;
    private static  int targetPoint = 50;
    private static int round = 1;

    /**
     * Constructor for the `MainController` class.
     *
     * Initializes the `MainController` with the specified `MainView` instance.
     *
     * @param mview The `MainView` instance used to interact with the game's view layer.
     */
    public MainController(MainView mview) {
        view = mview;
        //this.view = mview;
    }

    /**
     * Advances the game to the next round.
     *
     * This method performs the following actions:
     * - Increments the lifetime of all `MushroomBody` and `MushroomYarn` objects.
     * - Removes degraded `MushroomYarn` objects.
     * - Checks and updates yarn degradation for all `Tecton` objects.
     * - Decrements the effect remaining time for all `Insect` objects.
     * - Logs the round progression.
     */
    public static void NextRound() {
        // Increment lifetimes for MushroomBody
        if(!isGameOver){
            EndGame(view);
        }

        MainView.println("Event triggered: Nextround");
        for (MushroomBody mb : view.getBodies().values()) {
            mb.incLifeTime();
        }

        // Increment lifetimes for MushroomYarn
        Iterator<Map.Entry<String, MushroomYarn>> iterator = view.getYarns().entrySet().iterator();
        ArrayList<MushroomYarn> toRemove = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, MushroomYarn> entry = iterator.next();
            if (!entry.getValue().incLifeTime()) {
                // iterator.remove(); // Safely removes the entry from the map
                toRemove.add(entry.getValue());
            }
        }
        for (MushroomYarn yarn : toRemove) {
            onYarndeletion(yarn);
        }


        // Check yarn degradation for Tectons
        for (Tecton t : view.getTectons().values()) {
            t.checkYarnDegradation();
            t.keepYarnAlive();
        }
        for (Insect i : view.getInsects().values()) {
            i.decEffectRemainingTime();
        }



        // Log the round progression (assuming MainView has a way to log)
        // Since println is private, we could add a public log method in MainView or skip logging


    }

    /**
     * Determines the next player in the game.
     *
     * This method cycles through the list of players (mycologists and entomologists)
     * to determine the next player. If the current player is null, it returns the first player.
     * If the current player is the last in the list, it triggers the next round and returns
     * the first player. Otherwise, it returns the next player in the list.
     *
     * @param current The current `Player` whose turn is ending.
     * @return The next `Player` in the sequence, or null if an error occurs.
     */
    public static Player NextPlayer(Player current) {
        try {
            List<Player> mycologists = view.getMycologists();
            List<Player> entomologists = view.getEntomologists();
            List<Player> players = new ArrayList<>();
            players.addAll(mycologists);
            players.addAll(entomologists);
            if (current == null) {
                MainView.println("Event triggered: Nextplayer");
                currentPlayer = players.get(0);
                SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().playerChanged(currentPlayer, round)));
                return players.get(0);
            }
            if (players.indexOf(current) == players.size()-1 ) {
                MainView.println("Event triggered: Nextplayer");
                NextRound();
                round++;
                currentPlayer = players.get(0);
                SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().playerChanged(currentPlayer, round)));
                return players.get(0);
            } else {
                MainView.println("Event triggered: Nextplayer");
                currentPlayer = players.get(players.indexOf(current) + 1);
                SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().playerChanged(currentPlayer, round)));
                return players.get(players.indexOf(current) + 1);
            }

        } catch (Exception e) {
            System.err.println("Error accessing players: " + e.getMessage());
        }

        return null;
    }

    /**
     * Starts the game loop.
     *
     * This method initializes the game by determining the first player and then
     * enters the main game loop. The game loop continues until the game is over.
     * During each round, it performs actions for the current player and cycles
     * through all players before advancing to the next round.
     */
    public static void StartGame() {
        round = 0;
        Player firstPlayer = NextPlayer(null);
        currentPlayer = firstPlayer;
        if(!isGameOver){
            MainView.println(round + ". round");
            while (!currentPlayer.equals(firstPlayer)) {
                currentPlayer = NextPlayer(currentPlayer);
            }
            round++;
        }
    }


    /**
     * Ends the game and performs necessary cleanup.
     *
     * This method sets the game-over flag to true, logs the game-over event,
     * and performs any additional cleanup or finalization tasks.
     *
     * @param view The `MainView` instance used to interact with the game's view layer.
     */
    public static void EndGame(MainView view) {

        for (Player player : view.getMycologists()) {
            if (player.getScore() >= targetPoint) {
                MainView.println(player.getName() + " has reached " + targetPoint + " points. Game Over!");
                isGameOver =true;
                SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().gameOver(player)));

                return;
            }
        }

        for (Player player : view.getEntomologists()) {
            if (player.getScore() >= targetPoint) {
                MainView.println(player.getName() + " has reached " + targetPoint + " points. Game Over!");
                isGameOver =true;
                SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().gameOver(player)));
                return;
            }
        }

        if (view.getInsects().isEmpty() || view.getBodies().isEmpty()) {
            MainView.println("No Insects or MushroomBodies left on the map. Game Over!");
            isGameOver =true;
            MainView.println("Game Over");

        }
    }

    /**
     * Handles the deletion of a `MushroomYarn` object.
     *
     * This method removes the specified yarn from the game state and logs the deletion.
     * It retrieves the keys for the yarn, its source and target `Tecton`, and its associated
     * `MushroomBody` before removing the yarn from the view's yarn map.
     *
     * @param yarn The `MushroomYarn` object to be deleted.
     */
    public static void onYarndeletion(MushroomYarn yarn) {
        String yarnKey = null;
        String sourceKey = null;
        String targetKey = null;
        String bodyKey = null;
        for (Map.Entry<String, MushroomYarn> entry : view.getYarns().entrySet()) {
            if (entry.getValue().equals(yarn)) {
                yarnKey = entry.getKey();
                break;
            }
        }
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().removeYarn(yarn)));

        // Retrieve the key for the source Tecton
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(yarn.getSourceTecton())) {
                sourceKey = entry.getKey();
                break;
            }
        }
        for (Map.Entry<String, MushroomBody> entry : view.getBodies().entrySet()) {
            if (entry.getValue().equals(yarn.getBody())) {
                bodyKey = entry.getKey();
                break;
            }
        }

        // Retrieve the key for the target Tecton
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(yarn.getTargetTecton())) {
                targetKey = entry.getKey();
                break;
            }

        }
        MainView.println("[" + sourceKey + "]: yarns: Yarn removed: " + "[" + yarnKey + "]");
        MainView.println("[" + targetKey + "]: yarns: Yarn removed: " + "[" + yarnKey + "]");
        MainView.println("[" + bodyKey + "]: yarns: Yarn removed: " + "[" + yarnKey + "]");

        MainView.println("[" + yarnKey + "]: Deleted");
        // Remove the yarn from the map
        view.getYarns().entrySet().removeIf(entry -> entry.getValue().equals(yarn));
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));


    }

    /**
     * Handles the deletion of a `Tecton` object.
     *
     * This method removes the specified tecton from the game state and logs the deletion.
     *
     * @param tec The `Tecton` object to be deleted.
     */
    public static void onTectondeletion(Tecton tec, Tecton first, Tecton second) {
        String name = "";
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(tec)) {
                MainView.println("[" + entry.getKey() + "]: Deleted");
                name = entry.getKey();
                break;
            }
        }
        for (Map.Entry<int[], Tecton> entry : view.getTectonPosMap().entrySet()) {
            if (entry.getValue().equals(tec)) {
                int[] pos = entry.getKey();
                view.getTectons().put(name+"'", first);
                view.getTectonPosMap().put(new int[]{pos[0]-25,pos[1]}, first);
                view.getTectons().put(name+"''", second);
                view.getTectonPosMap().put(new int[]{pos[0]+25,pos[1]}, second);
                view.getTectonPosMap().remove(entry.getKey());
                break;
            }
        }
        view.getTectons().entrySet().removeIf(entry2 -> entry2.getValue().equals(tec));
        //SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().removeTecton(tec)));
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));

    }

    /**
     * Handles the deletion of an `Insect` object.
     *
     * This method removes the specified insect from the game state and logs the deletion.
     *
     * @param in The `Insect` object to be deleted.
     */
    public static void onInsectdeletion(Insect in) {
        for (Map.Entry<String, Insect> entry : view.getInsects().entrySet()) {
            if (entry.getValue().equals(in)) {
                MainView.println("[" + in.getOwner().getName() + "]: insects: Deleted: " + "[" + entry.getKey() + "]");
                view.getInsects().entrySet().removeIf(entry2 -> entry2.getValue().equals(in));
                MainView.println("[" + entry.getKey() + "]: Deleted");
                break;
            }
        }
        //SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().removeInsect(in)));
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));

    }

    /**
     * Handles the addition of a `MushroomYarn` object to the game state.
     *
     * This method assigns a unique name to the yarn, adds it to the yarn map,
     * and logs its addition to the associated `MushroomBody` and `Tecton` objects.
     *
     * @param yarn The `MushroomYarn` object to be added.
     */
    public static void onYarnAdded(MushroomYarn yarn) {
        String name = "my" + (view.getYarns().size() + 1);
        view.getYarns().put(name, yarn);
        for (Map.Entry<String, MushroomBody> entry : view.getBodies().entrySet()) {
            if (entry.getValue().equals(yarn.getBody())) {
                MainView.println("[" + entry.getKey() + "]: yarns: Added: " + "[" + name + "]");
                break;
            }
        }
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(yarn.getSourceTecton())) {
                MainView.println("[" + entry.getKey() + "]: yarns: Added: " + "[" + name + "]");
                break;
            }
        }
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(yarn.getTargetTecton())) {
                MainView.println("[" + entry.getKey() + "]: yarns: Added: " + "[" + name + "]");
                break;
            }
        }
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));
    }

    /**
     * Handles the addition of an `Insect` object to the game state.
     *
     * This method assigns a unique name to the insect, adds it to the insect map,
     * and logs its addition to the associated owner and source `Tecton`.
     *
     * @param ins The `Insect` object to be added.
     */
    public static void onInsectAdded(Insect ins) {
        String inskey = null;
        view.getInsects().put("insect" + (view.getInsects().size() + 1), ins);
        for (Map.Entry<String, Insect> entry : view.getInsects().entrySet()) {
            if (entry.getValue().equals(ins)) {
                MainView.println("[" + ins.getOwner().getName() + "]: insects: Added: " + "[" + entry.getKey() + "]");
                inskey = entry.getKey();

                break;
            }
        }
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(ins.getSourceTecton())) {
                MainView.println("[" + entry.getKey() + "]: insects: null -> " + "[" + inskey + "]");
                break;
            }
        }
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));

    }

    /**
     * Handles the removal of a `Spore` object from the game state.
     *
     * This method removes the specified spore from the game state and logs its deletion.
     * It retrieves the key for the associated `Tecton` and the spore itself before
     * removing the spore from the view's spore map.
     *
     * @param spore The `Spore` object to be removed.
     */
    public static void onSporeRemoved(Spore spore) {
        String teckey = null;
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(spore.getTecton())) {
                teckey = entry.getKey();
                break;
            }
        }

        for (Map.Entry<String, Spore> entry : view.getSpores().entrySet()) {
            if (entry.getValue().equals(spore)) {
                MainView.println("[" + teckey + "]: spores: Deleted: [" + entry.getKey() + "]");
                MainView.println("[" + entry.getKey() + "]: Deleted");
                break;
            }
        }
        view.getSpores().entrySet().removeIf(entry2 -> entry2.getValue().equals(spore));
        //SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().removeSpore(spore)));
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));


    }

    /**
     * Retrieves the current player in the game.
     *
     * @return The `Player` object representing the current player.
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Handles the addition of a `Tecton` object to the game state.
     *
     * This method assigns a unique name to the tecton and adds it to the tecton map.
     *
     * @param tec The `Tecton` object to be added.
     */
    public static void onTectonAdded(Tecton tec) {
        view.getTectons().put("t" + (view.getTectons().size() + 1), tec);
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));
    }

    /**
     * Handles the addition of a neighbor relationship between two `Tecton` objects.
     *
     * This method logs the addition of a neighbor relationship between the specified
     * initial `Tecton` and its neighbor.
     *
     * @param init  The initial `Tecton` object.
     * @param neigh The neighboring `Tecton` object to be added.
     */
    public static void onNeighbourAdded(Tecton init, Tecton neigh) {
        String neighKey = null;
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(neigh)) {
                neighKey = entry.getKey();
                break;
            }
        }

        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(init)) {
                MainView.println("[" + entry.getKey() + "]: neighbours: Added: " + "[" + neighKey + "]");
                MainView.println("[" + neighKey + "]: neighbours: Added: " + "[" + entry.getKey() + "]");
                break;

            }
        }
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));
    }

    /**
     * Handles the removal of a neighbor relationship between two `Tecton` objects.
     *
     * This method logs the removal of a neighbor relationship between the specified
     * `Tecton` and its neighbor.
     *
     * @param tec   The `Tecton` object from which the neighbor is being removed.
     * @param neigh The neighboring `Tecton` object to be removed.
     */
    public static void onNeighbourRemoved(Tecton tec, Tecton neigh) {
        String neighKey = null;
        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(neigh)) {
                neighKey = entry.getKey();
                break;
            }
        }

        for (Map.Entry<String, Tecton> entry : view.getTectons().entrySet()) {
            if (entry.getValue().equals(tec)) {
                MainView.println("[" + entry.getKey() + "]: neighbours: Removed: " + "[" + neighKey + "]");
                break;

            }
        }
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));
    }

    /**
     * Handles the addition of a `MushroomBody` object to the game state.
     *
     * This method assigns a unique name to the `MushroomBody`, adds it to the body map,
     * and logs its addition to the associated owner.
     *
     * @param mb The `MushroomBody` object to be added.
     */
    public static void onMushroomBodyAdded(MushroomBody mb) {
        String name = "mb" + (view.getBodies().size() + 1);
        view.getBodies().put(name, mb);
        MainView.println("[" + mb.getOwner().getName() + "]: bodies: Added: " + "[" + name + "]");
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));
    }

    /**
     * Handles the disconnection of a `MushroomYarn` object.
     *
     * This method logs the disconnection of the specified yarn from a mushroom.
     *
     * @param yarn The `MushroomYarn` object to be disconnected.
     */
    public static void onYarnDisconnected(MushroomYarn yarn) {
        for (Map.Entry<String, MushroomYarn> entry : view.getYarns().entrySet()) {
            if (entry.getValue().equals(yarn)) {
                MainView.println("[" + entry.getKey() + "]: isConnectedToMushroom: True -> False");
                break;
            }
        }
        SwingUtilities.invokeLater(() -> view.getGamePanel().updateYarnConnectionState(yarn));
    }

    /**
     * Handles the death of a `MushroomBody` object.
     *
     * This method logs the death of the specified `MushroomBody`.
     *
     * @param body The `MushroomBody` object that has died.
     */
    public static void onDead(MushroomBody body) {
        for (Map.Entry<String, MushroomBody> entry : view.getBodies().entrySet()) {
            if (entry.getValue().equals(body)) {
                MainView.println("[" + entry.getKey() + "]: dead: False -> True");
                break;

            }
        }
    }

    /**
     * Handles the addition of a `Spore` object to the game state.
     *
     * This method assigns a unique name to the spore and adds it to the spore map.
     *
     * @param spore The `Spore` object to be added.
     */
    public static void onSporeAdded(Spore spore) {
        view.getSpores().put("spore" + (view.getSpores().size() + 1), spore);
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> view.getGamePanel().onViewChanged()));

    }
    public static void setTargetPoint(int target) {
        targetPoint = target;
    }

}

