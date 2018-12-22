package data;

import javafx.scene.image.Image;
import sample.Controller;

import java.util.ArrayList;

public class Grandpa extends Creature //implements Leader
{
    public Grandpa(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/爷爷.png");
        getView().setImage(image);
        setCamp(1);
        damageValue = 100;
        deadProbability = 0.3;
        if(controller.isOnRecord)
        {
            //System.out.println(this+":"+controller.history.get(this.toString()));
            history = new ArrayList<>(controller.history.get(this.toString()));
        }
    }

    @Override
    public String toString(){ return "爷爷"; }

    //@Override
    /*public void run() {
        while (status != Status.DEAD) {
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (status == Status.DEAD)
                break;
            OnMoveTo();
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OnExecute();
        }

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(this+" 已退出");
    }*/
 /*   @Override
    public void OnExecute()
    {
        boolean state =  true;
        cure(this);
        synchronized (field) {
            for (int i = x - 1; i <= x + 1 && state; i++)
                for (int j = y - 1; j <= y + 1 && state; j++) {
                    if ((i >= 0) && (j >= 0) && (i < field.getRow()) && (j < field.getCol())
                            && !field.getPositions()[i][j].isEmpty()
                            && (field.getPositions()[i][j].getCreature().getCamp() == camp)
                            && (field.getPositions()[i][j].getCreature().HP < field.getPositions()[i][j].getCreature().maxHP)
                            && !(i == this.getX() && j == this.getY())) {
                        synchronized (field.getPositions()[i][j].getCreature()) {
                            if (field.getPositions()[i][j].getCreature().HP > 0) {
                                history.add("C");
                                history.add(String.valueOf(i));
                                history.add(String.valueOf(j));
                                cure(field.getPositions()[i][j].getCreature());
                                state =  false;
                            }
                        }
                    }
                }
        }
    }

    public void OnRecordExecute()
    {
        int x = Integer.parseInt(history.get(paveNum));
        int y = Integer.parseInt(history.get(paveNum + 1));
        paveNum += 2;
        cure(this);
        cure(field.getPositions()[x][y].getCreature());
    }

    public void cure(Creature creature){
        //creature.setBattleStatus(BattleStatus.BE_CURED);
        int cure_point = (int)(damageValue*(0.8+Math.random()*0.4));//伤害值上下浮动20%，即0.8——1.2倍伤害
        System.out.println("爷爷治疗"+creature+" 恢复"+cure_point+"点生命");
        creature.HP+=cure_point;
        if(creature.HP>creature.maxHP)
            creature.HP = creature.maxHP;

        controller.beCuredDisplay(creature);//播放治疗动画
        //creature.setBattleStatus(BattleStatus.OTHER);
    }
    */
}
