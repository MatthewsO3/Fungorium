package Spore;

import Game.Map.Tecton;
import Player.Insect;

import java.io.Serializable;

/**
 * Abstract class representing a spore.
 */
public abstract class Spore implements Serializable {
    private final int calorie;
    private final Tecton tecton;

    /**
     * Constructs a Spore with the specified calorie value and source Tecton.
     *
     * @param cal the calorie value of the spore
     * @param src the source Tecton where the spore is located
     */
    public Spore(int cal, Tecton src) {
        tecton = src;
        calorie = cal;
    }

    /**
     * Returns the calorie value of the spore.
     *
     * @return the calorie value
     */
    public int getCalorie() {
        return calorie;
    }

    /**
     * Returns the source Tecton where the spore is located.
     *
     * @return the source Tecton
     */
    public Tecton getTecton() { return tecton; }

    /**
     * Sets the effect of the spore on the target insect using the specified visitor.
     *
     * @param visitor the visitor that applies the effect
     * @param target the insect that will be affected by the spore
     */
    public abstract void setEffect(ISporeEffectVisitor visitor, Insect target);

    /**
     * Checks compatibility with a SlowingSpore.
     *
     * @param spore the SlowingSpore to check compatibility with
     * @return `false` as this spore is not compatible with SlowingSpores
     */
    public abstract boolean isCompatible(SlowingSpore spore);

    /**
     * Checks compatibility with a WeakeningSpore.
     *
     * @param spore the WeakeningSpore to check compatibility with
     * @return `false` as this spore is not compatible with WeakeningSpores
     */
    public abstract boolean isCompatible(WeakeningSpore spore);

    /**
     * Checks compatibility with a ParalysingSpore.
     *
     * @param spore the ParalysingSpore to check compatibility with
     * @return `false` as this spore is not compatible with ParalysingSpores
     */
    public abstract boolean isCompatible(ParalysingSpore spore);

    /**
     * Checks compatibility with an AcceleratorSpore.
     *
     * @param spore the AcceleratorSpore to check compatibility with
     * @return `false` as this spore is not compatible with AcceleratorSpores
     */
    public abstract boolean isCompatible(AcceleratorSpore spore);

    /**
     * Checks compatibility with a FissionSpore.
     *
     * @param spore the FissionSpore to check compatibility with
     * @return `false` as this spore is not compatible with FissionSpores
     */
    public abstract boolean isCompatible(FissionSpore spore);

}