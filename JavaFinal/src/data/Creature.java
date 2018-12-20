package data;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.*;

import static data.Creature.BattleStatus.MOVE;
import static data.Creature.BattleStatus.OTHER;

interface FundamentalBattle
{
    void moveTo(int x, int y);
    void attack(Creature creature);
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


    public enum Status{ALIVE, DEAD}
    public enum BattleStatus{BE_ATTECKED, BE_CURED, MOVE, OTHER}

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
    public Status getStatus(){return status;}
    public void setBattleStatus(BattleStatus battleStatus) {
        this.battleStatus = battleStatus;
    }
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
    public int getDamageValue() {
        return damageValue;
    }
    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    public void run ()
    {
        while(status != Status.DEAD) {
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(status == Status.DEAD)
                break;
            OnMoveTo();
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OnExecute();
        }
        System.out.println(this+" 已退出");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //移动回合
    public void OnMoveTo()
    {
        if(x<5 )
            moveTo(x+1,y);
        else if(x>8)
            moveTo(x-1,y);
        else if(y<4)
            moveTo(x,y+1);
        else if(y>8)
            moveTo(x,y-1);
        else {
            double rdm = Math.random();
            if (rdm < 0.25)
                moveTo(x - 1, y);
            else if (rdm < 0.5)
                moveTo(x, y + 1);
            else if (rdm < 0.75)
                moveTo(x + 1, y);
            else
                moveTo(x, y - 1);
        }
    }

    //操作回合
    public void OnExecute()
    {
        boolean state =  true;
        synchronized (field) {
            for (int i = x - 1; i <= x + 1 && state; i++)
                for (int j = y - 1; j <= y + 1 && state; j++) {
                        if ((i >= 0) && (j >= 0) && (i < field.getRow()) && (j < field.getCol())
                                && !field.getPositions()[i][j].isEmpty()
                                && (field.getPositions()[i][j].getCreature().getCamp() != camp)
                                /*&& (field.getPositions()[i][j].getCreature().getBattleStatus()== BattleStatus.OTHER)*/) {
                            synchronized (field.getPositions()[i][j].getCreature()) {
                                if (field.getPositions()[i][j].getCreature().HP > 0) {
                                    attack(field.getPositions()[i][j].getCreature());
                                    state = false;
                                }
                            }
                        }
                }
        }
    }

    public void moveTo(int x, int y)
    {
        battleStatus = MOVE;
        synchronized (controller) {
            if(x >= field.getRow() || y >= field.getCol() || x<0||y<0||!field.getPositions()[x][y].isEmpty())
                return;
            field.getPositions()[this.x][this.y].setCreature(null);
            field.getPositions()[x][y].setCreature(this);
            this.set(x, y);
            controller.moveToDisplay(this, x, y);
        }
        battleStatus = OTHER;
    }
    public synchronized void attack(Creature creature)
    {
        creature.setBattleStatus(BattleStatus.BE_ATTECKED);
        int hurt_point = (int)(damageValue*(0.8+Math.random()*0.4));//伤害值上下浮动20%，即0.8——1.2倍伤害
        System.out.print(this + "攻击" + creature);
        System.out.print(" 造成"+hurt_point+"点伤害   ");
        creature.HP-=hurt_point;
        if(creature.HP<0)
            creature.HP = 0;

        controller.beAttackedDisplay(creature);//播放受击动画
        if(creature.HP == 0)
        {
            field.getPositions()[creature.getX()][creature.getY()].setCreature(null);
            creature.setStatus(Creature.Status.DEAD);
            controller.DeadDisplay(creature);//播放死亡动画
            System.out.println(creature+"HP="+creature.HP+"  已死亡");

            //游戏结束判定
            if(creature.getCamp() == 1)
                controller.camp1.DecNumber();
            else if(creature.getCamp() == 2)
                controller.camp2.DecNumber();
            if(controller.camp1.getNumber()==0)
                controller.GameOverDisplay(1);
            else if(controller.camp2.getNumber()==0)
                controller.GameOverDisplay(2);
        }
        else
        {
            System.out.println(creature+"HP="+creature.HP);
        }
        creature.setBattleStatus(BattleStatus.OTHER);
        //System.out.println();
    }

}
