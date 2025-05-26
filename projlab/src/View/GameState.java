package View;

import Game.Map.Tecton;
import Player.Insect;
import Player.Mushroom.MushroomBody;
import Player.Mushroom.MushroomYarn;
import Player.Player;
import Spore.Spore;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents the state of the game, including various entities and configurations.
 * This class implements `Serializable` to allow the game state to be saved and loaded.
 */
public class GameState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Maps to store various game entities by their identifiers
    private final Map<String, Tecton> tectons;
    private final Map<String, MushroomBody> bodies;
    private final Map<String, MushroomYarn> yarns;
    private final Map<String, Insect> insects;
    private final Map<String, Spore> spores;

    // Lists to store players categorized as mycologists and entomologists
    private final List<Player> mycologists;
    private final List<Player> entomologists;

    // Flag indicating whether randomization is enabled in the game
    private final boolean randomEnabled;

    /**
     * Constructs a new `GameState` instance by initializing its fields
     * with data from the provided `MainView` object.
     *
     * @param view The `MainView` object containing the initial game state data.
     */
    public GameState(MainView view) {
        this.tectons = new LinkedHashMap<>(view.getTectons());
        this.bodies = new LinkedHashMap<>(view.getBodies());
        this.yarns = new LinkedHashMap<>(view.getYarns());
        this.insects = new LinkedHashMap<>(view.getInsects());
        this.spores = new LinkedHashMap<>(view.getSpores());
        this.mycologists = new ArrayList<>(view.getMycologists());
        this.entomologists = new ArrayList<>(view.getEntomologists());
        this.randomEnabled = view.isRandomEnabled();
    }

    // Getters

    /**
     * Retrieves the map of Tectons in the game.
     *
     * @return A map of Tectons identified by their string keys.
     */
    public Map<String, Tecton> getTectons() { return tectons; }

    /**
     * Retrieves the map of MushroomBody objects in the game.
     *
     * @return A map of MushroomBody objects identified by their string keys.
     */
    public Map<String, MushroomBody> getBodies() { return bodies; }

    /**
     * Retrieves the map of MushroomYarn objects in the game.
     *
     * @return A map of MushroomYarn objects identified by their string keys.
     */
    public Map<String, MushroomYarn> getYarns() { return yarns; }

    /**
     * Retrieves the map of Insects in the game.
     *
     * @return A map of Insects identified by their string keys.
     */
    public Map<String, Insect> getInsects() { return insects; }

    /**
     * Retrieves the map of Spores in the game.
     *
     * @return A map of Spores identified by their string keys.
     */
    public Map<String, Spore> getSpores() { return spores; }

    /**
     * Retrieves the list of players categorized as mycologists.
     *
     * @return A list of players who are mycologists.
     */
    public List<Player> getMycologists() { return mycologists; }

    /**
     * Retrieves the list of players categorized as entomologists.
     *
     * @return A list of players who are entomologists.
     */
    public List<Player> getEntomologists() { return entomologists; }

    /**
     * Checks if randomization is enabled in the game.
     *
     * @return `true` if randomization is enabled, `false` otherwise.
     */
    public boolean isRandomEnabled() { return randomEnabled; }
}