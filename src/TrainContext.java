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
        
        BufferedReader br = new BufferedReader(new FileReader("test_db.csv"));
        String line =  null;

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }

        br = new BufferedReader(new FileReader("homophonedb.txt"));

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

        br = new BufferedReader(new FileReader("w3_.txt"));

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

        //System.out.println(likelihood);
        
        long init_time = System.currentTimeMillis();

        System.out.println(act_time-init_time);

        br = new BufferedReader(new FileReader("test.txt"));

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
