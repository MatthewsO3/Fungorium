package Controller;

import Game.Map.Tecton;
import Player.Insect;
import Player.Mushroom.MushroomYarn;
import Spore.Spore;

import java.util.ArrayList;

/**
 * Checks if random behavior is enabled in the game.
 *
 * This method returns the current state of the `randomEnabled` flag,
 * which determines whether random behavior is active.
 *
 * @return `true` if random behavior is enabled, `false` otherwise.
 */
public interface IInsectController {

    /**
     * Handles the deletion of an insect.
     *
     * This method is called when an insect is deleted and delegates
     * the operation to the `MainController`.
     *
     * @param ins The `Insect` object that is being deleted.
     */
    static void onInsectDeletion(Insect ins) {
        MainController.onInsectdeletion(ins);
    }

    /**
     * Handles the addition of a new insect.
     *
     * This method is called when a new insect is added and delegates
     * the operation to the `MainController`.
     *
     * @param ins The `Insect` object that is being added.
     */
     static void onInsectAdded(Insect ins) {
        MainController.onInsectAdded(ins);
    }
     boolean hasActionPoint();
     boolean eatSpore();
     boolean cutYarn(MushroomYarn yarn);
     void disableYarnCutting();


    }
