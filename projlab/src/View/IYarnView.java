package View;

import Game.Map.Tecton;
import Player.Mushroom.MushroomBody;

public interface IYarnView {
     MushroomBody getBody();
     Tecton getSourceTecton();
     Tecton getTargetTecton() ;


}
