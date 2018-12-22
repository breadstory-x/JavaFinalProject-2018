package njuxwj.data;

import javafx.scene.image.Image;
import njuxwj.sample.Controller;

import java.util.ArrayList;

@AuthorInfo(name = "xwj")
public class Scorpion extends Creature
{
    public Scorpion(BattleField field, Controller controller){
        super(field,controller);
        try {
            Image image = new Image("/image/蝎子精.png");
            getView().setImage(image);
        }catch (Exception e){
            System.out.println(this+"图片未加载成功(单元测试时请无视)");
        }
        setCamp(2);
        damageValue = 100;
        deadProbability = 0.3;
        if(controller != null && controller.isOnRecord)
        {
            //System.out.println(this+":"+controller.history.get(this.toString()));
            history = new ArrayList<>(controller.history.get(this.toString()));
        }
    }

    @Override
    public String toString(){ return "蝎子精"; }
}
