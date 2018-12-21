package data;

import javafx.scene.image.Image;
import sample.Controller;

import java.util.ArrayList;

public class Monster extends Creature
{
    static int num = 0;
    private String name;
    public Monster(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/小喽啰.png");
        super.getView().setImage(image);
        super.setCamp(2);
        super.setDamageValue(100);
        name = "小喽啰"+String.valueOf(num);
        num++;
        if(controller.isOnRecord)
        {
            //System.out.println(this+":"+controller.history.get(this.toString()));
            history = new ArrayList<>(controller.history.get(this.toString()));
        }
    }

    public String toString() { return name; }
}
