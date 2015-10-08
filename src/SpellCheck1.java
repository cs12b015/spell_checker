
import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.*;

//import org.apache.commons.codec.EncoderException;
//import org.apache.commons.codec.language.*;

public class SpellCheck1 {
    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static Map<String, ArrayList<String>> trigrams = new HashMap<String, ArrayList<String>>();
    public static ArrayList<String> correctWords = new ArrayList<String>();
    public static String wrong_word;

    public SpellCheck1(Map<String, BigInteger> dictionary, Map<String, ArrayList<String>> trigrams, String line) throws IOException{
        this.dictionary = dictionary;
        this.trigrams = trigrams;
        this.wrong_word = line;

        
        if (line.trim().length() >15){
        	correctWords = generatecorrect(line.toUpperCase());
        }
        else{
        	correctWords = generatecorrectbruteforce(line.toUpperCase());
        }
        //System.out.println(correctwords);
    }
    
  

    public ArrayList<String> getResult(){
        return this.correctWords;
    }

    public ArrayList<String> generatecorrectbruteforce(String word) {

    	Map<String, BigInteger> correctwords1 = new HashMap<String, BigInteger>();
    	Map<String, BigInteger> correctwords2 = new HashMap<String, BigInteger>();
    	
        List<String> editings = editings(word);
        for(int i=0; i < editings.size(); i++){
            if(checkspelling(editings.get(i)) && !correctwords1.containsKey(editings.get(i))){
                correctwords1.put(editings.get(i), dictionary.get(editings.get(i).toUpperCase()));
            }
            List<String> edit2 = editings(editings.get(i));
            for (int j = 0; j < edit2.size(); j++){
                if (checkspelling(edit2.get(j)) && !correctwords1.containsKey(edit2.get(j))){
                    correctwords2.put(edit2.get(j), dictionary.get(edit2.get(j).toUpperCase()));
                }
            }
        }
        correctwords1=sortByValue(correctwords1);
        correctwords2=sortByValue(correctwords2);
        List<String> list1 = new ArrayList<String>(correctwords1.keySet());
        List<String> list2 = new ArrayList<String>(correctwords2.keySet());
        for(int z  = 0; z < list2.size(); z++){
            if(!list1.contains(list2.get(z))){
            	list1.add(list2.get(z));
            }
        }
        List<String> list3;
        if(list1.size() >=10){
        	 list3 =list1.subList(0, 10);
        }
        else{
        	 list3=list1;
        }
        
        ArrayList<String> mylist = new ArrayList<String>(list3);
        return mylist;    	
    }

    public Boolean checkspelling(String word){
        if(dictionary.containsKey(word.toUpperCase()))
        {
            return true;
        }
        else
            return false;		
    }
    
    public static Map<String, BigInteger>  sortByValue(Map<String, BigInteger>  unsortMap) {	 
    	List list = new LinkedList(unsortMap.entrySet());
     
    	Collections.sort(list, new Comparator() {
    		public int compare(Object o2, Object o1) {
    			return ((Comparable) ((Map.Entry) (o1)).getValue())
    						.compareTo(((Map.Entry) (o2)).getValue());
    		}
    	});
     
    	Map sortedMap = new LinkedHashMap();
    	for (Iterator it = list.iterator(); it.hasNext();) {
    		Map.Entry entry = (Map.Entry) it.next();
    		sortedMap.put(entry.getKey(), entry.getValue());
    	}
    	return sortedMap;
    }


