package njuxwj.data;

import java.util.ArrayList;
import java.util.Arrays;

@AuthorInfo(name = "xwj")
public class Camp<T extends Creature> {
    private ArrayList<Creature>soldiers;
    private T leader;
    private int number;
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
    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }
    public void DecNumber(){number--;}
}