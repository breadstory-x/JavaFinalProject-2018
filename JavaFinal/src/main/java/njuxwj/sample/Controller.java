package njuxwj.sample;

import javafx.animation.*;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import njuxwj.data.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AuthorInfo(name = "xwj")
public class Controller {

    @FXML public Pane pane = new Pane();
    public BattleField field = new BattleField(12,18);
    public Camp<Grandpa> camp1;
    public Camp<Snake> camp2;
    public ArrayList<CalabashBrother> gourds;

    public boolean isOnGame;
    public boolean isOnRecord;
    public ExecutorService GoodCampExecutor;
    public ExecutorService BadCampExecutor;

    public Stage primaryStage = new Stage();
    //打开存档的时候先将存档读入，然后在生物体的构造函数中调用
    public HashMap<String, ArrayList<String>> history = new HashMap<>();
    public ImageView victory = new ImageView();
    public ImageView defeat = new ImageView();



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
        //GameControl = Executors.newSingleThreadExecutor();
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
        Monster.num = 0;
        for(int i = 0; i<7;i++) {
            Monster temp = new Monster(field, this);
            camp2.addCreatures(temp);
            list.add(temp.getView());
            list.add(temp.getG_rect());
            list.add(temp.getR_rect());
            list.add(temp.getShield());
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
            list.add(cb.getShield());
        }

        list.add(grandpa.getView());
        list.add(grandpa.getG_rect());
        list.add(grandpa.getR_rect());
        list.add(grandpa.getShield());
        list.add(snake.getView());
        list.add(snake.getG_rect());
        list.add(snake.getR_rect());
        list.add(snake.getShield());
        list.add(scorpion.getView());
        list.add(scorpion.getG_rect());
        list.add(scorpion.getR_rect());
        list.add(scorpion.getShield());


        victory.setImage(new Image("/image/victory.png"));
        victory.setFitWidth(1440);
        victory.setFitHeight(900);
        victory.setOpacity(0);
        defeat.setImage(new Image("/image/defeat.png"));
        defeat.setFitWidth(1440);
        defeat.setFitHeight(900);
        defeat.setOpacity(0);
        list.add(victory);
        list.add(defeat);

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
                fileWriter.close();
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    @FXML private void MenuHelpTriggered(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("帮助");
        alert.setHeaderText(null);
        alert.setContentText("键盘操作：\n1. 空格键：开始游戏\n2. S键：选择路径保存回放(回放需手动保存，否则会在下一局开始后清空)\n3. L键：选择回放记录并播放");
        alert.showAndWait();
    }
    public synchronized void moveToDisplay(final Creature creature, final int x, final int y)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //synchronized (creature) {
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
               // }
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
    public synchronized void beAttackedDisplay(final Creature creature) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //synchronized (creature) {
                    Timeline timeline = new Timeline();
                    timeline.setCycleCount(4);
                    timeline.setAutoReverse(true);
                    timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80 - 10)),
                            new KeyFrame(Duration.millis(30), new KeyValue(creature.getView().xProperty(), creature.getY() * 80)));
                    timeline.play();

                    creature.getG_rect().setWidth(80.0 * creature.HP / creature.maxHP);
                    creature.getR_rect().setX(creature.getY() * 80 + 80.0 * creature.HP / creature.maxHP);
                    creature.getR_rect().setWidth(80.0 - 80.0 * creature.HP / creature.maxHP);
                //}
            }
        });
    }

    public synchronized void failBeAttackedDisplay(final Creature creature)
    {
        Platform.runLater(new Runnable() {
            public void run() {
                FadeTransition ft = new FadeTransition(Duration.millis(275), creature.getView());
                ft.setFromValue(1.0);
                ft.setToValue(0.5);
                ft.play();

                FadeTransition ft2 = new FadeTransition(Duration.millis(275), creature.getView());
                ft2.setFromValue(0.5);
                ft2.setToValue(1.0);
                ft2.play();
            }
        });
    }

    public void DefendDisplay(final Creature creature)
    {
        Platform.runLater(new Runnable() {
            public void run() {
                creature.getShield().setX(creature.getY()*80);
                creature.getShield().setY(creature.getX()*75-5);

                ScaleTransition scale = new ScaleTransition(Duration.millis(500), creature.getShield());
                scale.setCycleCount(2);
                scale.setAutoReverse(true);
                scale.setToX(2);
                scale.setToY(2);

                FadeTransition ft = new FadeTransition(Duration.millis(500), creature.getShield());
                ft.setFromValue(1.0);
                ft.setToValue(0.0);

                ParallelTransition parallel = new ParallelTransition(scale, ft);
                parallel.play();
            }
        });
    }

    public synchronized void DeadDisplay(final Creature creature){
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
            System.out.println("妖怪胜利");
            try {
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }
            FadeTransition ft = new FadeTransition(Duration.millis(500), defeat);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
        else if(type == 2)
        {
            isOnGame = false;
            camp1.getLeader().setStatus(Creature.Status.DEAD);
            for (Creature c:camp1.getSoldiers()) {
                c.setStatus(Creature.Status.DEAD);
            }
            System.out.println("葫芦娃胜利");
            try {
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }
            FadeTransition ft = new FadeTransition(Duration.millis(500), victory);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
    }
}
