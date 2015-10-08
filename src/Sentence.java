import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class Sentence{

    private ArrayList<String> sentence = new ArrayList<String>();

    private static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();

    private static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();

    private static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();

    private static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();

    private static Map<String, String> posmap = new HashMap<String, String>();

    private int index;

    private ArrayList<String> context = new ArrayList<String>();

    private Collocation left_col;

    private Collocation right_col;

    public Sentence(ArrayList<ArrayList<String>> homophonedb, Map<String, BigInteger> dictionary, HashMap<String, HashMap<String, Integer>> likelihood, Map<String, String> posmap, HashMap<String, HashMap<Collocation, Integer>> collocations,String sentence){
        this.dictionary = dictionary;
        this.homophonedb = homophonedb;
        this.likelihood = likelihood;
        this.posmap = posmap;
        this.collocations = collocations;

        String arr[] = sentence.split(" ");

        for (int i = 0; i < arr.length; i++){
            this.sentence.add(arr[i].toUpperCase());
        }

        this.index = getWrongWordIndex();
        this.context = getContext();

        this.left_col = getCollocation(0);
        this.right_col = getCollocation(1);

        /*
        if (left_col != null){
            left_col.printOut();
        }
        if (right_col != null){
            right_col.printOut();
        }
        */
    }


    private BigInteger getCollocationStrength(String word, Collocation left_col, Collocation right_col){
        //Return the strength of this word in the presence of these collocations
        HashMap<Collocation, Integer> word_col = collocations.get(word);
        //System.out.println(word);
        //System.out.println(word_col.size());
        BigInteger strength = BigInteger.valueOf(1);

        try {
            Iterator it = word_col.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Collocation temp_col = (Collocation)pair.getKey();
                if (left_col != null){
                    if (temp_col.hasConflict(left_col)){
                        strength = strength.multiply(BigInteger.valueOf((Integer)pair.getValue()));
                    }
                }
                if (right_col != null){
                    if (temp_col.hasConflict(right_col)){
                        strength = strength.multiply(BigInteger.valueOf((Integer)pair.getValue()));
                    }
                }
            }
        }catch(Exception e){
            return strength;
        }
        return strength;
    }


    private Collocation getCollocation(int side){
        if (side > 0){
            //Right side collocation will be returned
            ArrayList<ColWord> cw = new ArrayList<ColWord>();
            if (index < sentence.size()-1){
                for (int i = index; i < sentence.size(); i++){
                    if (posmap.containsKey(sentence.get(i).trim())){
                        ColWord temp = new ColWord(posmap.get(sentence.get(i)).trim());
                        cw.add(temp);
                    }else {
                        ColWord temp = new ColWord("NN1");
                        cw.add(temp);
                    }
                }
                Collocation right_col = new Collocation(cw, 1);
                return right_col;
            }else {
                return null;
            }
        }else {
            //Left side collocation will be returned
            ArrayList<ColWord> cw = new ArrayList<ColWord>();
            if (index > 0){
                for (int i = 0; i < index+1; i++){
                    if (posmap.containsKey(sentence.get(i).trim())){
                        ColWord temp = new ColWord(posmap.get(sentence.get(i)).trim());
                        cw.add(temp);
                    }else {
                        ColWord temp = new ColWord("NN1");
                        cw.add(temp);
                    }
                }
                Collocation left_col = new Collocation(cw);
                return left_col;
            }else {
                return null;
            }
        }
    }


    public HashMap<String, BigInteger> solve(Map<String, ArrayList<String>> trigrams) throws IOException{
        ArrayList<String> candidates = getCandidates(trigrams);
        HashMap<String, BigInteger> context_result = new HashMap<String, BigInteger>();
        HashMap<String, BigInteger> collocation_result = new HashMap<String, BigInteger>();
        HashMap<String, BigInteger> final_result = new HashMap<String, BigInteger>();

        for (int i = 0; i < candidates.size(); i++){
            BigInteger col_str = getCollocationStrength(candidates.get(i), left_col, right_col);
            BigInteger con_str = getContextStrength(candidates.get(i), context);
            context_result.put(candidates.get(i), con_str);
            collocation_result.put(candidates.get(i), col_str);
            final_result.put(candidates.get(i), con_str.multiply(col_str));
        }

        System.out.println(collocation_result);
        System.out.println(context_result);
        System.out.println(final_result);
        return final_result;
    }


    public ArrayList<String> getCandidates(Map<String, ArrayList<String>> trigrams) throws IOException{
        //method to generate all possible candidates
        ArrayList<String> candidates = new ArrayList<String>();

        if (!dictionary.containsKey(sentence.get(index))){
            SpellCheck1 sp1 = new SpellCheck1(dictionary, trigrams, sentence.get(index));
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
            if (isAmbiguous(sentence.get(index))){
                ArrayList<String> amb_words = getAmbiguousWords(sentence.get(index));
                for (int i = 0; i < amb_words.size(); i++){
                    candidates.add(amb_words.get(i));
                }
            }
        }

        return candidates;
    }


    public ArrayList<String> getContext(){

        ArrayList<String> context = new ArrayList<String>();

        for (int i = 0; i < sentence.size(); i++){
            if (i != index){
                context.add(sentence.get(i));        
            }
        }

        return context;
    }


    public int getWrongWordIndex(){
        //get the wrong word from the sentence

        BigInteger freq = BigInteger.valueOf(1000000);
        int k = 0;
        for (int i = 0; i < sentence.size(); i++){
            String word = sentence.get(i);

            if (!dictionary.containsKey(word)){
                return i;
            }
        }
        for (int i = 0; i < sentence.size(); i++){
            String word = sentence.get(i);
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
        return sentence.get(index);
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
        System.out.println(sentence);
    }


    public BigInteger getContextStrength(String word, ArrayList<String> context){
        BigInteger strength = BigInteger.valueOf(1);

        for (int i = 0; i < context.size(); i++){
            //System.out.println(context.get(i));
            if (context.get(i).trim() == word.trim()){
                continue;
            } else {
                strength = strength.multiply(BigInteger.valueOf(getLikelihood(word, context.get(i))));       
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
