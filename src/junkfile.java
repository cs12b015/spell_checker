import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;
/*THIS FILE WILL DO THE PRE PROCESSING AND SAVE IT INTO THE DATABASE IN THIS CASE LIKELIHOOD.TXT*/

public class junkfile {
	
	public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();
	
	public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();

    public static HashMap<String, String> posmap = new HashMap<String, String>();
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader("../data/homophonedb.txt"));
		String line= null;
        while((line = br.readLine()) != null){
            String arr[] = line.split(",");
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < arr.length; i++){
                temp.add(arr[i].trim());
                //System.out.println(arr[i].trim());
            }

            homophonedb.add(temp);
        }
        
        br = new BufferedReader(new FileReader("../data/w5_.txt"));
		line= null;
        while((line = br.readLine()) != null) {
            String arr[] = line.split("\t");

            for (int i = 1; i < arr.length; i++){
                for (int j = 1; j < arr.length; j++){
                    if (arr[i] != arr[j]){
                        addToLikelihood(arr[i], arr[j], Integer.parseInt(arr[0]));
                    }
                }
            }
        }

       /* System.out.println(likelihood);*/
        
        PrintStream console = System.out;    
        PrintStream out = new PrintStream(new FileOutputStream("../data/likelihood1.txt"));
        
        System.setOut(out);
        
       /* System.out.println(likelihood);*/
        for (String s:likelihood.keySet()){
        	System.out.println(s);
        	System.out.println(likelihood.get(s));
        }
        
        System.setOut(console);
        
	}
	
	
	public static void addToLikelihood(String str1, String str2, int freq){
        if (likelihood.containsKey(str1)){
            //The ambiguous word is already registered
            //Retrieve the arraylist of all the hashmaps associated with this ambiguous word
            HashMap<String, Integer> temp = likelihood.get(str1);

            if (temp.containsKey(str2)){
                //This context is already registered for this ambiguous word
                //Add the pervious frequency to the current one and update it
                temp.put(str2, temp.get(str2)+freq );
            } else {
                //This is a new context to the ambiguous word
                //Add the context along with the frequency value to the hashmap
                temp.put(str2, freq);
            }
        } else {
            //The ambiguous word is not seen before
            //Initialize a hashmap with the context word along with its frequency
            HashMap<String, Integer> temp = new HashMap<String, Integer>();
            temp.put(str2, freq);
            likelihood.put(str1, temp);
        }
    }



    public static boolean isAmbiguous(String str){
        Iterator<ArrayList<String>> it = homophonedb.iterator();

        while(it.hasNext()){
            ArrayList<String> amb_pair = it.next();
            if (amb_pair.contains(str)){
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
}
