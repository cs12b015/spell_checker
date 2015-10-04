import java.io.*;
import java.util.*;
import java.math.*;

public class SolverPhrase {


    public static HashMap<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();
    public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();
    public static Map<String, ArrayList<String>> trigrams = new HashMap<String, ArrayList<String>>();
    
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
                temp.add(arr[i].trim().toUpperCase());
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
                    tempvalue.put(entry[0].trim().toUpperCase(), Integer.parseInt(entry[1].trim()));
                }
                likelihood.put(temp.toUpperCase(), tempvalue);
            }
            counter++;
        }


        br = new BufferedReader(new FileReader("../data/trigrams_db.csv"));
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

        br = new BufferedReader(new FileReader("../data/phrasechecktest.txt"));
        line=null;
        while((line=br.readLine())!=null){
        	line=line.toUpperCase();
        	String[] array = line.split(" ");
        	for(int i=0;i<array.length;i++){
        		if(!dictionary.containsKey(array[i]))
        		{
        			SpellCheck1 sp1 = new SpellCheck1(dictionary, trigrams, array[i]);
                    sp1.sortCorrectWords();
        			//sp1.sortCorrectWordsBySoundex();
        			//sp1.printOut();
        			array[i]=sp1.getResult().get(0);
        		}
        	}
        	String newline = "";
        	for(int i=0;i<array.length;i++){
        		newline=newline+" "+array[i];
        	}
        	//System.out.println(newline.trim());
        	TrainContext1 tc1 = new TrainContext1(dictionary, homophonedb, likelihood, newline.trim());
            //System.out.println(tc1.getResult());
            tc1.printOut();
        }
     }

}

