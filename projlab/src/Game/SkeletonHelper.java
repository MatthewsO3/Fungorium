package Game;

import Game.Map.DegradableTecton;
import Game.Map.Tecton;
import Player.Entomologist;
import Player.Insect;
import Player.Mushroom.MushroomBody;
import Player.Mushroom.MushroomYarn;
import Player.Mycologist;
import Spore.AcceleratorSpore;
import Spore.ParalysingSpore;
import Spore.SlowingSpore;
import Spore.WeakeningSpore;
import Spore.Spore;

import java.util.HashMap;

/**
 * Helper class for managing the game skeleton, including Tectons, MushroomBodies, MushroomYarns, and Spores.
 */
public class SkeletonHelper {
    private static HashMap<String, Tecton> tectons;
    private static HashMap<String, MushroomBody> mushroomBodies;
    private static HashMap<String, MushroomYarn> mushroomYarns;
    private static HashMap<String, Spore> spores;

    private static Mycologist myc;
    private Entomologist ent;
    private Insect insect;

    /**
     * Constructs a SkeletonHelper and initializes the game entities.
     */
    public SkeletonHelper() {
        this.reset();
    }

    /**
     * Adds a MushroomYarn to the collection.
     *
     * @param newYarn the MushroomYarn to add
     * @param my5     the key for the MushroomYarn
     */
    public static void addYarn(MushroomYarn newYarn, String my5) {
        mushroomYarns.put(my5, newYarn);
    }

    /**
     * Adds a MushroomBody to the collection.
     *
     * @param body the MushroomBody to add
     * @param mb3  the key for the MushroomBody
     */
    public static void addBody(MushroomBody body, String mb3) {
        mushroomBodies.put(mb3, body);
    }

    /**
     * Resets the game entities to their initial state.
     */
    public void reset() {
        tectons = new HashMap<>();
        mushroomBodies = new HashMap<>();
        mushroomYarns = new HashMap<>();
        spores = new HashMap<>();
        myc = new Mycologist(1, "Mycologist", "FFFFFF");
        ent = new Entomologist(2, "Entomologist","FFFFFF");

        // Create 5 Tecton objects with names
        initializeTectons();
        insect = new Insect(ent, tectons.get("t1"));
        ent.addInsect(insect);
        // Set up some example relationships between tectons
        setupNeighbours();
        setupMushroomEntities();
        setupSpores();
    }

    /**
     * Initializes the Tectons in the game.
     *
     * This method creates and adds multiple `Tecton` instances to the `tectons` map,
     * including a `DegradableTecton`. It also sets specific properties for certain Tectons.
     */
    private void initializeTectons() {
        // Create 5 Tecton instances with descriptive names
        tectons.put("t1", new Tecton());
        tectons.put("t2", new Tecton());
        tectons.put("t3", new Tecton());
        tectons.put("t4", new Tecton());
        tectons.put("dt1", new DegradableTecton());
        tectons.put("neighbourboth", new Tecton());
        tectons.put("neighbourOft1", new Tecton());
        tectons.put("t5", new Tecton());
        tectons.get("dt1").setCanGrowBody(false);
    }

    /**
     * Sets up neighbor relationships between Tectons.
     *
     * This method establishes connections between various `Tecton` instances
     * to define their neighbor relationships in the game.
     */
    private void setupNeighbours() {
        // Example configuration: CentralTecton is connected to all others
        Tecton t1 = tectons.get("t1");
        Tecton t2 = tectons.get("t2");
        Tecton t3 = tectons.get("t3");
        Tecton t4 = tectons.get("t4");
        Tecton dt1 = tectons.get("dt1");
        Tecton neighbourboth = tectons.get("neighbourboth");
        Tecton neighbourOft1 = tectons.get("neighbourOft1");
        Tecton t5 = tectons.get("t5");

        // Set up neighbour relationships
        t1.addNeighbour(neighbourOft1);
        t1.addNeighbour(neighbourboth);
        t2.addNeighbour(neighbourboth);
        t2.addNeighbour(dt1);
        t3.addNeighbour(neighbourboth);
        t4.addNeighbour(neighbourOft1);
        t4.addNeighbour(dt1);
        dt1.addNeighbour(t2);
        dt1.addNeighbour(t4);
        neighbourboth.addNeighbour(t1);
        neighbourboth.addNeighbour(t2);
        neighbourboth.addNeighbour(t3);
        neighbourOft1.addNeighbour(t1);
        neighbourOft1.addNeighbour(t4);
        t5.addNeighbour(t4);
    }

