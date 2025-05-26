package Player;

import Controller.IInsectController;
import Controller.MainController;
import Game.Map.Tecton;
import Game.SkeletonHelper;

import Player.Mushroom.MushroomYarn;

import Game.Skeleton;
import Spore.Spore;
import Spore.SporeEffectVisitor;
import View.IInsectView;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents an insect controlled by the Entomologist.
 * The insect can perform actions like cutting yarn, eating spores, moving to new tectons,
 * and more. It also has action points that limit how many actions it can perform.
 */
public class Insect implements Serializable, IInsectController, IInsectView {
    // Attributes
    private int actionPoint;
    private int maxActionPoint;
    private boolean canCutYarn;

    public int getEffectRemainingTime() {
        return effectRemainingTime;
    }

    private int effectRemainingTime;
    private boolean stunned;
    private transient final SporeEffectVisitor effectVisitor;
    private final Entomologist owner;
    private Tecton sourceTecton;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to initialize an Insect for a specific Entomologist and Tecton.
     *
     * @param ento The Entomologist who owns this insect.
     * @param tec  The Tecton where this insect resides.
     */
    public Insect(Entomologist ento, Tecton tec) {
        this.owner = ento;
        this.sourceTecton = tec;
        effectVisitor = new SporeEffectVisitor();
        actionPoint = 2;
        canCutYarn = true;
        effectRemainingTime = 0;
        stunned = false;
        owner.addInsect(this);
        sourceTecton.initInsect(this);
        maxActionPoint = 2;
        IInsectController.onInsectAdded(this);
    }

    /**
     * Checks if the insect can cut yarn.
     *
     * @return True if the insect can cut yarn, false otherwise.
     */
    public boolean checkCanCutYarn() {
        return canCutYarn;
    }

    /**
     * Cuts a yarn if the insect is allowed to.
     *
     * @param yarn The MushroomYarn object that the insect tries to cut.
     * @return True if the yarn was cut successfully, false otherwise.
     */
    @Override
    public boolean cutYarn(MushroomYarn yarn) {
        if (!checkCanCutYarn()) {
            return false;
        }
        yarn.setDisconnected();
        decActionPoint();
        return true;
    }

    /**
     * Decreases the action points of the insect by 1.
     */
    public void decActionPoint() {
        this.actionPoint--;
    }

    /**
     * Decreases the remaining time of the insect's current effect by 1.
     */
    public void decEffectRemainingTime() {
        if (effectRemainingTime > 0) {
            effectRemainingTime--;
            actionPoint = maxActionPoint;
        }
        if (effectRemainingTime == 0) {
            canCutYarn = true;
            stunned = false;
            actionPoint = 2;
            maxActionPoint = 2;
        }
    }

    /**
     * Disables the ability of the insect to cut yarn.
     */
    @Override
    public void disableYarnCutting() {
        this.canCutYarn = false;
    }

    /**
     * The insect eats a spore from the current Tecton, gaining calories and possibly
     * receiving an effect from the spore. The insect's action points are reduced by 1.
     */
    @Override
    public boolean eatSpore() {
        if (actionPoint < 1) {
            return false;
        }
        ArrayList<Spore> spores = sourceTecton.getSpores();
        if (spores != null) {
            Spore spore = spores.get(0);
            spore.setEffect(effectVisitor, this);
            decActionPoint();
            return true;
        }
        return false;
    }

    /**
     * Gets the current action points of the insect.
     *
     * @return The current action points of the insect.
     */
    public int getActionPoint() {
        return actionPoint;
    }

    /**
     * Gets the Entomologist who owns this insect.
     *
     * @return The Entomologist owner.
     */
    public Entomologist getOwner() {
        return owner;
    }

    /**
     * Gets the current Tecton where the insect resides.
     *
     * @return The Tecton where the insect resides.
     */
    @Override
    public Tecton getSourceTecton() {
        return sourceTecton;
    }

    /**
     * Checks if the insect has any remaining action points.
     *
     * @return True if the insect has remaining action points, false otherwise.
     */
    @Override
    public boolean hasActionPoint() {
        return actionPoint > 0;
    }

    /**
     * Kills the insect and removes it from the Entomologist's insect collection.
     */
    public void killInsect() {
        sourceTecton.removeInsect();
        owner.removeInsect(this);
        IInsectController.onInsectDeletion(this);

    }

    /**
     * Moves the insect to a new Tecton if the insect has enough action points and
     * a path exists between the current and target Tectons.
     *
     * @param target The target Tecton to move the insect to.
     * @return True if the insect successfully moves to the target Tecton, false otherwise.
     */
    public boolean moveToTecton(Tecton target) {
        if (hasActionPoint()) {
            if (!sourceTecton.checkIfPathExists(target)) {
                return false;
            }
            if(target.hasInsect()){
                return false;
            }

            sourceTecton.removeInsect();
            sourceTecton = target;
            if (!sourceTecton.setInsect(this)) {
                return false;
            }
            decActionPoint();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the action points of the insect.
     *
     * @param newPoint The new action point value to set.
     */
    public void setMaxActionPoint(int newPoint) {
        this.maxActionPoint = newPoint;
    }

    /**
     * Sets the current action points of the insect.
     *
     * @param newPoint The new action point value to set for the insect.
     */
    public void setActionPoint(int newPoint) {
        this.actionPoint = newPoint;
    }

    /**
     * Sets the remaining effect time for the insect.
     *
     * @param time The time to set as the effect remaining time.
     */
    public void setEffectRemainingTime(int time) {
        this.effectRemainingTime = time;
    }

    /**
     * Checks if the insect is currently stunned.
     *
     * @return True if the insect is stunned, false otherwise.
     */
    public boolean isStunned() {
        return stunned;
    }

    /**
     * Sets the insect's status to stunned.
     */
    public void setStunned() {
        this.stunned = true;
    }

    /**
     * Updates the Tecton where the insect is currently located.
     *
     * @param sourceTecton The new Tecton to set as the insect's location.
     */
    public void setSourceTecton(Tecton sourceTecton) {
        this.sourceTecton = sourceTecton;
    }

    /**
     * Retrieves the maximum number of action points the insect can have.
     *
     * @return The maximum action points of the insect.
     */
    public int getMaxActionPoint() {
        return maxActionPoint;
    }
}