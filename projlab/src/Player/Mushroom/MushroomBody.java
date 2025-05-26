package Player.Mushroom;

import Controller.IMushroomBodyController;
import Game.Map.Tecton;
import Game.SkeletonHelper;
import Player.Mycologist;
import Spore.*;
import Game.Skeleton;
import View.IMushroomBodyView;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class MushroomBody implements Serializable, IMushroomBodyController, IMushroomBodyView {
    // Attributes
    private int actionPoint;
    private boolean dead;
    private boolean isFullyGrown;
    private int lifeTime;
    private final int maxSporeProductionCount;
    private final int roundToFullyGrow;
    private int sporeCooldown;

    public int getSporeReleasedCount() {
        return sporeReleasedCount;
    }

    private int sporeReleasedCount;
    private final Mycologist owner;
    private final Tecton sourceTecton;
    private ArrayList<MushroomYarn> yarns;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new MushroomBody.
     *
     * @param myco The owner of the mushroom.
     * @param tec  The tecton where the mushroom body is located.
     */
    public MushroomBody(Mycologist myco, Tecton tec) {
        this.owner = myco;
        this.sourceTecton = tec;
        this.actionPoint = 2;
        this.dead = false;
        this.isFullyGrown = false;
        this.lifeTime = 0;
        this.maxSporeProductionCount = 5;
        this.roundToFullyGrow = 12;
        this.sporeCooldown = 0;
        this.sporeReleasedCount = 0;
        this.yarns = new ArrayList<>();

    }

    /**
     * Adds a new yarn connection to this mushroom body.
     *
     * @param yarn The MushroomYarn to be added.
     */
    public void addYarn(MushroomYarn yarn) {
        this.yarns.add(yarn);
    }

    /**
     * Checks if the mushroom body is ready to release spores.
     *
     * @return True if ready to release spores, false otherwise.
     */
    public boolean checkIfReadyToRelease() {
        if (!dead && lifeTime >= 6 && sporeCooldown == 0) {
            return true;
        }
        return false;

    }

    /**
     * Checks if the spore spread limit has been reached.
     */
    public boolean checkSporeSpreadLimit() {
        if (sporeReleasedCount >= maxSporeProductionCount) {
            return true;
        }
        return false;
    }

    /**
     * Decreases the action points of the mushroom body by one.
     */
    public void decActionPoint() {
        this.actionPoint--;
    }

    /**
     * Gets the remaining action points.
     *
     * @return The current action points.
     */
    public int getActionPoint() {
        return actionPoint;
    }



    /**
     * Gets the owner of this mushroom body.
     *
     * @return The Mycologist owner.
     */
    public Mycologist getOwner() {
        return owner;
    }

    /**
     * Gets the tecton where this mushroom body originated.
     *
     * @return The source Tecton.
     */
    @Override
    public Tecton getSourceTecton() {
        return sourceTecton;
    }
    @Override
    public ArrayList<MushroomYarn> getYarns() {
        return yarns;
    }



    /**
     * Increases the spore released count if under the limit.
     */
    public void incSporeReleasedCount() {
        sporeReleasedCount++;
        sporeCooldown = 2;
        if (checkSporeSpreadLimit())
        {
            setDead();
        }
    }

    /**
     * Initiates the growth of a MushroomBody on the given Tecton.
     * This method checks if the target Tecton can grow, whether there are enough spores,
     * and if the action points are sufficient to grow a new MushroomBody.
     * If successful, the new MushroomBody is created, added to the owner, and set on the target Tecton.
     *
     * @param targetTecton The Tecton where the MushroomBody will grow.
     * @return true if the MushroomBody was successfully initiated, false otherwise.
     */
    @Override
    public boolean initiateMushroomBodyGrowth(Tecton targetTecton, boolean needSpore) {

        if(needSpore) {
            if (!targetTecton.canGrow() || targetTecton.hasYarnWithBody(this)) {
                return false;
            }
        }

        if (targetTecton.getSpores() == null && needSpore) {
            return false;
        }
        if(targetTecton.getSpores().size() < 2)
        {
            return false;
        }
        if (actionPoint < 1) {
            return false;
        }
        decActionPoint();

        MushroomBody mb3 = new MushroomBody(owner, targetTecton);
        owner.addMushroomBody(mb3);
        IMushroomBodyController.onMushroomBodyAdded(mb3);
        targetTecton.setBody(mb3);
        return true;
    }

    /**
     * Initiates the growth of a MushroomYarn between the current body and a target Tecton.
     * This method checks if the source Tecton and target Tecton are neighbors, and if the target Tecton
     * has space for a new Yarn. The new Yarn is then created and connected between the body and the target Tecton.
     *
     * @param targetTecton The Tecton where the new MushroomYarn will grow.
     * @return true if the MushroomYarn was successfully initiated, false otherwise.
     */
    @Override
    public boolean initiateMushroomYarnGrowth(Tecton targetTecton) {
        Tecton src;
        if (actionPoint < 1) {
            return false;
        }

        MushroomYarn lastYarn = getLastYarn();
        if(sourceTecton.checkIfNeighbour(targetTecton))
        {
            src = sourceTecton;
        }
        else if (lastYarn != null) {
            src = lastYarn.getSourceTecton();

        } else {
            src = getSourceTecton();
        }
        if(src.hasYarnBetween(targetTecton)) {
            return false;
        }
        if (src.checkIfNeighbour(targetTecton)) {
            if (targetTecton.fullYarns()) {
                return false;
            } else {
                MushroomYarn newYarn = new MushroomYarn(this, src, targetTecton,false);
                decActionPoint();
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * Increments the lifetime of the mushroom body and checks if it is now fully grown.
     *
     * @return True if the mushroom body has fully grown, false otherwise.
     */
    public boolean incLifeTime() {
        lifeTime++;
        if(sporeCooldown > 0) {
            sporeCooldown--;
        }
        if (lifeTime >= roundToFullyGrow && !isFullyGrown) {
            isFullyGrown = true;
            return true;
        }
        actionPoint = 2;
        return false;
    }


    /**
     * Releases a spore of a given type to a neighboring Tecton.
     * The method checks if the source Tecton is a neighbor of the target Tecton and if the body is ready to release the spore.
     * If all conditions are met, the specified spore type is created and added to the Tecton.
     *
     * @param tec The Tecton where the spore will be released.
     * @return true if the spore was successfully released, false otherwise.
     */
    @Override
    public boolean releaseSpore(Tecton tec, String type) {
        Tecton src = getSourceTecton();
        boolean isNeighbour = src.checkIfNeighbour(tec);
        if (!isNeighbour) {
            if (isFullyGrown) {
                for (Tecton neighbour : src.getNeighbours()) {
                    if (neighbour.checkIfNeighbour(tec)) {
                        isNeighbour = true;
                        break;
                    }
                }
            }
            else
            {
                return false;
            }

        }
       if(!isNeighbour) {
            return false;
        }
        if (!checkIfReadyToRelease()) {
            return false;
        }
        boolean isSame = true;
        switch (type) {
            case "a": {
                AcceleratorSpore spore = new AcceleratorSpore(8, tec);
                if (tec.getSpores() != null) {
                    isSame = tec.getSpores().get(0).isCompatible(spore);
                }
                if (!isSame) {
                    return false;
                }
                tec.addSporeToTecton(spore);
                break;
            }
            case "p": {
                ParalysingSpore spore = new ParalysingSpore(20, tec);
                if (tec.getSpores() != null) {
                    isSame = tec.getSpores().get(0).isCompatible(spore);
                }
                if (!isSame) {
                    return false;
                }
                tec.addSporeToTecton(spore);
                break;
            }
            case "w": {
                WeakeningSpore spore = new WeakeningSpore(15, tec);
                if (tec.getSpores() != null) {
                    isSame = tec.getSpores().get(0).isCompatible(spore);
                }
                if (!isSame) {
                    return false;
                }
                tec.addSporeToTecton(spore);
                break;
            }
            case "s": {
                SlowingSpore spore = new SlowingSpore(12, tec);
                if (tec.getSpores() != null) {
                    isSame = tec.getSpores().get(0).isCompatible(spore);
                }
                if (!isSame) {
                    return false;
                }
                tec.addSporeToTecton(spore);

                break;
            }
            case "f": {
                FissionSpore spore = new FissionSpore(5, tec);
                if (tec.getSpores() != null) {
                    isSame = tec.getSpores().get(0).isCompatible(spore);
                }
                if (!isSame) {
                    return false;
                }
                tec.addSporeToTecton(spore);
                break;
            }

        }
        decActionPoint();
        incSporeReleasedCount();
        IMushroomBodyController.onSporeAdded(tec.getSpores().get(tec.getSpores().size() - 1));
        return true;
    }

    /**
     * Removes a specific yarn connection from this mushroom body.
     *
     * @param yarn The yarn to be removed.
     * @return True if the yarn was successfully removed, false otherwise.
     */
    public boolean removeYarn(MushroomYarn yarn) {
        return yarns.remove(yarn);
    }

    /**
     * Sets the action points to a specified value.
     *
     * @param newPoint The new action point value.
     */
    public void setActionPoint(int newPoint) {
        this.actionPoint = newPoint;
    }

    /**
     * Sets the mushroom body as dead.
     */
    @Override
    public void setDead() {
        this.dead = true;
        IMushroomBodyController.onDead(this);

        if (yarns != null && !yarns.isEmpty()) {
            disconnectYarns(yarns.get(0));
        }
    }

    /**
     * Retrieves the last yarn connection added.
     *
     * @return The last MushroomYarn or null if no yarns exist.
     */
    public MushroomYarn getLastYarn() {
        if (yarns.isEmpty()) {
            return null;
        }
        return yarns.get(yarns.size() - 1);
    }

    /**
     * Disconnects all Yarn connections starting from the target Yarn.
     * This method marks all the Yarns in the list starting from the target Yarn as disconnected.
     *
     * @param targetYarn The starting Yarn to disconnect.

     */
    public void disconnectYarns(MushroomYarn targetYarn) {
        for (int i = yarns.indexOf(targetYarn); i < yarns.size(); i++) {
                yarns.get(i).setDisconnected();
        }
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }
    public void setSporeReleasedCount(int sporeReleasedCount) {
        this.sporeReleasedCount = sporeReleasedCount;
    }

    public void setSporeCooldowns(int sporeCooldowns) {
        this.sporeCooldown = sporeCooldowns;
    }
    public void setIsDead(boolean d) {
        dead = d;
    }
    public void setFullyGrown(boolean fullyGrown) {
        isFullyGrown = fullyGrown;
    }

}