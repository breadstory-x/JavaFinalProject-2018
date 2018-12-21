package sample;

import data.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Controller {

    @FXML public MenuItem menuStart = new MenuItem();
    @FXML public MenuItem menuOpen  = new MenuItem();
    @FXML public MenuItem menuSave = new MenuItem();
    @FXML public Pane pane = new Pane();

    public BattleField field = new BattleField(12,18);
    public Camp<Grandpa> camp1;
    public Camp<Snake> camp2;
    public ArrayList<CalabashBrother> gourds;

    public boolean isOnGame;
    public boolean isOnRecord;
    public ExecutorService GoodCampExecutor;
    public ExecutorService BadCampExecutor;
    public ExecutorService GameControl;

    public Stage primaryStage = new Stage();
    public HashMap<String, ArrayList<String>> history = new HashMap<>();

    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    private void initGame()
    {
        ObservableList list = pane.getChildren();
        list.clear();
        GoodCampExecutor= Executors.newCachedThreadPool();
        BadCampExecutor= Executors.newCachedThreadPool();
        GameControl = Executors.newSingleThreadExecutor();
        //创建对象
        gourds = new ArrayList<>();
        CalabashBrother[] cbs = {new CalabashBrother(field, this, 1, "大娃", "红色"),
                new CalabashBrother(field, this, 2, "二娃", "橙色"),
                new CalabashBrother(field, this, 3, "三娃", "黄色"),
                new CalabashBrother(field, this, 4, "四娃", "绿色"),
                new CalabashBrother(field, this, 5, "五娃", "青色"),
                new CalabashBrother(field, this, 6, "六娃", "蓝色"),
                new CalabashBrother(field, this, 7, "七娃", "紫色")};
        gourds.addAll(Arrays.asList(cbs));
        Grandpa grandpa = new Grandpa(field, this);
        camp1 = new Camp<>(grandpa, cbs);

        Snake snake = new Snake(field, this);
        camp2 = new Camp<>(snake);
        Scorpion scorpion = new Scorpion(field, this);
        camp2.addCreatures(scorpion);
        for(int i = 0; i<7;i++) {
            Monster temp = new Monster(field, this);
            camp2.addCreatures(temp);
            list.add(temp.getView());
            list.add(temp.getG_rect());
            list.add(temp.getR_rect());
            //threads.add(new Thread(temp));
        }
        camp1.setNumber(1+gourds.size());
        camp2.setNumber(1+camp2.getSoldiers().size());

        //设置阵型
        Formation.ChangShe(field, camp1, 3);
        Formation.YanXing(field,camp2,9,6);

        //将图片添加到pane中
        for (CalabashBrother cb:gourds) {
            list.add(cb.getView());
            list.add(cb.getG_rect());
            list.add(cb.getR_rect());
        }

        list.add(grandpa.getView());
        list.add(grandpa.getG_rect());
        list.add(grandpa.getR_rect());
        list.add(snake.getView());
        list.add(snake.getG_rect());
        list.add(snake.getR_rect());
        list.add(scorpion.getView());
        list.add(scorpion.getG_rect());
        list.add(scorpion.getR_rect());

        //初始化历史记录
        if(isOnGame) {
            history = new HashMap<>();
            history.put(camp1.getLeader().toString(), new ArrayList<>());
            for (Creature c : camp1.getSoldiers()) {
                history.put(c.toString(), new ArrayList<>());
            }
            history.put(camp2.getLeader().toString(), new ArrayList<>());
            for (Creature c : camp2.getSoldiers()) {
                history.put(c.toString(), new ArrayList<>());
            }
        }
    }

    private void ExecuteThread()
    {
        //执行线程
        for (CalabashBrother cb:gourds) {
            GoodCampExecutor.execute(cb);
        }
        GoodCampExecutor.execute(camp1.getLeader());

        for (Creature temp:camp2.getSoldiers()) {
            BadCampExecutor.execute(temp);
        }
        BadCampExecutor.execute(camp2.getLeader());

        GoodCampExecutor.shutdown();
        BadCampExecutor.shutdown();
    }

    @FXML
    void initialize()
    {
        isOnGame = false;
        isOnRecord = false;
        Platform.runLater(new Runnable() {
            public void run() {
                pane.requestFocus();  //聚焦到pane
            }
        });
        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(!isOnGame)
                {
                    switch (event.getCode()){
                        case SPACE:
                            MenuStartTriggered();break;
                        case S:
                            MenuSaveTriggered();break;
                        case L:
                            MenuOpenTriggered();break;
                    }
                }
            }
        });
    }

    @FXML private void MenuStartTriggered(){
        isOnGame = true;
        System.out.println("开始游戏！");
        initGame();
        ExecuteThread();
    }

    @FXML private void MenuOpenTriggered(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("打开存档");
        File file = fileChooser.showOpenDialog(primaryStage);

        history = new HashMap<>();
        if(file != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    String[] splitArray = str.split(":");
                    //System.out.println(str);
                    ArrayList<String> temp = new ArrayList<>(Arrays.asList(splitArray[1].split(" ")));
                    history.put(splitArray[0],temp);
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("开始回放！");
            isOnRecord = true;
            initGame();
            ExecuteThread();
        }
        else{
            System.out.println("文件打开失败");
        }
    }

    @FXML private void MenuSaveTriggered(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("保存存档");
        File file = fileChooser.showSaveDialog(primaryStage);

        if(file != null)
            try{
                //true表示追加，否则是覆盖写，覆盖写不需要true
                FileWriter fileWriter = new FileWriter(file,true);

                fileWriter.write(camp1.getLeader().toString()+":");
                for (String s:camp1.getLeader().getHistory()) {
                    fileWriter.write(s+" ");
                }
                fileWriter.write("\n");
                for (Creature c : camp1.getSoldiers()) {
                    fileWriter.write(c.toString()+":");
                    for (String s:c.getHistory()) {
                        fileWriter.write(s+" ");
                    }
                    fileWriter.write("\n");
                }
                fileWriter.write(camp2.getLeader().toString()+":");
                for (String s:camp2.getLeader().getHistory()) {
                    fileWriter.write(s+" ");
                }
                fileWriter.write("\n");
                for (Creature c : camp2.getSoldiers()) {
                    fileWriter.write(c.toString()+":");
                    for (String s:c.getHistory()) {
                        fileWriter.write(s+" ");
                    }
                    fileWriter.write("\n");
                }
                /*for(HashMap.Entry<String,ArrayList<String>> m:history.entrySet())
                {
                    //fileWriter.write(m.getKey()+":"+m.getValue()+"\n");
                    fileWriter.write(m.getKey()+":");
                    for (String s:m.getValue()) {
                        fileWriter.write(s+" ");
                    }
                    fileWriter.write("\n");
                }*/
                fileWriter.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("information");
        alert.setHeaderText(null);
        alert.setContentText("MenuSaveTriggered");
        alert.showAndWait();*/
    }


    public synchronized void moveToDisplay(Creature creature, int x, int y)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                synchronized (creature) {
                    Timeline timeline = new Timeline();
                    timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(350),new KeyValue(creature.getView().xProperty(), 80*y)),
                            new KeyFrame(Duration.millis(400),new KeyValue(creature.getView().yProperty(), 75*x+5)),
                            new KeyFrame(Duration.millis(400),new KeyValue(creature.getR_rect().xProperty(), y*80+80*creature.HP/creature.maxHP)),
                            new KeyFrame(Duration.millis(400),new KeyValue(creature.getR_rect().yProperty(), 75*x)),
                            new KeyFrame(Duration.millis(400),new KeyValue(creature.getG_rect().xProperty(), 80*y)),
                            new KeyFrame(Duration.millis(400),new KeyValue(creature.getG_rect().yProperty(), 75*x)));
                    timeline.play();


                    creature.getView().setX(y * 80);
                    creature.getView().setY(x * 75 + 5);
                    creature.getG_rect().setX(y * 80);
                    creature.getG_rect().setY(x * 75);
                    creature.getR_rect().setY(x * 75);
                }
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
                synchronized (creature) {
                    Timeline timeline = new Timeline();
                    timeline.setCycleCount(4);
                    timeline.setAutoReverse(true);
                    timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80 - 10)),
                            new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80)));
                    timeline.play();

                    creature.getG_rect().setWidth(80.0 * creature.HP / creature.maxHP);
                    creature.getR_rect().setX(creature.getY() * 80 + 80.0 * creature.HP / creature.maxHP);
                    creature.getR_rect().setWidth(80.0 - 80.0 * creature.HP / creature.maxHP);
                }
            }
        });
    }

    public synchronized void beCuredDisplay(Creature creature)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                /*Timeline timeline = new Timeline();
                timeline.setCycleCount(4);
                timeline.setAutoReverse(true);
                timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80 - 10)),
                        new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80)));
                timeline.play();*/

                /*ImageView s = new ImageView(new Image("/sample/image/捕获2.png"));
                s.setFitWidth(5);
                s.setFitHeight(5);
                s.setX(creature.getY()*80+10);
                s.setY(creature.getX()*75+65);

                pane.getChildren().add(s);
                TranslateTransition translate =
                        new TranslateTransition(Duration.seconds(2));
                translate.setFromY(50);
                translate.setToY(320);*/
                FadeTransition ft = new FadeTransition(Duration.millis(200), creature.getView());
                ft.setFromValue(1.0);
                ft.setToValue(0.5);
                ft.play();

                FadeTransition ft2 = new FadeTransition(Duration.millis(200), creature.getView());
                ft2.setFromValue(0.5);
                ft2.setToValue(1.0);
                ft2.play();

                //synchronized (creature) {
                    creature.getG_rect().setWidth(80.0 * creature.HP / creature.maxHP);
                    creature.getR_rect().setX(creature.getY() * 80 + 80.0 * creature.HP / creature.maxHP);
                    creature.getR_rect().setWidth(80.0 - 80.0 * creature.HP / creature.maxHP);
                //}
            }
        });
    }
    public synchronized void DeadDisplay(Creature creature){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FadeTransition ft = new FadeTransition(Duration.millis(250), creature.getView());
                ft.setFromValue(1.0);
                ft.setToValue(0.1);
                ft.play();

                FadeTransition ft1 = new FadeTransition(Duration.millis(250), creature.getG_rect());
                ft1.setFromValue(1.0);
                ft1.setToValue(0.0);
                ft1.play();

                FadeTransition ft2 = new FadeTransition(Duration.millis(250), creature.getR_rect());
                ft2.setFromValue(1.0);
                ft2.setToValue(0.0);
                ft2.play();
                //creature.getView().setImage(null);
            }
        });
    }

    public synchronized void GameOverDisplay(int type)
    {
        if(type == 1) {
            isOnGame = false;
            camp2.getLeader().setStatus(Creature.Status.DEAD);
            for (Creature c : camp2.getSoldiers()) {
                c.setStatus(Creature.Status.DEAD);
            }
            /*Text t = new Text(600,50,"妖怪胜利");
            t.setFont(Font.font("黑体",50));
            t.setFill(Color.RED);
            pane.getChildren().add(t);*/
            System.out.println("妖怪胜利");
            /*FadeTransition ft = new FadeTransition(Duration.millis(500), t);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();*/
        }
        else if(type == 2)
        {
            isOnGame = false;
            camp1.getLeader().setStatus(Creature.Status.DEAD);
            for (Creature c:camp1.getSoldiers()) {
                c.setStatus(Creature.Status.DEAD);
            }
            /*Text t = new Text(600,50,"葫芦娃胜利");
            t.setFont(Font.font("黑体",50));
            t.setFill(Color.RED);
            pane.getChildren().add(t);*/
            System.out.println("葫芦娃胜利");
            /*FadeTransition ft = new FadeTransition(Duration.millis(500), t);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();*/
        }
    }
}
