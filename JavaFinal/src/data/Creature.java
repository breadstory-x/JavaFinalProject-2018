package data;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Rectangle;
import sample.*;

import java.util.ArrayList;


interface FundamentalBattle
{
    void moveTo(int x, int y);
    void beAttacked(int dv);
}

interface Leader
{
    void cure(Creature creature);
}

abstract public class Creature implements Runnable,FundamentalBattle{
    //信息传递属性
    final protected BattleField field;
    final protected Controller controller;
    //UI控制属性
    protected int x;
    protected int y;
    private ImageView view;
    private Rectangle g_rect = new Rectangle();
    private Rectangle r_rect = new Rectangle();
    //战斗状态属性
    protected Status status;
    protected BattleStatus battleStatus;
    protected int camp;
    public int HP;
    public int maxHP;
    protected int damageValue;
    protected double deadProbability;
    protected int paveNum;

    protected ArrayList<String>history = new ArrayList<>();

    public enum Status{ALIVE, DEAD}
    public enum BattleStatus{BE_ATTECKED, BE_CURED, MOVE, OTHER}
    public enum Towards{LEFT,RIGHT,UP,DOWN}

    Creature(BattleField field, Controller controller)
    {
        x = -1;
        y = -1;
        this.field = field;
        this.controller=controller;
        view = new ImageView();
        view.setFitWidth(80);//1440÷18
        view.setFitHeight(75-5);//900÷12
        status = Status.ALIVE;
        battleStatus = BattleStatus.OTHER;
        HP = 100;
        maxHP = 100;
        paveNum = 0;
        g_rect.setHeight(5);
        g_rect.setWidth(80);
        g_rect.setFill(Color.rgb(0,255,0));
        r_rect.setHeight(5);
        r_rect.setWidth(0);
        r_rect.setFill(Color.rgb(255,0,0));
    }
    public void set(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    public void setStatus(Status x){status = x;}
    public BattleStatus getBattleStatus() {
        return battleStatus;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getCamp(){return camp;}
    public void setCamp(int camp){this.camp = camp;}
    public ImageView getView(){return view;}
    public Rectangle getG_rect() { return g_rect; }
    public Rectangle getR_rect() { return r_rect; }
    //public ArrayList<String> getHistory() {
    //    return history;
    //}

    public void run () {
        while (status != Status.DEAD) {
            if (controller.isOnGame) {
                try {
                    Thread.sleep(550);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (status == Status.DEAD)
                    break;
                if(battleStatus == BattleStatus.BE_ATTECKED)
                    battleStatus = BattleStatus.OTHER;
                OnMoveTo();
                try {
                    Thread.sleep(550);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                OnExecute();
            }
            else if(controller.isOnRecord){
                //移动判定回合
                try {
                    Thread.sleep(550);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String s = history.get(paveNum);
                if(s.equals("M"))
                {
                    paveNum++;
                    OnRecordMoveTo();
                }
                if(paveNum >= history.size())
                    break;

                //操作判定回合
                try {
                    Thread.sleep(550);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                s = history.get(paveNum);
                if(s.equals("A"))
                {
                    paveNum++;
                    OnRecordExecute();
                }
                else if(s.equals("D"))
                {
                    //int i = 1;
                    //while(history.get(paveNum+i).equals("D"))
                    //    i++;
                    //paveNum+=i;
                    paveNum++;
                    controller.DefendDisplay(this);
                }
                if(paveNum >= history.size())
                    break;
            }
        }
        System.out.println(this + " 已退出");
    }

    //移动回合
    public void OnMoveTo()
    {
        int dest_x = x, dest_y = y;
        ArrayList<Towards> towards = new ArrayList<>();
        towards.add(Towards.LEFT);
        towards.add(Towards.RIGHT);
        towards.add(Towards.UP);
        towards.add(Towards.DOWN);
        if(x<5)
            towards.remove(Towards.UP);
        if(x>7)
            towards.remove(Towards.DOWN);
        if(y<7)
            towards.remove(Towards.LEFT);
        if(y>9)
            towards.remove(Towards.RIGHT);

        int temp = (int)(Math.random()*towards.size());
        switch (towards.get(temp)){
            case UP:
                dest_x--;break;
            case DOWN:
                dest_x++;break;
            case LEFT:
                dest_y--;break;
            case RIGHT:
                dest_y++;break;
        }
        moveTo(dest_x,dest_y);
    }
    //操作回合
    public void OnExecute()
    {
        boolean state =  true;
        //防止多生物在同一位置
        synchronized (field) {
            for (int i = x - 1; i <= x + 1 && state; i++)
                for (int j = y - 1; j <= y + 1 && state; j++) {
                        if ((i >= 0) && (j >= 0) && (i < field.getRow()) && (j < field.getCol())
                                && !field.getPositions()[i][j].isEmpty()
                                && (field.getPositions()[i][j].getCreature().getCamp() != camp)) {
                            synchronized (field.getPositions()[i][j].getCreature()) {
                                if (field.getPositions()[i][j].getCreature().HP > 0 && field.getPositions()[i][j].getCreature().getBattleStatus()!= BattleStatus.BE_ATTECKED) {
                                    field.getPositions()[i][j].getCreature().beAttacked(damageValue);
                                    state = false;
                                }
                            }
                        }
                }
        }
    }
    //回放移动回合
    public void OnRecordMoveTo()
    {
        int x = Integer.parseInt(history.get(paveNum));
        int y = Integer.parseInt(history.get(paveNum + 1));
        paveNum += 2;
        if(x == this.x && y == this.y)
            return;

        field.getPositions()[this.x][this.y].setCreature(null);
        field.getPositions()[x][y].setCreature(this);
        this.set(x, y);
        controller.moveToDisplay(this, x, y);
    }
    //回放操作回合
    public void OnRecordExecute()
    {
        int x = Integer.parseInt(history.get(paveNum));
        paveNum ++;
        HP -= x;
        controller.beAttackedDisplay(this);
        if (HP <= 0) {
            field.getPositions()[this.x][this.y].setCreature(null);
            setStatus(Creature.Status.DEAD);
            controller.DeadDisplay(this);//播放死亡动画
        }
    }

    //移动
    public void moveTo(int x, int y)
    {
        synchronized (field) {
            if (x >= field.getRow() || y >= field.getCol() || x < 0 || y < 0 || !field.getPositions()[x][y].isEmpty())
            {
                if (controller.isOnGame) {
                    history.add("M");
                    history.add(String.valueOf(this.x));
                    history.add(String.valueOf(this.y));
                }
                return;
            }
            field.getPositions()[this.x][this.y].setCreature(null);
            field.getPositions()[x][y].setCreature(this);
            this.set(x, y);

            if (controller.isOnGame) {
                history.add("M");
                history.add(String.valueOf(x));
                history.add(String.valueOf(y));
            }
            controller.moveToDisplay(this, x, y);
        }
    }
    //被攻击
    public synchronized void beAttacked(int dv)
    {
        battleStatus = BattleStatus.BE_ATTECKED;
        if(isNearLeader())
        {
            history.add("D");
            controller.DefendDisplay(this);
            return;
        }
        int hurt_point = 0;
        if(Math.random()<deadProbability) {
            hurt_point = dv;
        }
        history.add("A");
        history.add(String.valueOf(hurt_point));
        HP -= hurt_point;
        controller.beAttackedDisplay(this);//播放受击动画
        if (HP == 0) {
            field.getPositions()[getX()][getY()].setCreature(null);
            setStatus(Creature.Status.DEAD);
            controller.DeadDisplay(this);//播放死亡动画
            System.out.println(this  + "  已死亡");

            //游戏结束判定
            if (getCamp() == 1)
                controller.camp1.DecNumber();
            else if (getCamp() == 2)
                controller.camp2.DecNumber();
            if (controller.camp1.getNumber() == 0)
                controller.GameOverDisplay(1);
            else if (controller.camp2.getNumber() == 0)
                controller.GameOverDisplay(2);
        }
    }

    public boolean isNearLeader() {
        if(this.toString().equals("爷爷")|| this.toString().equals("蛇精"))
            return false;
        for (int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++)
                if ((i >= 0) && (j >= 0) && (i < field.getRow()) && (j < field.getCol())
                        && !field.getPositions()[i][j].isEmpty()) {
                    if (camp == 1 && field.getPositions()[i][j].getCreature().toString().equals("爷爷"))
                        return true;
                    if (camp == 2 && field.getPositions()[i][j].getCreature().toString().equals("蛇精"))
                        return true;
                }
        return false;
    }
}
