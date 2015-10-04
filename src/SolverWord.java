import java.io.*;
import java.util.*;
import java.math.*;

import org.apache.commons.codec.EncoderException;

public class SolverWord {


    public static HashMap<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static Map<String, ArrayList<String>> trigrams = new HashMap<String, ArrayList<String>>();
    public static void main(String[] args) throws NumberFormatException, IOException, EncoderException {

        BufferedReader br = new BufferedReader(new FileReader("data/test_db.csv"));
        String line =  null;
        //String word = args[0].toUpperCase();

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }

        br = new BufferedReader(new FileReader("data/trigrams_db.csv"));
        line=null;
        while((line=br.readLine())!=null){
        	String tristring = line.substring(0,3);
        	
        	String arraystring= line.substring(4);
        	arraystring=arraystring.substring(1, arraystring.length()-1);
        	String[] keyValuePairs = arraystring.split(", "); 
        	ArrayList<String> myarray= new ArrayList<String>();
        	for(String pair : keyValuePairs)                
    		{
        		myarray.add(pair);
    		}
        	trigrams.put(tristring, myarray);	
        }

        br = new BufferedReader(new FileReader("data/spellchecktest.txt"));
        line=null;
        while((line=br.readLine())!=null){
        	
        	SpellCheck1 sp1 = new SpellCheck1(dictionary, trigrams, line);
            sp1.sortCorrectWordsBySoundex();
            sp1.printOut();
        }   
        
    }

}
