package Game.Map;


import Controller.MainController;
import Game.Skeleton;
import Game.SkeletonHelper;

import Player.Mushroom.MushroomYarn;
import View.MainView;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a degradable tecton in the game.
 *
 * This class extends the `Tecton` class and implements the `Serializable` interface.
 * It includes functionality for yarn degradation based on the number of rounds passed.
 */
public class DegradableTecton extends Tecton implements Serializable {
    // Attributes
    private final int roundToDegrade;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a DegradableTecton object and initializes the roundToDegrade value.
     * The degradation threshold is set to 4 rounds by default.
     */
    public DegradableTecton() {
        super(); // Call the parent Tecton constructor
        this.roundToDegrade = 4; // Example value, adjust as needed
    }

    /**
     * Checks the degradation of yarns based on the number of rounds passed.
     * If the rounds passed are greater than or equal to the roundToDegrade,
     * it deletes the yarns from the tecton.
     * The method prompts the user to input the number of rounds that have passed
     * since the yarn was planted. If the number is greater than or equal to 4,
     * all yarns associated with the tecton will be deleted.
     */
    @Override
    public void checkYarnDegradation() {
        ArrayList<MushroomYarn> yarnsToDelete = this.getYarns();
        for (int i = 0; i < yarnsToDelete.size(); i++) {
            if (yarnsToDelete.get(i).getLifeTime() >= roundToDegrade) {
                MainController.onYarndeletion(yarnsToDelete.get(i));
                yarnsToDelete.get(i).deleteYarn();

            }
        }
    }

}