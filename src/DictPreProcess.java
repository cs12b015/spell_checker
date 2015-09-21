import java.io.*;
import java.util.*;
import java.math.*;

public class DictPreProcess {

    private static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    private static Map<String, ArrayList<String>> dictPreProcess = new HashMap<String, ArrayList<String>>();

    public static void main(String[] args) throws NumberFormatException, IOException {

        BufferedReader br = new BufferedReader(new FileReader("test_db.csv"));
        String line =  null;

        /*
           while((line=br.readLine())!=null){
           String arr[] = line.split("\t");
           BigInteger abcd = new BigInteger(arr[1]);
           dictionary.put(arr[0],abcd);
           }
           */

        dictionary.put(args[0], new BigInteger("1234567"));

        for (String key : dictionary.keySet()){
            ArrayList<String> trigrams = new ArrayList<String>();
            trigrams = get_trigrams(key);
            for (String tri : trigrams){
                if (!dictPreProcess.containsKey(tri)){

                    SpellCheck sp = new SpellCheck(dictionary);
                    sp.kedit(tri.toUpperCase(),2);
                    sp.setCorrectWords();
                    sp.sortCorrectWords();

                    dictPreProcess.put(tri, sp.getCorrectWords());

                    System.out.println(dictPreProcess);
                }
            }
        }
    }

    private static ArrayList<String> get_trigrams(String key){
        ArrayList<String> temp_tri = new ArrayList<String>();
        for (int i = 0; i < key.length()-2; i++){
            temp_tri.add(key.substring(i, i+3));
        }
        return temp_tri;
    }

}