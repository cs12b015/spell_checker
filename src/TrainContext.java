import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class TrainContext {

    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();

    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();

    public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();
    
    public static void main(String[] args) throws NumberFormatException, IOException {
        long act_time = System.currentTimeMillis();

        //long init_time = System.currentTimeMillis();
        
        BufferedReader br = new BufferedReader(new FileReader("data/test_db.csv"));
        String line =  null;

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }

        br = new BufferedReader(new FileReader("data/homophonedb.txt"));

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

        
        
        br = new BufferedReader(new FileReader("data/likelihood.txt"));
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
        
        long init_time = System.currentTimeMillis();
        System.out.println(act_time-init_time);
        br = new BufferedReader(new FileReader("data/test.txt"));

        while((line = br.readLine()) != null){
            String arr[] = line.split(" ");

            for (int i = 0; i < arr.length; i++){
                //System.out.println(arr[i].trim());
                if (isAmbiguous(arr[i])){
                    //It is an ambiguous word, so check the probabilities
                    ArrayList<String> amb_words = getAmbiguousWords(arr[i]);
                    //System.out.println(amb_words);
                    
                    ArrayList<String> context = new ArrayList<String>();
                    for (int j = 0; j < arr.length; j++){
                        if (arr[j].trim() != arr[i].trim()){
                            context.add(arr[j]);
                        }
                    }

                    Iterator<String> it = amb_words.iterator();

                    while(it.hasNext()){
                        String word = it.next();
                        int str = getContextStrength(word, context);
                        
                        // Multiplied with prior. Check results.
                        
                        BigInteger prior = dictionary.get(word.toUpperCase());
                        BigInteger bigstr = BigInteger.valueOf(str);
                        bigstr.multiply(prior);
                        System.out.println("The strength of " + word + " in this context is : " + bigstr);
                    }

                } else {
                    continue;
                }
            }
        }
        System.out.println(System.currentTimeMillis() - init_time);
    }

    public static int getContextStrength(String word, ArrayList<String> context){
        int strength = 1;

        for (int i = 0; i < context.size(); i++){
            //System.out.println(context.get(i));
            if (context.get(i).trim() == word.trim()){
                continue;
            } else {
                strength = strength*getLikelihood(word, context.get(i));       
            }
        }

        return strength;
    }

    public static ArrayList<String> getAmbiguousWords(String str){

        Iterator<ArrayList<String>> it = homophonedb.iterator();

        while(it.hasNext()){
            ArrayList<String> amb_pair = it.next();
            if (amb_pair.contains(str)){
                return amb_pair;
            } else {
                continue;
            }
        }

        return new ArrayList<String>();
    }

    public static int getLikelihood(String str1, String str2){
        if (likelihood.containsKey(str1)){
            //This word is ambiguous and hence, we can proceed forward
            HashMap<String, Integer> temp = likelihood.get(str1);
            if (temp.containsKey(str2)){
                //This context word is relevant to the given word str1
                //Return the frequency of this context, given the word
                return temp.get(str2);
            } else {
                //This context word is not relevant to the given word str1
                //Return 0, which is the least possible non negative integer to show irrelevance
                return 1;
            }
        } else {
            //This is not an ambiguous word and hence doesn't have any entry in the likelihood hashmap
            return 0;
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
