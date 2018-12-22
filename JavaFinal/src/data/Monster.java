package data;

import javafx.scene.image.Image;
import sample.Controller;

import java.util.ArrayList;

public class Monster extends Creature
{
    public static int num = 0;
    private String name;
    public Monster(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/小喽啰.png");
        getView().setImage(image);
        setCamp(2);
        damageValue = 100;
        deadProbability = 0.55;
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
