import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;
/*import org.apache.commons.codec.language;*/

public class SpellCheck {

    private Map<String, BigInteger> dictionary;
    private List<String> correctWords;
    private List<String> seen;
    private List<String> unseen;
    private String wrong_word;

    /*Constructors for SpellCheck class*/

    public SpellCheck(Map<String, BigInteger> dictionary){
        this.dictionary = dictionary;
        this.correctWords = new ArrayList<String>();
        this.seen = new ArrayList<String>();
        this.unseen = new ArrayList<String>();
    }

    public void printOut(){
        System.out.println(correctWords);
    }

    public boolean isCorrect(String word){
        if(dictionary.containsKey(word.toUpperCase())) return true;
        return false;
    }

    public void edit(String word){

        //System.out.println("Entered edit for word : " + word);
        if (!seen.contains(word)){

            //System.out.println("Word not present in seen : " + word);

            seen.add(word);
            int wordlength=word.length();

            /*Deletions*/
            for(int i=0;i<wordlength;i++){
                String temp = word.substring(0,i)+word.substring(i+1,wordlength) ;
                if (!unseen.contains(temp) && !seen.contains(temp)) unseen.add(temp);	
            }

            /*replacing a letter*/
            for(int i=0;i<wordlength;i++){
                for(char j='A';j<'Z';j++){
                    String temp = word.substring(0,i)+j+word.substring(i+1,wordlength) ;
                    if (!unseen.contains(temp) && !seen.contains(temp)) unseen.add(temp);	
                }
            }

            /*insertion of one letter*/
            for(int i=0;i<wordlength+1;i++){
                for(char j='A';j<'Z';j++){
                    String temp = word.substring(0,i)+j+word.substring(i,wordlength) ;
                    if (!unseen.contains(temp) && !seen.contains(temp)) unseen.add(temp);	
                }
            }

            /*transpose of adjacent symbols */
            for(int i=0;i<wordlength-1;i++){

                char a = word.charAt(i);
                char b = word.charAt(i+1);

                String temp = word.substring(0,i)+b+word.substring(i+1);
                temp = temp.substring(0,i+1)+a+temp.substring(i+2);

                if (!unseen.contains(temp) && !seen.contains(temp)) unseen.add(temp);	
            }

            unseen.remove(word);

        }

    }

    public void kedit(String word, int k){

        this.wrong_word = word;
        unseen.add(word);
        List<String> current;

        for (int i = 0; i < k; i++){
            //System.out.println("The depth is currently : " + i);
            current = new ArrayList<String>(unseen.size());

            for (String str: unseen){
                current.add(str);
            }
            //System.out.println(current);
            //System.out.println(current.size());
            for (int j = 0; j < current.size(); j++){
                edit(current.get(j));
                //System.out.println(seen);
            }
        }
    }

    public void setCorrectWords(){
        for (int i = 0; i < seen.size(); i++){
            if (isCorrect(seen.get(i))) correctWords.add(seen.get(i));
        }
        for (int j = 0; j < unseen.size(); j++){
            if (isCorrect(unseen.get(j))) correctWords.add(unseen.get(j));
        }
    }

    public ArrayList<String> getCorrectWords(){
        return new ArrayList<>(this.correctWords);
    }

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

  /*  public void sortCorrectWordsBySoundex(){
        try {
            Collections.sort(correctWords, new Comparator<String>(){
                public int compare(String str1, String str2){
                    Soundex soundes = new Soundex();
                    return soundex.difference(str1, str2);
                }
            });
        }catch(IllegalArgumentException e){
            System.out.println(this.wrong_word);
            System.out.println("There is an illegal argument exception.");
        }
    }*/


}
