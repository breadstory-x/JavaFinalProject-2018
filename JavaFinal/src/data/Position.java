package data;

public class Position {
    private Creature creature;
    private int x;
    private int y;
    Position(int x, int y)//构造方法将该方块置空
    {
        this.x = x;
        this.y = y;
        creature = null;
    }
    public void setCreature(Creature creature) { this.creature = creature; }
    public Creature getCreature(){ return creature; }
    public boolean isEmpty(){ return creature==null; }
}
