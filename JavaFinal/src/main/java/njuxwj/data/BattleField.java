package njuxwj.data;
//import sample.*;

@AuthorInfo(name = "xwj")
public class BattleField {
    private Position[][] positions;
    private int row;
    private int col;

    public BattleField(int row, int col) {
        this.row = row;
        this.col = col;
        positions = new Position[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                Position temp = new Position(i,j);
                positions[i][j] = temp;
            }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position[][] getPositions() {
        return positions;
    }

    public synchronized void setCreature(Creature creature, int x, int y) {
        //如果原位置单位与将要移动的单位相同，则将该位置置为空
        if (creature.getX() != -1 && positions[creature.getX()][creature.getY()].getCreature() == creature)
        {
            positions[creature.getX()][creature.getY()].setCreature(null);
        }
        positions[x][y].setCreature(creature);
        creature.set(x, y);
        creature.getView().setX(y*80);
        creature.getView().setY(x*75+5);
        creature.getG_rect().setX(y*80);
        creature.getG_rect().setY(x*75);
        creature.getR_rect().setX(y*80);
        creature.getR_rect().setY(x*75);
    }
}

