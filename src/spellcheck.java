import java.io.*;
import java.util.*;
import java.math.*;

public class spellcheck {
    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static Map<String, BigInteger> correctwords1 = new HashMap<String, BigInteger>();
    public static Map<String, BigInteger> correctwords2 = new HashMap<String, BigInteger>();
    public static int[][] del_cm = new int[26][26];

    public static void main(String[] args) throws NumberFormatException, IOException {
       // System.out.println(args[0]);
        BufferedReader br = new BufferedReader(new FileReader("src/test_db.csv"));
        String line =  null;

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }

        BufferedReader br_del = new BufferedReader(new FileReader("src/del_cm.csv"));

        String del_line = null;
        int l = 0;
        while((del_line = br_del.readLine()) != null){
            String del_arr[] = del_line.split("\\s+");
            //System.out.println(del_arr);
            int m = 0;
            for (String del_int : del_arr){
                del_cm[l][m] = Integer.parseInt(del_int);
                //System.out.print(del_cm[l][m]);
                m++;
            }
            //System.out.println(" ");
            l++;
        }


        if(dictionary.containsKey(args[0].toUpperCase()))
        {
            System.out.println("yes it is a correct word");
        }
        else{
            /*System.out.println("no it is a wrong word");*/
            List<String> editings = editings(args[0]);
            for(int i=0; i < editings.size(); i++){
                if(checkspelling(editings.get(i)) && !correctwords1.containsKey(editings.get(i))){
                    correctwords1.put(editings.get(i), dictionary.get(editings.get(i).toUpperCase()));
                }
                /*For each edit 1 distance word, get the words with distance 1 and add known words to correctwords.*/
                List<String> edit2 = editings(editings.get(i));
                for (int j = 0; j < edit2.size(); j++){
                    if (checkspelling(edit2.get(j)) && !correctwords1.containsKey(edit2.get(j))){
                        correctwords2.put(edit2.get(j), dictionary.get(edit2.get(j).toUpperCase()));
                    }
                    /*For each edit 2 distance words, get the words*/
                }
            }
            //System.out.println(correctwords);
            /*printout(correctwords1);
            printout(correctwords2);*/
            correctwords1=sortByValue(correctwords1);
            correctwords2=sortByValue(correctwords2);
            /*printout(correctwords1);
            printout(correctwords2);*/
            List<String> list1 = new ArrayList<String>(correctwords1.keySet());
            List<String> list2 = new ArrayList<String>(correctwords2.keySet());
            for(int z  = 0; z < list2.size(); z++){
                if(!list1.contains(list2.get(z))){
                	list1.add(list2.get(z));
                }
            }
            List<String> list3 =list1.subList(0, 3);
            System.out.println(list3);
            
            
           /* TreeMap<String, Integer> sortedMap1 = SortByValue(correctwords1);
            TreeMap<String, Integer> sortedMap2 = SortByValue(correctwords2);*/
        }	
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
    
    public static void printout(Map<String, BigInteger> words){
        System.out.println(words);
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

