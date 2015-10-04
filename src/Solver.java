import java.io.*;
import java.util.*;
import java.math.*;

public class Solver {


    public static HashMap<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();
    public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();

    public static void main(String[] args) throws NumberFormatException, IOException {

        BufferedReader br = new BufferedReader(new FileReader("../data/test_db.csv"));
        String line =  null;
        //String word = args[0].toUpperCase();

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }

        br = new BufferedReader(new FileReader("../data/homophonedb.txt"));

        while((line = br.readLine()) != null){
            String arr[] = line.split(",");
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < arr.length; i++){
                temp.add(arr[i].trim());
                //System.out.println(arr[i].trim());
            }

            homophonedb.add(temp);
        }
        //System.out.println(System.currentTimeMillis() - init_time);
        //System.out.print(homophonedb);


        br = new BufferedReader(new FileReader("../data/likelihood.txt"));
        line =  null;
        int counter=0;
        String temp="";

        while((line=br.readLine())!=null){
            if(counter % 2 == 0){
                temp=line;
                /*System.out.println(line);*/
            }
            else{


                line = line.substring(1, line.length()-1);
                String[] keyValuePairs = line.split(",");  

                HashMap<String, Integer> tempvalue = new HashMap<String, Integer> ();               

                for(String pair : keyValuePairs)                
                {
                    String[] entry = pair.split("=");         
                    tempvalue.put(entry[0].trim(), Integer.parseInt(entry[1].trim()));
                }

                likelihood.put(temp, tempvalue);

            }
            counter++;
        }

        //SpellCheck sp = new SpellCheck(dictionary);

        TrainContext1 tc1 = new TrainContext1(dictionary, homophonedb, likelihood, "peace of cake");
        System.out.println(tc1.getResult());
        /*
           if (sp.isCorrect(word)){
           System.out.println("yes it is a correct word");
           }else {
           sp.kedit(word,2);
           sp.setCorrectWords();
           sp.sortCorrectWords();
           sp.printOut();
           }
           */
        //        sp.kedit(word,2);
        //        sp.setCorrectWords();
        //        sp.sortCorrectWords();
        //        sp.printOut();
    }

}
