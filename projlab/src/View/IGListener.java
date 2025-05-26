package View;


import Game.Map.GTecton;
import Player.GInsect;
import Player.Mushroom.GMushroomBody;
import Player.Mushroom.GMushroomYarn;

public interface IGListener {
    void onGTecton(GTecton gt);
    void onGMushroomBody(GMushroomBody gmb);
    void onGMushroomYarn(GMushroomYarn gmy);
    void onGInsect(GInsect ginsect);

}
