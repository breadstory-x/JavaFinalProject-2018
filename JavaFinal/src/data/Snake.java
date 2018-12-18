package data;

import javafx.scene.image.Image;
import sample.Controller;

public class Snake extends Creature
{
    public Snake(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/蛇精.png");
        super.getView().setImage(image);
        super.setCamp(2);
    }
    public String toString(){ return "蛇精"; }
    /*public void cheer(){
        System.out.println("蛇精：加油！");
    };*/
}