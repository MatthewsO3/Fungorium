package Game.Map;

import Player.Mushroom.MushroomYarn;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a `LifewireTecton` in the game.
 *
 * This class extends the `Tecton` class and implements the `Serializable` interface.
 * It includes functionality to keep all associated `MushroomYarn` objects alive by
 * marking them as connected to a mushroom.
 */
public class LifewireTecton extends Tecton implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new `LifewireTecton` object.
     *
     * This constructor initializes the `LifewireTecton` by calling the parent `Tecton` constructor.
     */
    public LifewireTecton() {
        super();
    }

    /**
     * Ensures all associated `MushroomYarn` objects remain connected to a mushroom.
     *
     * This method iterates through all `MushroomYarn` objects associated with this `LifewireTecton`
     * and sets their `isConnectedToMushroom` property to `true`.
     */
    public void keepYarnAlive() {
        for (MushroomYarn yarn : this.getYarns()) {
            yarn.setIsConnectedToMushroom(true);
        }
    }

}
