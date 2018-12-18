package data;

import java.util.ArrayList;
import java.util.Arrays;

public class Camp<T extends Creature> {
    private ArrayList<Creature>soldiers;
    private T leader;
    public Camp(T leader)
    {
        this.leader = leader;
        soldiers = new ArrayList<>();
    }
    public Camp(T leader, Creature[] creatures/*,int num*/)
    {
        this.leader = leader;
        soldiers = new ArrayList<>();
        soldiers.addAll(Arrays.asList(creatures));
        //for(int i = 0;i<num;i++)
        //    soldiers.add(unit[i]);
    }
    public void addCreatures(Creature creatures)
    {
        soldiers.add(creatures);
    }
    public ArrayList<Creature> getSoldiers()
    {
        return soldiers;
    }
    public T getLeader()
    {
        return leader;
    }

}