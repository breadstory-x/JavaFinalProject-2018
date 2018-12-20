package data;

import javafx.scene.image.Image;
import sample.Controller;

public class Grandpa extends Creature implements Leader
{
    public Grandpa(BattleField field, Controller controller){
        super(field,controller);
        Image image = new Image("/sample/image/爷爷.png");
        super.getView().setImage(image);
        super.setCamp(1);
        super.setDamageValue(20);
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
    @Override
    public void OnExecute()
    {
        synchronized (field) {
            for (int i = x - 1; i <= x + 1; i++)
                for (int j = y - 1; j <= y + 1; j++) {
                    if ((i >= 0) && (j >= 0) && (i < field.getRow()) && (j < field.getCol())
                            && !field.getPositions()[i][j].isEmpty()
                            && (field.getPositions()[i][j].getCreature().getCamp() == camp)
                            /*&& (field.getPositions()[i][j].getCreature().getBattleStatus()!= BattleStatus.OTHER)*/) {
                        synchronized (field.getPositions()[i][j].getCreature()) {
                            if (field.getPositions()[i][j].getCreature().HP > 0) {
                                cure(field.getPositions()[i][j].getCreature());
                            }
                        }
                    }
                }
        }
    }

    public void cure(Creature creature){
        creature.setBattleStatus(BattleStatus.BE_CURED);
        int cure_point = (int)(damageValue*(0.8+Math.random()*0.4));//伤害值上下浮动20%，即0.8——1.2倍伤害
        System.out.println("爷爷治疗"+creature+" 恢复"+cure_point+"点生命");
        creature.HP+=cure_point;
        if(creature.HP>creature.maxHP)
            creature.HP = creature.maxHP;

        controller.beCuredDisplay(creature);//播放治疗动画
        creature.setBattleStatus(BattleStatus.OTHER);
    }
}