    public ArrayList<String> generatecorrect(String word){
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
        ArrayList<String> oneedit=new ArrayList<String>();
        ArrayList<String> twoedit=new ArrayList<String>();
        for(int i=0;i<possiblecandidates.size();i++){
            if(edit_distance(possiblecandidates.get(i),word,possiblecandidates.get(i).length(),word.length())==1)
            {
                oneedit.add(possiblecandidates.get(i));
            }
            
            if(edit_distance(possiblecandidates.get(i),word,possiblecandidates.get(i).length(),word.length())==2)
            {
                twoedit.add(possiblecandidates.get(i));
            }
            
        }
        //overallsort(oneedit,twoedit,word);
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
    
    
    
    public List<String> editings(String word) {
        List<String> list = new ArrayList<String>();

        /*Deletions*/
        int wordlength=word.length();
        for(int i=0;i<wordlength;i++){
            String temp = word.substring(0,i)+word.substring(i+1,wordlength) ;
            list.add(temp.toUpperCase());	
        }

        /*replacing a letter*/
        for(int i=0;i<wordlength;i++){
            for(char j='a';j<'z';j++){
                String temp = word.substring(0,i)+j+word.substring(i+1,wordlength) ;
                list.add(temp.toUpperCase());	
            }
        }

        /*insertion of one letter*/
        for(int i=0;i<wordlength+1;i++){
            for(char j='a';j<'z';j++){
                String temp = word.substring(0,i)+j+word.substring(i,wordlength) ;
                list.add(temp.toUpperCase());	
            }
        }

        /*transpose of adjacent symbols */
        for(int i=0;i<wordlength-1;i++){

            char a = word.charAt(i);
            char b = word.charAt(i+1);

            String temp = word.substring(0,i)+b+word.substring(i+1);
            temp = temp.substring(0,i+1)+a+temp.substring(i+2);

            list.add(temp.toUpperCase());
        }

        return list;
    }

    public void printOut(){
        System.out.println(correctWords);
    }

//    public void sortCorrectWordsBySoundex(){
//        try {
//            Collections.sort(correctWords, new Comparator<String>(){
//                public int compare(String str1, String str2){
//                    Soundex soundex = new Soundex();
//                    try {
//						return soundex.difference(str1, str2);
//					} catch (EncoderException e) {
//						// TODO Auto-generated catch block			
//						e.printStackTrace();
//						return 0;
//					}
//                }
//             });
//       }catch(Exception e){
//            System.out.println(this.wrong_word);
//            System.out.println("There is an illegal argument exception.");
//        }
//    }


    /*
    public ArrayList<String> overallsort(ArrayList<String> oneedit,ArrayList<String> twoedit,String word) throws EncoderException{
    	ArrayList<BigInteger> editonedist = new ArrayList<BigInteger>();
    	ArrayList<BigInteger> edittwodist = new ArrayList<BigInteger>();
    	Map<String,BigInteger> map=new HashMap<String,BigInteger>();
    	BigInteger numb = new BigInteger("2");
    	for(int i=0;i<oneedit.size();i++){
    		int temmp = soundexdiff(oneedit.get(i),word);
    		BigInteger bigstr = BigInteger.valueOf(temmp);
    		editonedist.add( dictionary.get(oneedit.get(i)).multiply(numb).multiply(bigstr) );	
    		map.put(oneedit.get(i),dictionary.get(oneedit.get(i)).multiply(numb).multiply(bigstr));
    	}
    	for(int i=0;i<twoedit.size();i++){
    		int temmp = soundexdiff(twoedit.get(i),word);
    		BigInteger bigstr = BigInteger.valueOf(temmp);
    		edittwodist.add( dictionary.get(twoedit.get(i)).multiply(bigstr) );
    		map.put(twoedit.get(i), dictionary.get(twoedit.get(i)).multiply(bigstr) );
    	}
    	map=sortByValue(map);
    	System.out.println(map);
    	
    	ArrayList<String> original = new ArrayList<String> ();
    	
    	
    	return original;
    }
    */
    
    
//    public int soundexdiff(String str1,String str2) throws EncoderException{
//    	Soundex soundex = new Soundex();
//    	return soundex.difference(str1, str2);
//    }
    
//    public void sortCorrectWordsBySoundex(){
//        try {
//            Collections.sort(correctWords, new Comparator<String>(){
//                public int compare(String str1, String str2){
//                    Soundex soundex = new Soundex();
//                    try {
//						return soundex.difference(str1, str2);
//					} catch (EncoderException e) {
//						// TODO Auto-generated catch block			
//						e.printStackTrace();
//						return 0;
//					}
//                }
//             });
//       }catch(Exception e){
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