    /**
     * Sets up Mushroom entities in the game.
     *
     * This method creates `MushroomBody` and `MushroomYarn` instances, assigns them
     * to specific `Tecton` instances, and establishes their relationships.
     */
    private void setupMushroomEntities() {
        // Get the Tectons we'll be working with
        Tecton neighborOft1 = tectons.get("neighbourOft1"); // t1
        Tecton t1 = tectons.get("t1"); // t1 (neighbourOfT1/neighbourOfT)
        Tecton neighbourboth = tectons.get("neighbourboth");  // t2 (neighbourOfT2)
        Tecton t2 = tectons.get("t2");
        Tecton t3 = tectons.get("t3");
        Tecton t4 = tectons.get("t4");
        Tecton dt1 = tectons.get("dt1");
        Tecton t5 = tectons.get("t5");

        // Create MushroomBody (mb3) on t5
        MushroomBody mb3 = new MushroomBody(myc, t5);
        t5.setBody(mb3);
        mushroomBodies.put("mb3", mb3);

        // Create MushroomBody (mb1) on neighborOft1
        MushroomBody mb1 = new MushroomBody(myc, neighborOft1);
        neighborOft1.setBody(mb1);
        mushroomBodies.put("mb1", mb1);

        // Create MushroomBody (mb2) on t3
        MushroomBody mb2 = new MushroomBody(myc, t3);
        t3.setBody(mb2);
        mushroomBodies.put("mb2", mb2);

        // Create MushroomYarn (my1) connecting mb1 to t1
        MushroomYarn my1 = new MushroomYarn(mb1, neighborOft1, t1, true);
        neighborOft1.addYarn(my1);
        t1.addYarn(my1);
        mushroomYarns.put("my1", my1);
        mb1.addYarn(my1);

        // Create MushroomYarn (my2) connecting mb1 to neighbourboth
        MushroomYarn my2 = new MushroomYarn(mb1, t1, neighbourboth, true);
        t1.addYarn(my2);
        neighbourboth.addYarn(my2);
        mushroomYarns.put("my2", my2);
        mb1.addYarn(my2);

        // Create MushroomYarn (my3) connecting mb1 to t2
        MushroomYarn my3 = new MushroomYarn(mb1, neighbourboth, t2, true);
        neighbourboth.addYarn(my3);
        t2.addYarn(my3);
        mushroomYarns.put("my3", my3);
        mb1.addYarn(my3);

        // Create MushroomYarn (my4) connecting mb3 to dt1
        MushroomYarn my4 = new MushroomYarn(mb3, t4, dt1, true);
        dt1.addYarn(my4);
        t4.addYarn(my4);
        mushroomYarns.put("my4", my4);
        mb3.addYarn(my4);

        myc.addMushroomBody(mb1);
        myc.addMushroomBody(mb2);
        myc.addMushroomBody(mb3);
    }

