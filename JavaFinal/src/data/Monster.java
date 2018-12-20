package data;

import javafx.scene.image.Image;
import sample.Controller;

public class Monster extends Creature
{
    public Monster(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/小喽啰.png");
        super.getView().setImage(image);
        super.setCamp(2);
        super.setDamageValue(35);
    }

    public String toString() { return "小喽啰"; }
}
