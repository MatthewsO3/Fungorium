package Player;

import Player.Mushroom.MushroomBody;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a player in the game, with an ID, name, and score.
 * This class serves as the base class for all types of players in the game.
 */
public abstract class Player implements Serializable {
    private final int id;
    private  String name;
    private int score;
    private String color;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to initialize a player with an ID, name, and a starting score of 0.
     *
     * @param id   The unique ID of the player.
     * @param name The name of the player.
     */
    public Player(int id, String name, String col) {
        this.id = id;
        this.name = name;
        score = 0;
        color = col;
    }

    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current score of the player.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the unique ID of the player.
     *
     * @return The player's ID.
     */
    public int getID() {
        return id;

    }

    /**
     * Increases the player's score by a given number of points.
     *
     * @param point The number of points to add to the player's score.
     */
    public void incScore(int point) {
        score += point;
    }

    /**
     * A method to perform actions. This method can be overridden by subclasses to define
     * specific actions that the player can perform during the game.
     */
    public void performActions() {

    }

    /**
     * Retrieves the list of Insects associated with the player.
     *
     * This method must be implemented by subclasses to return the specific
     * list of Insects managed by the player.
     *
     * @return A list of `Insect` objects associated with the player.
     */
    public abstract List<Insect> getInsects();

    /**
     * Retrieves the list of MushroomBody objects associated with the player.
     *
     * This method must be implemented by subclasses to return the specific
     * list of MushroomBody objects managed by the player.
     *
     * @return A list of `MushroomBody` objects associated with the player.
     */
    public abstract List<MushroomBody> getBodies();
    public void setName(String nev)
    {
        name = nev;
    }
    public String getColor()
    {
        return color;
    }
    public void setColor(String color)
    {
        this.color = color;
    }
}
