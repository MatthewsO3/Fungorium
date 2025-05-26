package View;

import Game.Map.Tecton;
import Player.Mushroom.MushroomYarn;

import java.util.ArrayList;

public interface IMushroomBodyView {
     Tecton getSourceTecton();
     ArrayList<MushroomYarn> getYarns();

}
