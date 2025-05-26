package Spore;

import Game.Map.Tecton;
import Player.Insect;

import java.io.Serializable;

/**
 * Represents a FissionSpore, a type of Spore with specific compatibility rules.
 * This class extends the `Spore` class and implements `Serializable`.
 */
public class FissionSpore extends Spore implements Serializable {

    /**
     * Constructs a Spore with the specified calorie value and source Tecton.
     *
     * @param cal the calorie value of the spore
     * @param src the source Tecton where the spore is located
     */
    public FissionSpore(int cal, Tecton src) {
        super(cal, src);
    }

    /**
     * Applies the effect of this spore to a target insect using the provided visitor.
     *
     * @param visitor The visitor implementing the spore effect logic.
     * @param target  The target insect to apply the effect to.
     */
    @Override
    public void setEffect(ISporeEffectVisitor visitor, Insect target) {
        visitor.visit(this, target);
    }

    /**
     * Checks compatibility with another FissionSpore.
     *
     * @param spore The FissionSpore to check compatibility with.
     * @return `true` as FissionSpores are always compatible with each other.
     */
    @Override
    public boolean isCompatible(FissionSpore spore) {
        return true;
    }

    /**
     * Checks compatibility with an AcceleratorSpore.
     *
     * @param spore The AcceleratorSpore to check compatibility with.
     * @return `false` as FissionSpores are not compatible with AcceleratorSpores.
     */
    @Override
    public boolean isCompatible(AcceleratorSpore spore) {
        return false;
    }

    /**
     * Checks compatibility with a ParalysingSpore.
     *
     * @param spore The ParalysingSpore to check compatibility with.
     * @return `false` as FissionSpores are not compatible with ParalysingSpores.
     */
    @Override
    public boolean isCompatible(ParalysingSpore spore) {
        return false;
    }

    /**
     * Checks compatibility with a WeakeningSpore.
     *
     * @param spore The WeakeningSpore to check compatibility with.
     * @return `false` as FissionSpores are not compatible with WeakeningSpores.
     */
    @Override
    public boolean isCompatible(WeakeningSpore spore) {
        return false;
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
