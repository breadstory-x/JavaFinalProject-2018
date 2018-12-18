package data;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.*;



abstract public class Creature implements Runnable{
    private int x;
    private int y;
    final private BattleField field;
    final private Controller controller;
    private ImageView view;
    private Status status;
    private int camp;
    public int HP;
    public enum Status{ALIVE, DEAD}

    Creature(BattleField field, Controller controller)
    {
        x = -1;
        y = -1;
        this.field = field;
        this.controller=controller;
        view = new ImageView();
        view.setFitWidth(80);
        view.setFitHeight(75);
        status = Status.ALIVE;
        HP = 100;

    }
    public void set(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    public void setStatus(Status x){status = x;}
    public Status getStatus(){return status;}
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getCamp(){return camp;}
    public void setCamp(int camp){this.camp = camp;}
    public ImageView getView(){return view;}

    public void run ()
    {//判断周围是否有敌人，有的话攻击，没有则移动
        while(status == Status.ALIVE) {
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean state =  true;
            synchronized (field) {
                for (int i = x - 1; i <= x + 1 && state; i++)
                    for (int j = y - 1; j <= y + 1 && state; j++)
                        if ((i >= 0) && (j >= 0) && (i < field.getRow()) && (j < field.getCol())
                                && !field.getPositions()[i][j].isEmpty()
                                && (field.getPositions()[i][j].getCreature().getCamp() != camp)) {
                            //synchronized (field.getPositions()[i][j].getCreature()) {
                            if (field.getPositions()[i][j].getCreature().HP>0) {
                                System.out.println(this + "攻击" + field.getPositions()[i][j].getCreature());
                                attack(field.getPositions()[i][j].getCreature());
                                state = false;
                            }
                            //}
                        }
            }
            //if(!state)
            //    continue;

            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(x<4 )
                moveTo(x+1,y);
            else if(x>8)
                moveTo(x-1,y);
            else if(y<4)
                moveTo(x,y+1);
            else if(y>10)
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
        System.out.println(this+" 已退出");
    }
    public void moveTo(int x, int y)
    {
        synchronized (controller) {
            if(x >= field.getRow() || y >= field.getCol() || x<0||y<0||!field.getPositions()[x][y].isEmpty())
                return;
            field.getPositions()[this.x][this.y].setCreature(null);
            field.getPositions()[x][y].setCreature(this);
            this.set(x, y);
            controller.moveToDisplay(this, x, y);
        }
    }
    public synchronized void attack(Creature creature)
    {
        creature.HP-=50;
        controller.beAttackedDisplay(creature);//播放受击动画
        if(creature.HP <= 0)
        {
            controller.DeadDisplay(creature);//播放死亡动画
            creature.setStatus(Creature.Status.DEAD);
            System.out.println(creature+"HP="+creature.HP+"  已死亡");
            field.getPositions()[creature.getX()][creature.getY()].setCreature(null);
        }
        else
        {
            System.out.println(creature+"HP="+creature.HP);
        }

        System.out.println();
    }
}
