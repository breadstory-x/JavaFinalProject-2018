package data;

import javafx.scene.image.Image;
import sample.Controller;

public class Grandpa extends Creature
{
    public Grandpa(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/爷爷.png");
        super.getView().setImage(image);
        super.setCamp(1);
    }

    @Override
    public String toString(){ return "爷爷"; }
    /*public void cheer(){
        System.out.println("爷爷：加油！");
    };*/
}
