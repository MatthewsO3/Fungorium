package Game.Map;

import Controller.ITectonController;
import Player.Insect;
import Player.Mushroom.MushroomBody;
import Player.Mushroom.MushroomYarn;
import Spore.Spore;
import View.ITectonView;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Tecton implements Serializable, ITectonController, ITectonView {
    // Attributes
    private boolean canGrowBody;
    private int insectMoveThrough;
    private int maxYarns;
    private ArrayList<Tecton> neighbours;
    private MushroomBody body; // 0..1 relationship with MushroomBody
    private final int breakPoint = 8;

    private Insect insect; // 0..1 relationship with Insect
    private ArrayList<Spore> spores; // 0..* relationship with Spore
    private ArrayList<MushroomYarn> yarns; // 0..* relationship with MushroomYarn
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a Tecton object with default values.
     * It initializes attributes and sets up relationships with possible elements.
     */
    public Tecton() {
        this.canGrowBody = true; // Example value, adjust as needed
        this.insectMoveThrough = 0;
        this.maxYarns = 5; // Example value, adjust as needed
        this.neighbours = new ArrayList<>();
        this.body = null; // Initially no MushroomBody
        this.insect = null; // Initially no Insect
        this.spores = new ArrayList<>();
        this.yarns = new ArrayList<>();
    }

    /**
     * Adds a neighboring Tecton to this Tecton, ensuring a bidirectional connection.
     *
     * @param tec The neighboring Tecton to add.
     */
    @Override
    public void addNeighbour(Tecton tec) {
        if (!neighbours.contains(tec)) {
            neighbours.add(tec);
            tec.neighbours.add(this); // Ensure bidirectional relationship
            ITectonController.onNeighbourAdded(this, tec);
        }
    }

    /**
     * Adds a spore to this Tecton.
     *
     * @param spore The spore to be added.
     */
    @Override
    public void addSporeToTecton(Spore spore) {
        this.spores.add(spore);
    }

    /**
     * Attempts to add a yarn to this Tecton if the maximum yarn limit is not exceeded.
     *
     * @param yarn The MushroomYarn to be added.
     * @return true if the yarn was successfully added, false otherwise.
     */
    @Override
    public boolean addYarn(MushroomYarn yarn) {
        if (yarns.size() < maxYarns) {
            yarns.add(yarn);
            return true;
        }
        return false;
    }

    /**
     * Breaks the Tecton, removing all associated entities (MushroomBody, Spores, Yarns, Insect).
     */
    public void breakTecton() {
        this.body = null;
        this.spores.clear();
        for (int i = 0; i < this.yarns.size(); i++) {
            yarns.get(i).deleteYarn();
        }
        this.yarns.clear();
        for (int i = 0; i < neighbours.size(); i++) {
            neighbours.get(i).removeNeighbour(this);
        }
        if (this.insect != null) {
            this.insect.killInsect();
        }
        Tecton firts = new Tecton();
        Tecton second = new Tecton();
        for (Tecton neighbour : neighbours) {
            if (neighbour != null) {
                firts.addNeighbour(neighbour);
                second.addNeighbour(neighbour);
            }
        }
        ITectonController.onTectondeletion(this, firts,second);
    }

    /**
     * Checks if a MushroomBody can grow on this Tecton.
     *
     * @return true if it can grow, false otherwise.
     */
    public boolean canGrowBody() {
        return canGrowBody;// Can grow a body if none exists
    }

    /**
     * Determines if a MushroomBody can grow on this Tecton based on its conditions.
     *
     * @return true if the body can grow, false otherwise.
     */
    public boolean canGrow() {
        if (!canGrowBody()) {
            return false;
        }
        return !hasBody();

    }

    /**
     * Checks if a given Tecton is a neighbor.
     *
     * @param targetTecton The Tecton to check.
     * @return true if the target is a neighbor, false otherwise.
     */
    public boolean checkIfNeighbour(Tecton targetTecton) {
        return neighbours.contains(targetTecton);
    }

    /**
     * Checks if the Tecton should break based on the number of insects that have moved through it.
     *
     * @return true if it must break, false otherwise.
     */
    public boolean checkIfTectonHasToBreak() {
        if (insectMoveThrough > breakPoint) {
            System.out.println("[i] Tecton has to break!");
            return true;
        }
        return false;
    }

    /**
     * Checks if there is a direct path between this Tecton and the target Tecton via MushroomYarns.
     *
     * @param target The target Tecton to check for a connection.
     * @return true if a path exists, false otherwise.
     */
    public boolean checkIfPathExists(Tecton target) {
        if (target == null) return false;

        for (MushroomYarn yarn : this.yarns) {
            if (yarn.getSourceTecton() == target || yarn.getTargetTecton() == target) return true;
        }
        for (MushroomYarn yarn : target.getYarns()) {
            if (yarn.getSourceTecton() == this || yarn.getTargetTecton() == this) return true;
        }
        return false;
    }

    /**
     * Checks if the maximum number of MushroomYarns has been reached for this Tecton.
     *
     * @return true if the number of yarns is at the maximum limit, false otherwise.
     */
    public boolean fullYarns() {
        return yarns.size() == maxYarns;
    }

    /**
     * Retrieves the neighboring Tectons.
     *
     * @return List of neighboring Tectons.
     */
    @Override
    public ArrayList<Tecton> getNeighbours() {
        return neighbours;
    }

    /**
     * Retrieves the MushroomBody on this Tecton, if any.
     *
     * @return The MushroomBody or null if none exists.
     */
    @Override
    public MushroomBody getBody() {
        return body;
    }

    /**
     * Retrieves the spores present on this Tecton.
     *
     * @return List of spores.
     */
    @Override
    public ArrayList<Spore> getSpores() {
        if (spores.isEmpty()) {
            return null;
        }
        return spores;
    }


    /**
     * Retrieves the MushroomYarns attached to this Tecton.
     *
     * @return List of MushroomYarns.
     */
    @Override
    public ArrayList<MushroomYarn> getYarns() {
        return yarns;
    }

    /**
     * Checks if this Tecton has a MushroomBody.
     *
     * @return true if a MushroomBody is present, false otherwise.
     */
    @Override
    public boolean hasBody() {
        return body != null;
    }

    /**
     * Checks if this Tecton has an Insect.
     *
     * @return true if an Insect is present, false otherwise.
     */
    @Override
    public boolean hasInsect() {
        return insect != null;
    }

    /**
     * Increments the counter tracking how many times an Insect has moved through this Tecton.
     * If the threshold is exceeded, the Tecton may break.
     *
     * @return
     */
    public boolean incInsectMoveThrough() {
        this.insectMoveThrough++;
        if (checkIfTectonHasToBreak()) {
            breakTecton();
            return false;
        }
        return true;
    }

    /**
     * Removes a specific spore from this Tecton.
     *
     * @param spore The spore to be removed.
     */
    public void removeSpore(Spore spore) {
        this.spores.remove(spore);
        ITectonController.onSporeRemoved(spore);
        ITectonController.onSporeRemoved(spore);

    }

    /**
     * Removes a specific MushroomYarn from this Tecton.
     *
     * @param yarn The MushroomYarn to be removed.
     */
    public void removeYarn(MushroomYarn yarn) {
        this.yarns.remove(yarn);
    }

    /**
     * Sets the MushroomBody on this Tecton.
     *
     * @param body The MushroomBody to be set.
     */
    public void setBody(MushroomBody body) {
        this.body = body;
    }

    /**
     * Assigns an Insect to this Tecton and increments the movement counter.
     *
     * @param insect The Insect to be placed on this Tecton.
     */
    public boolean setInsect(Insect insect) {
        this.insect = insect;
        return incInsectMoveThrough();
    }

    /**
     * Sets whether a MushroomBody can grow on this Tecton.
     *
     * @param canGrow true if a body can grow, false otherwise.
     */
    public void setCanGrowBody(boolean canGrow) {
        canGrowBody = canGrow;
    }

    @Override
    public void checkYarnDegradation() {}

    /**
     * Removes the Insect from this Tecton.
     */
    public void removeInsect() {
        insect = null;
    }

    /**
     * Retrieves the Insect currently present on this Tecton.
     *
     * @return The `Insect` object if present, or `null` if no Insect is assigned.
     */
    @Override
    public Insect getInsect() {
        return insect;
    }

    /**
     * Initializes the Insect on this Tecton.
     *
     * @param i The `Insect` object to be assigned to this Tecton.
     */
    public void initInsect(Insect i) {
        insect = i;
    }

    /**
     * Checks if there is a MushroomYarn associated with a specific MushroomBody on this Tecton.
     *
     * @param body The `MushroomBody` to check for an associated MushroomYarn.
     * @return `true` if a MushroomYarn with the specified body exists, `false` otherwise.
     */
    public boolean hasYarnWithBody(MushroomBody body) {
        for (MushroomYarn yarn : yarns) {
            if (yarn.getBody() == body) return true;
        }
        return false;
    }

    /**
     * Sets the maximum number of MushroomYarns that can be attached to this Tecton.
     *
     * @param maxYarns The maximum number of MushroomYarns allowed.
     */
    public void setMaxYarns(int maxYarns) {
        this.maxYarns = maxYarns;
    }

    /**
     * Ensures all associated MushroomYarns remain connected to a mushroom.
     */
    public void keepYarnAlive() {
    }

    /**
     * Removes a neighboring Tecton from this Tecton's list of neighbors.
     * This method also notifies the `ITectonController` about the removal of the neighbor.
     *
     * @param tec The `Tecton` object to be removed as a neighbor.
     */
    public void removeNeighbour(Tecton tec) {
        neighbours.remove(tec);
        ITectonController.onNeighbourRemoved(tec, this);
    }
    public boolean hasYarnBetween(Tecton tec)
    {
        for (MushroomYarn yarn : yarns) {
            if (yarn.getTargetTecton() == tec || yarn.getSourceTecton() == tec) return true;
        }
        return false;
    }

    @Override
    public Tecton getTecton() {
        return this;
    }
}