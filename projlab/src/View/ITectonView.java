package View;

import Game.Map.Tecton;
import Player.Insect;
import Player.Mushroom.MushroomBody;
import Player.Mushroom.MushroomYarn;
import Spore.Spore;

import java.util.ArrayList;

public interface ITectonView {
     Tecton getTecton();
     boolean hasBody();
     ArrayList<Tecton> getNeighbours();
     MushroomBody getBody();
     ArrayList<Spore> getSpores();
     ArrayList<MushroomYarn> getYarns();
     boolean hasInsect();
     Insect getInsect();
}
