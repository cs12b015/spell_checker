import java.io.*;
import java.util.*;
import java.math.*;

public class Test {
    public static void main(String[] args){
        ArrayList<ColWord> cw1 = new ArrayList<ColWord>();

        ColWord a1 = new ColWord("a1");
        ColWord a2 = new ColWord("a2");
        ColWord a3 = new ColWord("a3");

        Collocation c1 = new Collocation(cw1);
        Collocation c2 = new Collocation(cw1, 1);

        HashMap<Collocation, Integer> col_test = new HashMap<Collocation, Integer>();
        col_test.put(c1, 10);
        col_test.put(c2, 20);

        System.out.println(col_test.get(c1));
        System.out.println(col_test.get(c2));
        System.out.println(col_test.size());
    }
}
