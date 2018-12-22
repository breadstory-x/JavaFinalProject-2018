package njuxwj;
import njuxwj.data.*;
import njuxwj.sample.Controller;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;


public class MyTest {
    public BattleField field = new BattleField(12,18);
    Grandpa grandpa = new Grandpa(field, null);
    Snake snake = new Snake(field, null);
    Scorpion scorpion = new Scorpion(field, null);
    Monster[] monsters;
    public MyTest(){
        monsters = new Monster[7];
        for(int i = 0;i<7;i++){
            monsters[i] = new Monster(field,null);
        }
    }

    @Test
    public void MonsterNumTest()throws Exception{
        for(int i = 0;i<monsters.length;i++)
        {
            assertEquals(monsters[i].toString(),"小喽啰"+String.valueOf(i));
        }
    }
}
