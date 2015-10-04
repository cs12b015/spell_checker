import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;
/*THIS FILE WILL DO THE PRE PROCESSING AND SAVE IT INTO THE DATABASE IN THIS CASE LIKELIHOOD.TXT*/

public class junkfile {
	
	public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();
	
	public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader("data/homophonedb.txt"));
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
        
        br = new BufferedReader(new FileReader("data/w3_.txt"));
		line= null;
        while((line = br.readLine()) != null) {
            String arr[] = line.split("\t");

            if (isAmbiguous(arr[1])){
                //System.out.println("Found an ambiguous word: " + arr[1]);
                addToLikelihood(arr[1], arr[2], Integer.parseInt(arr[0]));
                addToLikelihood(arr[1], arr[3], Integer.parseInt(arr[0]));
            }

            if (isAmbiguous(arr[2])){
                //System.out.println("Found an ambiguous word: " + arr[1]);
                addToLikelihood(arr[2], arr[1], Integer.parseInt(arr[0]));
                addToLikelihood(arr[2], arr[3], Integer.parseInt(arr[0]));
            }

            if (isAmbiguous(arr[3])){
                //System.out.println("Found an ambiguous word: " + arr[1]);
                addToLikelihood(arr[3], arr[2], Integer.parseInt(arr[0]));
                addToLikelihood(arr[3], arr[1], Integer.parseInt(arr[0]));
            }
        }

       /* System.out.println(likelihood);*/
        
        PrintStream console = System.out;    
        PrintStream out = new PrintStream(new FileOutputStream("data/likelihood11.txt"));
        
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
