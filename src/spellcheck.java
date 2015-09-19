import java.io.*;
import java.util.*;
import java.math.*;

public class spellcheck {
    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static List<String> correctwords;

    public static void main(String[] args) throws NumberFormatException, IOException {
        correctwords = new ArrayList<String>();
        System.out.println(args[0]);
        BufferedReader br = new BufferedReader(new FileReader("test_db.csv"));
        String line =  null;

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }

        if(dictionary.containsKey(args[0].toUpperCase()))
        {
            System.out.println("yes it is a correct word");
        }
        else{
            /*System.out.println("no it is a wrong word");*/
            List<String> editings = editings(args[0]);
            for(int i=0; i < editings.size(); i++){
                if(checkspelling(editings.get(i)) && !correctwords.contains(editings.get(i))){
                    correctwords.add(editings.get(i));
                }
                /*For each edit 1 distance word, get the words with distance 1 and add known words to correctwords.*/
                List<String> edit2 = editings(editings.get(i));
                for (int j = 0; j < edit2.size(); j++){
                    if (checkspelling(edit2.get(j)) && !correctwords.contains(edit2.get(j))){
                        correctwords.add(edit2.get(j));
                    }
                }
            }
            //System.out.println(correctwords);
            printout(correctwords);
        }	
    }	

    public static void printout(List<String> words){
        System.out.println(correctwords);
    }


    public static Boolean checkspelling(String word){
        if(dictionary.containsKey(word.toUpperCase()))
        {
            return true;
        }
        else
            return false;		
    }


    public static List<String> editings(String word) {
        List<String> list = new ArrayList<String>();

        /*Deletions*/
        int wordlength=word.length();
        for(int i=0;i<wordlength;i++){
            String temp = word.substring(0,i)+word.substring(i+1,wordlength) ;
            list.add(temp);	
        }

        /*replacing a letter*/
        for(int i=0;i<wordlength;i++){
            for(char j='a';j<'z';j++){
                String temp = word.substring(0,i)+j+word.substring(i+1,wordlength) ;
                list.add(temp);	
            }
        }

        /*insertion of one letter*/
        for(int i=0;i<wordlength+1;i++){
            for(char j='a';j<'z';j++){
                String temp = word.substring(0,i)+j+word.substring(i,wordlength) ;
                list.add(temp);	
            }
        }

        /*transpose of adjacent symbols */
        for(int i=0;i<wordlength-1;i++){

            char a = word.charAt(i);
            char b = word.charAt(i+1);

            String temp = word.substring(0,i)+b+word.substring(i+1);
            temp = temp.substring(0,i+1)+a+temp.substring(i+2);

            list.add(temp);
        }

        return list;
    }
}

