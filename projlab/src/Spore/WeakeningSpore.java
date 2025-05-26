package Spore;

import Game.Map.Tecton;
import Player.Insect;

import java.io.Serializable;

/**
 * Represents a weakening spore that can affect insects.
 */
public class WeakeningSpore extends Spore implements Serializable {

    /**
     * Constructs a WeakeningSpore with the specified calorie value and source Tecton.
     *
     * @param cal the calorie value of the spore
     * @param src the source Tecton where the spore is located
     */
    public WeakeningSpore(int cal, Tecton src) {
        super(cal, src);
    }

    /**
     * Sets the effect of the spore on the target insect using the specified visitor.
     *
     * @param visitor the visitor that applies the effect
     * @param target the insect that will be affected by the spore
     */
    @Override
    public void setEffect(ISporeEffectVisitor visitor, Insect target) {
        visitor.visit(this, target);
    }

    /**
     * Checks compatibility with a FissionSpore.
     *
     * @param spore the FissionSpore to check compatibility with
     * @return `false` as WeakeningSpores are not compatible with FissionSpores
     */
    @Override
    public boolean isCompatible(FissionSpore spore) {
        return false;
    }

    /**
     * Checks compatibility with an AcceleratorSpore.
     *
     * @param spore the AcceleratorSpore to check compatibility with
     * @return `false` as WeakeningSpores are not compatible with AcceleratorSpores
     */
    @Override
    public boolean isCompatible(AcceleratorSpore spore) {
        return false;
    }

    /**
     * Checks compatibility with a ParalysingSpore.
     *
     * @param spore the ParalysingSpore to check compatibility with
     * @return `false` as WeakeningSpores are not compatible with ParalysingSpores
     */
    @Override
    public boolean isCompatible(ParalysingSpore spore) {
        return false;
    }

    /**
     * Checks compatibility with another WeakeningSpore.
     *
     * @param spore the WeakeningSpore to check compatibility with
     * @return `true` as WeakeningSpores are compatible with each other
     */
    @Override
    public boolean isCompatible(WeakeningSpore spore) {
        return true;
    }

    /**
     * Checks compatibility with a SlowingSpore.
     *
     * @param spore The SlowingSpore to check compatibility with.
     * @return `false` as FissionSpores are not compatible with SlowingSpores.
     */
    @Override
    public boolean isCompatible(SlowingSpore spore) {
        return false;
    }
}