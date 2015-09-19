import java.io.*;
import java.util.*;
import java.math.*;

public class Solver {

    public static void main(String[] args) throws NumberFormatException, IOException {

        Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
        BufferedReader br = new BufferedReader(new FileReader("test_db.csv"));
        String line =  null;
        String word = args[0].toUpperCase();

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }

        SpellCheck sp = new SpellCheck(dictionary);

        if (sp.isCorrect(word)){
            System.out.println("yes it is a correct word");
        }else {
            sp.kedit(word,2);
            sp.setCorrectWords();
            sp.sortCorrectWords();
            sp.printOut();
        }
    }

}
