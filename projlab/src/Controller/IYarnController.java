package Controller;

import Player.Insect;
import Player.Mushroom.MushroomYarn;

import java.util.Map;

/**
 * Interface for handling yarn-related events in the game.
 */
public interface IYarnController {

    /**
     * Handles the deletion of a yarn.
     *
     * This method is called when a yarn is deleted and delegates
     * the operation to the `MainController`.
     *
     * @param yarn The `MushroomYarn` object that is being deleted.
     */
    static void onYarndeletion(MushroomYarn yarn) {
        MainController.onYarndeletion(yarn);
    }

    /**
     * Handles the addition of a new yarn.
     *
     * This method is called when a new yarn is added and delegates
     * the operation to the `MainController`.
     *
     * @param yarn The `MushroomYarn` object that is being added.
     */
    static void onYarnAdded(MushroomYarn yarn) {
        MainController.onYarnAdded(yarn);
    }

    /**
     * Handles the disconnection of a yarn.
     *
     * This method is called when a yarn is disconnected and delegates
     * the operation to the `MainController`.
     *
     * @param yarn The `MushroomYarn` object that is being disconnected.
     */
    static void onYarnDisconnected(MushroomYarn yarn) {
        MainController.onYarnDisconnected(yarn);
    }
    boolean eatInsect();
    void setDisconnected();


}
