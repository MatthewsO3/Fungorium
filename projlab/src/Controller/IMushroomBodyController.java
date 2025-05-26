package Controller;

import Game.Map.Tecton;
import Player.Mushroom.MushroomBody;
import Player.Mushroom.MushroomYarn;
import Spore.Spore;

/**
 * Interface for handling mushroom body-related events in the game.
 */
public interface IMushroomBodyController {

    /**
     * Handles the addition of a new mushroom body.
     *
     * This method is called when a new mushroom body is added and delegates
     * the operation to the `MainController`.
     *
     * @param mb3 The `MushroomBody` object that is being added.
     */
    static void onMushroomBodyAdded(MushroomBody mb3) {
        MainController.onMushroomBodyAdded(mb3);
    }

    /**
     * Handles the event when a mushroom body is marked as dead.
     *
     * This method is called when a mushroom body dies and delegates
     * the operation to the `MainController`.
     *
     * @param body The `MushroomBody` object that is marked as dead.
     */
    static void onDead(MushroomBody body) {
        MainController.onDead(body);
    }

    /**
     * Handles the addition of a new spore.
     *
     * This method is called when a new spore is added and delegates
     * the operation to the `MainController`.
     *
     * @param spore The `Spore` object that is being added.
     */
    static void onSporeAdded(Spore spore) {
        MainController.onSporeAdded(spore);
    }
    boolean initiateMushroomYarnGrowth(Tecton targetTecton);
    boolean initiateMushroomBodyGrowth(Tecton targetTecton, boolean needSpore) ;
    boolean releaseSpore(Tecton tec, String type) ;
    void setDead();

    }
