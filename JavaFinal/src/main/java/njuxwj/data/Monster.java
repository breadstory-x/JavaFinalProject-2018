package njuxwj.data;

import javafx.scene.image.Image;
import njuxwj.sample.Controller;

import java.util.ArrayList;

@AuthorInfo(name = "xwj")
public class Monster extends Creature
{
    public static int num = 0;
    private String name;
    public Monster(BattleField field, Controller controller){
        super(field,controller);
        setCamp(2);
        damageValue = 100;
        deadProbability = 0.55;
        name = "小喽啰"+String.valueOf(num);
        num++;
        try {
            Image image = new Image("/image/小喽啰.png");
            getView().setImage(image);
        }catch (Exception e){
            System.out.println(this+"图片未加载成功(单元测试时请无视)");
        }
        if(controller != null && controller.isOnRecord)
        {
            //System.out.println(this+":"+controller.history.get(this.toString()));
            history = new ArrayList<>(controller.history.get(this.toString()));
        }
    }

    public String toString() { return name; }
}
