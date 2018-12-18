package data;

import javafx.scene.image.Image;
import sample.Controller;

public class Scorpion extends Creature
{
    public Scorpion(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/蝎子精.png");
        super.getView().setImage(image);
        super.setCamp(2);
    }

    @Override
    public String toString(){ return "蝎子精"; }
}
