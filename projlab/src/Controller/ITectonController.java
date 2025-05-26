package Controller;

import Game.Map.Tecton;
import Player.Mushroom.MushroomYarn;
import Spore.Spore;
import View.MainView;

/**
 * Interface for handling tecton-related events in the game.
 */
public interface ITectonController {

    /**
     * Handles the deletion of a tecton.
     *
     * This method is called when a tecton is deleted and delegates
     * the operation to the `MainController`.
     *
     * @param tec The `Tecton` object that is being deleted.
     */
    static void onTectondeletion(Tecton tec, Tecton firts, Tecton second) {
        MainController.onTectondeletion(tec,firts,second);
    }

    /**
     * Handles the removal of a spore.
     *
     * This method is called when a spore is removed and delegates
     * the operation to the `MainController`.
     *
     * @param spore The `Spore` object that is being removed.
     */
    static void onSporeRemoved(Spore spore) {
        MainController.onSporeRemoved(spore);
    }



    /**
     * Handles the addition of a neighbor to a tecton.
     *
     * This method is called when a neighbor is added to a tecton and delegates
     * the operation to the `MainController`.
     *
     * @param init      The initial `Tecton` object.
     * @param neighbour The neighboring `Tecton` object being added.
     */
    static void onNeighbourAdded(Tecton init, Tecton neighbour) {
        MainController.onNeighbourAdded(init, neighbour);
    }

    /**
     * Handles the removal of a neighbor from a tecton.
     *
     * This method is called when a neighbor is removed from a tecton and delegates
     * the operation to the `MainController`.
     *
     * @param tec    The `Tecton` object from which the neighbor is being removed.
     * @param tecton The neighboring `Tecton` object being removed.
     */
    static void onNeighbourRemoved(Tecton tec, Tecton tecton) {
        MainController.onNeighbourRemoved(tec, tecton);
    }
    void addNeighbour(Tecton tec);
    void addSporeToTecton(Spore spore);
    boolean addYarn(MushroomYarn yarn) ;
    void checkYarnDegradation() ;

}
