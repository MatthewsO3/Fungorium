package Spore;

import Game.Map.Tecton;
import Game.Skeleton;
import Game.SkeletonHelper;
import Player.Insect;
import Player.Entomologist;
import View.MainView;

import java.util.ArrayList;

/**
 * Visitor class that applies effects of different spores on insects.
 */
public class SporeEffectVisitor implements ISporeEffectVisitor {

    /**
     * Applies the effect of a ParalysingSpore on the target insect.
     *
     * @param ps the ParalysingSpore
     * @param target the insect to be affected
     */
    @Override
    public void visit(ParalysingSpore ps, Insect target) {
        target.getOwner().incScore(ps.getCalorie());
        ps.getTecton().removeSpore(ps);
        target.setMaxActionPoint(0);
        target.setEffectRemainingTime(2);
        target.setStunned();
    }

    /**
     * Applies the effect of a WeakeningSpore on the target insect.
     *
     * @param ws the WeakeningSpore
     * @param target the insect to be affected
     */
    @Override
    public void visit(WeakeningSpore ws, Insect target) {
        target.getOwner().incScore(ws.getCalorie());
        ws.getTecton().removeSpore(ws);
        target.disableYarnCutting();
        target.setEffectRemainingTime(2);
    }

    /**
     * Applies the effect of an AcceleratorSpore on the target insect.
     *
     * @param as the AcceleratorSpore
     * @param target the insect to be affected
     */
    public void visit(AcceleratorSpore as, Insect target) {
        target.getOwner().incScore(as.getCalorie());
        as.getTecton().removeSpore(as);
        target.setMaxActionPoint(3);
        target.setEffectRemainingTime(2);
    }

    /**
     * Applies the effect of a SlowingSpore on the target insect.
     *
     * @param ss the SlowingSpore
     * @param target the insect to be affected
     */
    @Override
    public void visit(SlowingSpore ss, Insect target) {
        target.getOwner().incScore(ss.getCalorie());
        ss.getTecton().removeSpore(ss);

        target.setMaxActionPoint(1);
        target.setEffectRemainingTime(2);
    }

    /**
     * Applies the effect of a FissionSpore on the target insect.
     *
     * The FissionSpore effect removes the spore from its current Tecton,
     * increases the owner's score by the calorie value of the spore, and
     * creates a new insect in one of the neighboring Tectons of the target insect.
     *
     * @param fs the FissionSpore to apply
     * @param target the insect to be affected
     */
    @Override
    public void visit(FissionSpore fs, Insect target) {
        fs.getTecton().removeSpore(fs);

        Entomologist owner = target.getOwner();
        owner.incScore(fs.getCalorie());
        Tecton currentTecton = target.getSourceTecton();
        ArrayList<Tecton> neighbours = currentTecton.getNeighbours();
        if (neighbours != null && !neighbours.isEmpty()) {
            Insect newInsect = new Insect(owner, neighbours.get(0));
        }
    }
}