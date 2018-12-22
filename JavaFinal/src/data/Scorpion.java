package data;

import javafx.scene.image.Image;
import sample.Controller;

import java.util.ArrayList;

public class Scorpion extends Creature
{
    public Scorpion(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/蝎子精.png");
        getView().setImage(image);
        setCamp(2);
        damageValue = 100;
        deadProbability = 0.3;
        if(controller.isOnRecord)
        {
            //System.out.println(this+":"+controller.history.get(this.toString()));
            history = new ArrayList<>(controller.history.get(this.toString()));
        }
    }

    @Override
    public String toString(){ return "蝎子精"; }
}