    /**
     * Sets up Spores in the game.
     *
     * This method creates various types of `Spore` instances, assigns them to specific
     * `Tecton` instances, and adds them to the `spores` map.
     */
    private void setupSpores() {
        // Get the Tectons where we'll place the spores
        Tecton neighborOft1 = tectons.get("neighbourOft1"); // t1
        Tecton t1 = tectons.get("t1"); // t1 (neighbourOfT1/neighbourOfT)
        Tecton neighbourboth = tectons.get("neighbourboth");  // t2
        Tecton t2 = tectons.get("t2");
        Tecton t4 = tectons.get("t4");
        // Create one of each type of spore with a default calorie value
        Spore accSpore = new AcceleratorSpore(8, t2);
        t2.addSporeToTecton(accSpore);
        spores.put("as", accSpore);

        Spore acceSpore = new AcceleratorSpore(8, t2);
        t4.addSporeToTecton(acceSpore);
        spores.put("as", acceSpore);

        Spore parSpore = new ParalysingSpore(20, t1);
        t1.addSporeToTecton(parSpore);
        spores.put("ps1", parSpore);
        Spore parSpore2 = new ParalysingSpore(20, t1);
        t1.addSporeToTecton(parSpore2);
        spores.put("ps2", parSpore2);

        Spore slowSpore = new SlowingSpore(12, neighborOft1);
        neighborOft1.addSporeToTecton(slowSpore);
        spores.put("ss", slowSpore);

        Spore weakSpore = new WeakeningSpore(15, neighbourboth);
        neighbourboth.addSporeToTecton(weakSpore);
        spores.put("ws", weakSpore);
    }

    /**
     * Gets the key for a given Tecton.
     *
     * @param tecton the Tecton to find the key for
     * @return the key for the Tecton, or null if not found
     */
    public static String getTectonKey(Tecton tecton) {
        for (HashMap.Entry<String, Tecton> entry : tectons.entrySet()) {
            if (entry.getValue() == tecton) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the key for a given MushroomYarn.
     *
     * @param yarn the MushroomYarn to find the key for
     * @return the key for the MushroomYarn, or null if not found
     */
    public static String getYarnKey(MushroomYarn yarn) {
        for (HashMap.Entry<String, MushroomYarn> entry : mushroomYarns.entrySet()) {
            if (entry.getValue() == yarn) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the key for a given MushroomBody.
     *
     * @param body the MushroomBody to find the key for
     * @return the key for the MushroomBody, or null if not found
     */
    public static String getBodyKey(MushroomBody body) {
        for (HashMap.Entry<String, MushroomBody> entry : mushroomBodies.entrySet()) {
            if (entry.getValue() == body) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the key for a given Spore.
     *
     * @param spore the Spore to find the key for
     * @return the key for the Spore, or null if not found
     */
    public static String getSporeKey(Spore spore) {
        for (HashMap.Entry<String, Spore> entry : spores.entrySet()) {
            if (entry.getValue() == spore) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the collection of Tectons.
     *
     * @return the collection of Tectons
     */
    public HashMap<String, Tecton> getTectons() {
        return tectons;
    }

    /**
     * Gets the collection of MushroomBodies.
     *
     * @return the collection of MushroomBodies
     */
    public HashMap<String, MushroomBody> getMushroomBodies() {
        return mushroomBodies;
    }

    /**
     * Gets the collection of MushroomYarns.
     *
     * @return the collection of MushroomYarns
     */
    public HashMap<String, MushroomYarn> getMushroomYarns() {
        return mushroomYarns;
    }

    /**
     * Gets the collection of Spores.
     *
     * @return the collection of Spores
     */
    public HashMap<String, Spore> getSpores() {
        return spores;
    }

    /**
     * Gets the Mycologist.
     *
     * @return the Mycologist
     */
    public static Mycologist getMyc() {
        return myc;
    }

    /**
     * Gets the Entomologist.
     *
     * @return the Entomologist
     */
    public Entomologist getEnt() {
        return ent;
    }

    /**
     * Gets the Insect.
     *
     * @return the Insect
     */
    public Insect getInsect() {
        return insect;
    }

    /**
     * Adds a Spore to the collection.
     *
     * @param news the Spore to add
     * @param key  the key for the Spore
     */
    public static void addSpore(Spore news, String key) {
        spores.put(key, news);
    }
}