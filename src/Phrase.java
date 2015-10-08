import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class Phrase{

    private ArrayList<String> phrase = new ArrayList<String>();

    private static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();

    private static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();

    private static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();

    private int index;

    private ArrayList<String> context = new ArrayList<String>();

    public Phrase(ArrayList<ArrayList<String>> homophonedb, Map<String, BigInteger> dictionary, HashMap<String, HashMap<String, Integer>> likelihood,String phrase){
        this.dictionary = dictionary;
        this.homophonedb = homophonedb;
        this.likelihood = likelihood;
        String arr[] = phrase.split(" ");

        for (int i = 0; i < arr.length; i++){
            this.phrase.add(arr[i].toUpperCase());
        }

        this.index = getWrongWordIndex();
        this.context = getContext();
    }


    public HashMap<String, Integer> solve(Map<String, ArrayList<String>> trigrams) throws IOException{
        ArrayList<String> candidates = getCandidates(trigrams);
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        for (int i = 0; i < candidates.size(); i++){
            result.put(candidates.get(i), getContextStrength(candidates.get(i), context));
        }

        System.out.println(result);
        return result;
    }


    public ArrayList<String> getCandidates(Map<String, ArrayList<String>> trigrams) throws IOException{
        //method to generate all possible candidates
        ArrayList<String> candidates = new ArrayList<String>();

        if (!dictionary.containsKey(phrase.get(index))){
            SpellCheck1 sp1 = new SpellCheck1(dictionary, trigrams, phrase.get(index));
            sp1.sortCorrectWords();

            ArrayList<String> result = sp1.getResult();

            for (int i = 0; i < 3; i++){
                if (isAmbiguous(result.get(i))){

                    ArrayList<String> amb_words = getAmbiguousWords(result.get(i));
                    for (int j = 0; j < amb_words.size(); j++){
                        candidates.add(amb_words.get(j));
                    }
                }else {
                    candidates.add(result.get(i));
                }
            }
        }else {
            if (isAmbiguous(phrase.get(index))){
                ArrayList<String> amb_words = getAmbiguousWords(phrase.get(index));
                for (int i = 0; i < amb_words.size(); i++){
                    candidates.add(amb_words.get(i));
                }
            }
        }

        return candidates;
    }


    public ArrayList<String> getContext(){

        ArrayList<String> context = new ArrayList<String>();

        for (int i = 0; i < phrase.size(); i++){
            if (i != index){
                context.add(phrase.get(i));        
            }
        }

        return context;
    }


    public int getWrongWordIndex(){
        //get the wrong word from the phrase

        BigInteger freq = BigInteger.valueOf(1000000);
        int k = 0;
        for (int i = 0; i < phrase.size(); i++){
            String word = phrase.get(i);

            if (!dictionary.containsKey(word)){
                return i;
            }
            if (isAmbiguous(word)){
                return i;        
            }
            BigInteger val = dictionary.get(word);
            if (freq.compareTo(val) > 0){
                freq = val;
                k = i;
            }
        }
        return k;
    }


    public String getWrongWord(){
        return phrase.get(index);
    }


    private ArrayList<String> getAmbiguousWords(String str){

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


    private boolean isAmbiguous(String str){
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

    public void printOut(){
        System.out.println(phrase);
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

    public int getLikelihood(String str1, String str2){
        if (likelihood.containsKey(str1)){
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
        }else {
            return 0;
        }
    }


}
