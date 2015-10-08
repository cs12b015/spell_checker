import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class TrainPosMap{

    public static HashMap<String, String> posmap = new HashMap<String, String>();

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("../data/w5c.txt"));
        String line = null;

        while ((line = br.readLine()) != null){
            String arr[] = line.split("\t");

            if (arr.length == 11){
            for (int i = 1; i < 6; i++){
                posmap.put(arr[i].toUpperCase(), arr[i+5].toUpperCase());
            }
            }else {
                System.out.println(arr[0]);
                continue;
            }
        }

        PrintStream console = System.out;
        PrintStream out = new PrintStream(new FileOutputStream("../data/posmap.txt"));
        
        System.setOut(out);
        
       /* System.out.println(likelihood);*/
        for (String s:posmap.keySet()){
        	System.out.println(s + " " + posmap.get(s));
        }
        
        System.setOut(console);
    }

}
