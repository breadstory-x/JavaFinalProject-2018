package data;

import javafx.scene.image.Image;
import sample.Controller;

public class CalabashBrother extends Creature{
    private int number;
    private String name;
    private String color;
    public CalabashBrother(BattleField field, Controller controller,int number, String name, String color)
    {
        super(field, controller);
        this.number = number;
        this.name = name;
        this.color = color;
        String s = "";
        switch (name){
            case "大娃": s = "/sample/image/rwjs1_01_1.png";break;
            case "二娃": s = "/sample/image/rwjs2_01_1.png";break;
            case "三娃": s = "/sample/image/rwjs3_01_1.png";break;
            case "四娃": s = "/sample/image/rwjs4_01_1.png";break;
            case "五娃": s = "/sample/image/rwjs5_01_1.png";break;
            case "六娃": s = "/sample/image/rwjs6_01_1.png";break;
            case "七娃": s = "/sample/image/rwjs7_01_1.png";break;
        }
        Image image = new Image(s);
        super.getView().setImage(image);
        super.setCamp(1);
        super.setDamageValue(40);
    }
    public int getNumber() { return number; }
    @Override
    public String toString() { return name; }
    public String getColor() { return color; }
}
