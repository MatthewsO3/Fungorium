package Player;

import Player.Mushroom.MushroomBody;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Mycologist, a player that can interact with and manage Mushroom bodies.
 * The Mycologist can add Mushroom bodies to their collection and keep track of how many
 * Mushroom bodies they control.
 */
public class Mycologist extends Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // List of Mushroom bodies controlled by the Mycologist
    private ArrayList<MushroomBody> bodies = new ArrayList<>();

    /**
     * Constructor to initialize a Mycologist with an ID and a name.
     * The Mycologist starts with an empty list of Mushroom bodies.
     *
     * @param id   The ID of the Mycologist.
     * @param name The name of the Mycologist.
     */
    public Mycologist(int id, String name, String col) {
        super(id, name, col);
        this.bodies = new ArrayList<>();
    }

    @Override
    public List<Insect> getInsects() {
        return null;
    }

    public int getId() {
        return super.getID();
    }

    /**
     * Adds a MushroomBody to the Mycologist's collection.
     *
     * @param body The MushroomBody to add.
     */
    public void addMushroomBody(MushroomBody body) {
        bodies.add(body);

    }

    /**
     * Returns the list of Mushroom bodies controlled by the Mycologist.
     *
     * @return An ArrayList of MushroomBody objects.
     */
    @Override
    public List<MushroomBody> getBodies() {
        return bodies;
    }

    /**
     * Counts how many Mushroom bodies the Mycologist controls.
     *
     * @return The number of Mushroom bodies.
     */
    public int countMushrooms() {
        return bodies.size();
    }

}
