package data;

import javafx.scene.image.Image;
import sample.Controller;

import java.util.ArrayList;

public class Scorpion extends Creature
{
    public Scorpion(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/蝎子精.png");
        super.getView().setImage(image);
        super.setCamp(2);
        super.setDamageValue(100);
        //super.HP = 200;
        //super.maxHP = 200;
        if(controller.isOnRecord)
        {
            //System.out.println(this+":"+controller.history.get(this.toString()));
            history = new ArrayList<>(controller.history.get(this.toString()));
        }
    }

    @Override
    public String toString(){ return "蝎子精"; }
}
