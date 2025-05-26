package Player;

import Game.Skeleton;
import Player.Mushroom.MushroomBody;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Entomologist player who collects and interacts with insects.
 * The Entomologist has a list of insects and a method to calculate their score.
 */
public class Entomologist extends Player implements Serializable {
    // Attributes
    private ArrayList<Insect> insects;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to initialize an Entomologist player with an ID and name.
     * It also initializes an empty list of insects.
     *
     * @param id   The ID of the Entomologist player.
     * @param name The name of the Entomologist player.
     */
    public Entomologist(int id, String name, String col) {
        super(id, name, col); // Call the parent class (Player) constructor
        this.insects = new ArrayList<>(); // Initialize the insects list
    }

    public int getId() {
        return super.getID();
    }

    /**
     * Adds an insect to the Entomologist's collection.
     *
     * @param insect The insect to be added to the collection.
     */
    public void addInsect(Insect insect) {
        this.insects.add(insect);
    }

    /**
     * Calculates the total score of the Entomologist, which is the sum of the action points
     * of all collected insects and the player's own score (from the parent class).
     *
     * @return The total score of the Entomologist.
     */
    public int calculateScore() {
        int totalScore = 0;
        for (Insect insect : insects) {
            totalScore += insect.getActionPoint();
        }
        // Add the player's score (from the parent class) to the total
        totalScore += getScore();
        return totalScore;
    }

    /**
     * Retrieves the list of insects currently collected by the Entomologist.
     *
     * @return The list of insects.
     */
    @Override
    public ArrayList<Insect> getInsects() {
        return insects;
    }

    /**
     * Retrieves the list of MushroomBody objects associated with the Entomologist.
     * <p>
     * This method is currently not implemented and always returns null.
     *
     * @return null, as the method is not implemented.
     */
    @Override
    public List<MushroomBody> getBodies() {
        return null;
    }

    /**
     * Removes a specific insect from the Entomologist's collection.
     *
     * @param insect The insect to be removed from the collection.
     */
    public void removeInsect(Insect insect) {
        this.insects.remove(insect);
    }
}