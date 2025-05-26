package Player.Mushroom;

import Controller.IYarnController;
import Controller.MainController;
import Game.Map.Tecton;
import Game.Skeleton;
import Game.SkeletonHelper;
import Player.Insect;
import View.IYarnView;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a MushroomYarn that connects a MushroomBody to two Tectons.
 * The MushroomYarn has attributes related to its life span, connections, and status.
 */
public class MushroomYarn implements Serializable, IYarnController, IYarnView {
    // Attributes
    private MushroomBody body;

    public boolean isConnectedToMushroom() {
        return isConnectedToMushroom;
    }

    private boolean isConnectedToMushroom;
    private int lifeTime;
    private final Tecton sourceTecton;
    private final Tecton targetTecton;
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Constructor to initialize a MushroomYarn with a MushroomBody and two Tectons.
     * The Yarn starts as connected and has an initial lifetime of 0.
     *
     * @param body The MushroomBody to which this Yarn is attached.
     * @param target The target Tecton where this Yarn connects.
     * @param src The source Tecton where this Yarn originates.
     */
    public MushroomYarn(MushroomBody body, Tecton target, Tecton src, boolean init) {
        this.body = body;
        this.targetTecton = target;
        this.sourceTecton = src;
        this.isConnectedToMushroom = true; // Initially connected
        this.lifeTime = 0;
        if (!init)
        {
            IYarnController.onYarnAdded(this);
        }
        sourceTecton.addYarn(this);
        targetTecton.addYarn(this);
        body.addYarn(this);

    }

    /**
     * Deletes the current MushroomYarn by disconnecting it from the MushroomBody
     * and both the source and target Tectons. The Yarn is also removed from each.
     *
     */
    public void deleteYarn() {
        // Logic to delete the yarn, likely involves notifying the body
        body.disconnectYarns(this);
        body.removeYarn(this);

        this.sourceTecton.removeYarn(this);
        this.targetTecton.removeYarn(this);
        this.body = null;


    }

    /**
     * Gets the MushroomBody associated with this MushroomYarn.
     *
     * @return The MushroomBody connected to this MushroomYarn.
     */
    @Override
    public MushroomBody getBody() {
        return body;
    }

    /**
     * Gets the current lifetime of the MushroomYarn.
     *
     * @return The lifetime of the MushroomYarn.
     */
    public int getLifeTime() {
        return lifeTime;
    }

    /**
     * Gets the source Tecton where this MushroomYarn originates from.
     *
     * @return The source Tecton of the MushroomYarn.
     */
    @Override
    public Tecton getSourceTecton() {
        return sourceTecton;
    }

    /**
     * Gets the target Tecton where this MushroomYarn is connected.
     *
     * @return The target Tecton of the MushroomYarn.
     */
    @Override
    public Tecton getTargetTecton() {
        return targetTecton;
    }

    /**
     * Increments the lifetime of the MushroomYarn by 1 and prompts the user
     * to check if two rounds have passed. If two rounds have passed,
     * the Yarn is deleted.
     */
    public boolean incLifeTime() {
        this.lifeTime++;

        if (lifeTime == 0) {
            this.deleteYarn();
            return false;
        }
        return true;
    }

    /**
     * Marks the MushroomYarn as disconnected from the MushroomBody and sets its lifetime to -2.
     */
    public void setDisconnected() {
        this.isConnectedToMushroom = false;
        lifeTime = -2;
        IYarnController.onYarnDisconnected(this);
    }

    /**
     * Sets the lifetime of the MushroomYarn to a specified value.
     *
     * @param time The new lifetime value to set.
     */
    public void setLifeTime(int time) {
        this.lifeTime = time;
    }

    @Override
    public boolean eatInsect(){
        Insect insect1 = sourceTecton.getInsect();
        Insect insect2 = targetTecton.getInsect();
        boolean ret = false;
        if(insect1 != null && insect1.isStunned()){
            insect1.killInsect();
            body.initiateMushroomBodyGrowth(sourceTecton,false);
            ret = true;
        }
        if(insect2 != null && insect2.isStunned()){
            insect2.killInsect();
            body.initiateMushroomBodyGrowth(targetTecton,false);
            ret = true;
        }
        return ret;
    }

    public void setIsConnectedToMushroom(boolean isConnected)
    {
        isConnectedToMushroom = isConnected;
        lifeTime = 1;// ??
    }
}