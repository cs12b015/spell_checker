
import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.*;

public class SpellCheck1 {
    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static Map<String, ArrayList<String>> trigrams = new HashMap<String, ArrayList<String>>();
    public static ArrayList<String> correctWords = new ArrayList<String>();
    public static String wrong_word;

    public SpellCheck1(Map<String, BigInteger> dictionary, Map<String, ArrayList<String>> trigrams, String line) throws IOException{
        this.dictionary = dictionary;
        this.trigrams = trigrams;
        this.wrong_word = line;

        /*System.out.println(line);*/
        correctWords = generatecorrect(line.toUpperCase());
        //System.out.println(correctwords);
    }

    public ArrayList<String> getResult(){
        return this.correctWords;
    }

    public ArrayList<String> generatecorrect(String word) {
        ArrayList<String> inputtrigrams=new ArrayList<String>();
        ArrayList<String> possiblecandidates=new ArrayList<String>();
        ArrayList<String> orginalcandidates=new ArrayList<String>();
        int wordsize = word.length();

        for(int i=0;i<word.length()-2;i++){
            String temp = word.substring(i,i+3);
            ArrayList<String> temparray=new ArrayList<String>();
            temparray = trigrams.get(temp);
            for(int j=0;j<temparray.size();j++)
            {
                if(!possiblecandidates.contains(temparray.get(j))&& temparray.get(j).length() <= wordsize+1 && temparray.get(j).length() >= wordsize-1)
                    possiblecandidates.add(temparray.get(j));
            }
        }
        for(int i=0;i<possiblecandidates.size();i++){
            if(edit_distance(possiblecandidates.get(i),word,possiblecandidates.get(i).length(),word.length())<3)
            {
                orginalcandidates.add(possiblecandidates.get(i));
            }
        }
        return orginalcandidates;
    }

    public int edit_distance(String string1,String string2,int m,int n){
        if( m == 0 && n == 0 )
            return 0;
        if( m == 0 )
            return n;
        if( n == 0 )
            return m;
        int popo;
        int left = edit_distance(string1, string2, m-1, n) + 1;
        int right = edit_distance(string1, string2, m, n-1) + 1;
        if((string1.charAt(m-1)!= string2.charAt(n-1)))
        {
            popo=1;
        }
        else
        {
            popo=0;
        }
        int corner = edit_distance(string1, string2, m-1, n-1) + popo;
        return Math.min(Math.min(left, right), corner);
    }

    public void printOut(){
        System.out.println(correctWords);
    }

//    public void sortCorrectWordsBySoundex(){
//        try {
//            Collections.sort(correctWords, new Comparator<String>(){
//                public int compare(String str1, String str2){
//                    Soundex soundes = new Soundex();
//                    return soundex.difference(str1, str2);
//                }
//            });
//        }catch(IllegalArgumentException e){
//            System.out.println(this.wrong_word);
//            System.out.println("There is an illegal argument exception.");
//        }
//    }

    public void sortCorrectWords(){
        try {
            Collections.sort(correctWords, new Comparator<String>(){
                public int compare(String str1, String str2){
                    return dictionary.get(str2).subtract(dictionary.get(str1)).intValue();
                }
            });
        }catch(IllegalArgumentException e){
            System.out.println(this.wrong_word);
            System.out.println("There is an illegal argument exception.");
        }
    }

}
