package sample;

import data.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

public class Controller {

    @FXML public Button menuStart = new Button();
    @FXML public Button menuOpen  = new Button();
    @FXML public Button menuSave = new Button();
    @FXML public Pane pane = new Pane();

    public BattleField field = new BattleField(12,18);
    private Camp<Grandpa> camp1;
    private Camp<Snake> camp2;
    ArrayList<Thread> threads;

    private void initGame()
    {
        System.out.println("开始游戏！");
        threads = new ArrayList<>();
        ObservableList list = pane.getChildren();
        //list.clear();
        CalabashBrother[] gourds = {new CalabashBrother(field, this, 1, "大娃", "红色"),
                new CalabashBrother(field, this, 2, "二娃", "橙色"),
                new CalabashBrother(field, this, 3, "三娃", "黄色"),
                new CalabashBrother(field, this, 4, "四娃", "绿色"),
                new CalabashBrother(field, this, 5, "五娃", "青色"),
                new CalabashBrother(field, this, 6, "六娃", "蓝色"),
                new CalabashBrother(field, this, 7, "七娃", "紫色")};
        Grandpa grandpa = new Grandpa(field, this);
        camp1 = new Camp<>(grandpa, gourds);

        Snake snake = new Snake(field, this);
        camp2 = new Camp<>(snake);
        Scorpion scorpion = new Scorpion(field, this);
        camp2.addCreatures(scorpion);
        for(int i = 0; i<7;i++) {
            Monster temp = new Monster(field, this);
            camp2.addCreatures(temp);
            list.add(temp.getView());
            threads.add(new Thread(temp));
        }

        Formation.ChangShe(field, camp1, 3);
        Formation.YanXing(field,camp2,9,6);


        list.add(gourds[0].getView());
        list.add(gourds[1].getView());
        list.add(gourds[2].getView());
        list.add(gourds[3].getView());
        list.add(gourds[4].getView());
        list.add(gourds[5].getView());
        list.add(gourds[6].getView());
        list.add(grandpa.getView());
        list.add(snake.getView());
        list.add(scorpion.getView());


        for(int i = 0;i<gourds.length;i++)
        {
            Thread t = new Thread(gourds[i]);
            threads.add(t);
        }
        threads.add(new Thread(grandpa));
        threads.add(new Thread(snake));
        threads.add(new Thread(scorpion));

        for(int i = 0;i<threads.size();i++)
            threads.get(i).start();
    }

    @FXML
    void initialize()
    {
        Platform.runLater(new Runnable() {
            public void run() {
                pane.requestFocus();  //将用户行为的焦点设置到游戏区域
            }
        });
        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.S) {
                    MenuSaveTriggered();
                }
                else if (event.getCode() == KeyCode.SPACE) {
                    MenuStartTriggered();
                }
                else if (event.getCode() == KeyCode.L) {
                    MenuOpenTriggered();
                }
            }
        });
    }

    @FXML private void MenuStartTriggered(){
        initGame();
    }

    @FXML private void MenuOpenTriggered(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("information");
        alert.setHeaderText(null);
        alert.setContentText("MenuOpenTriggered");
        alert.showAndWait();
    }

    @FXML private void MenuSaveTriggered(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("information");
        alert.setHeaderText(null);
        alert.setContentText("MenuSaveTriggered");
        alert.showAndWait();
    }


    public synchronized void moveToDisplay(Creature creature, int x, int y)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(400),new KeyValue(creature.getView().xProperty(), 80*y)),
                        new KeyFrame(Duration.millis(400),new KeyValue(creature.getView().yProperty(), 75*x)));
                timeline.play();
                creature.getView().setX(y*80);
                creature.getView().setY(x*75);
            }
        });

        /*Timeline timeline  = new Timeline();
        Rectangle rectangle  = new Rectangle(0, 0, 50, 50);
        KeyValue xValue  = new KeyValue(rectangle.xProperty(), 100);
        KeyValue yValue  = new KeyValue(rectangle.yProperty(), 100);
        KeyFrame keyFrame  = new KeyFrame(Duration.millis(1000), xValue, yValue);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();*/
    }

    public synchronized void beAttackedDisplay(Creature creature) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Timeline timeline = new Timeline();
                timeline.setCycleCount(4);
                timeline.setAutoReverse(true);
                timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80 - 10)),
                        new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80)));
                timeline.play();

            }
        });
    }

    public synchronized void DeadDisplay(Creature creature){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FadeTransition ft = new FadeTransition(Duration.millis(250), creature.getView());
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
                //creature.getView().setImage(null);
            }
        });
    }
}
