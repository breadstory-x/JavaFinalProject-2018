package njuxwj.data;

@AuthorInfo(name = "xwj")
public class Formation {
    public static void ChangShe(BattleField field,Camp<? extends Creature> camp, int start)
    {
        if(camp.getLeader().toString().equals("爷爷"))
        {
            field.setCreature(camp.getLeader(), 0,3);
            for(int i = start;i<start+camp.getSoldiers().size();i++)
                field.setCreature(camp.getSoldiers().get(i-start),i,3);
        }
        else
        {
            field.setCreature(camp.getLeader(), start,15);
            for(int i = start;i<start+camp.getSoldiers().size();i++)
                field.setCreature(camp.getSoldiers().get(i-start),i,15);
        }
    }
    public static void YanXing(BattleField field,Camp<?> camp, int start_x, int start_y)
    {
        if(camp.getLeader().toString().equals("爷爷"))
        {
            field.setCreature(camp.getLeader(), start_x,start_y+3);
            for(int i = 0;i<camp.getSoldiers().size();i++)
            {
                field.setCreature(camp.getSoldiers().get(i),start_x,start_y);
                start_x--;
                start_y++;
            }
        }
        else
        {
            field.setCreature(camp.getLeader(), start_x,start_y+3);
            field.setCreature(camp.getSoldiers().get(0),start_x,start_y);
            for(int i = 1;i<camp.getSoldiers().size();i++)
            {
                start_x--;
                start_y++;
                field.setCreature(camp.getSoldiers().get(i),start_x,start_y);
            }
        }
    }
}
