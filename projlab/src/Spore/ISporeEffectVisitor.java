package Spore;

import Player.Insect;

/**
 * Interface for visitors that apply effects to insects based on the type of spore.
 */
public interface ISporeEffectVisitor {

        /**
         * Applies the effect of a ParalysingSpore on the target insect.
         *
         * @param ps the ParalysingSpore
         * @param target the insect to be affected
         */
        void visit(ParalysingSpore ps, Insect target);

        /**
         * Applies the effect of a WeakeningSpore on the target insect.
         *
         * @param ws the WeakeningSpore
         * @param target the insect to be affected
         */
        void visit(WeakeningSpore ws, Insect target);

        /**
         * Applies the effect of an AcceleratorSpore on the target insect.
         *
         * @param as the AcceleratorSpore
         * @param target the insect to be affected
         */
        void visit(AcceleratorSpore as, Insect target);

        /**
         * Applies the effect of a SlowingSpore on the target insect.
         *
         * @param ss the SlowingSpore
         * @param target the insect to be affected
         */
        void visit(SlowingSpore ss, Insect target);

        /**
         * Applies the effect of a FissionSpore on the target insect.
         *
         * @param ss the FissionSpore to apply
         * @param target the insect to be affected
         */
        void visit(FissionSpore ss, Insect target);
}