import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class TrainContext1{

    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();

    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();

    public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();

    public static String line;

    public static HashMap<String, BigInteger> result = new HashMap<String, BigInteger>();

    public TrainContext1(Map<String, BigInteger> dictionary, ArrayList<ArrayList<String>> homophonedb, HashMap<String, HashMap<String, Integer>> likelihood, String line) throws NumberFormatException, IOException{
        this.dictionary = dictionary;
        this.homophonedb = homophonedb;
        this.likelihood = likelihood;
        this.line = line;

        //BufferedReader br = new BufferedReader(new FileReader("../data/test.txt"));
        //String line = null;

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
                    //System.out.println("The strength of " + word + " in this context is : " + bigstr);
                    result.put(word, bigstr);
                }

            } else {
                continue;
            }
        }
    }

    public HashMap<String, BigInteger> getResult(){
        return this.result;
    }


    public int getContextStrength(String word, ArrayList<String> context){
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

    public ArrayList<String> getAmbiguousWords(String str){

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

    public int getLikelihood(String str1, String str2){
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

    public boolean isAmbiguous(String str){
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
