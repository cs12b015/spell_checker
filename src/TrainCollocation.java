import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class TrainCollocation{

    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();
    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static Map<String, String> posmap = new HashMap<String, String>();

    public static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();
    
    public static void main(String[] args) throws NumberFormatException, IOException {
        
        BufferedReader br = new BufferedReader(new FileReader("src/test_db.csv"));
        String line =  null;

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }
    	
        br = new BufferedReader(new FileReader("src/homophonedb.txt"));


        while((line = br.readLine()) != null){
            String arr[] = line.split(",");
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < arr.length; i++){
                temp.add(arr[i].trim());
                //System.out.println(arr[i].trim());
            }

            homophonedb.add(temp);
        }

        br = new BufferedReader(new FileReader("src/w5c.txt"));

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");

            for (int i = 1; i < 6; i++){
            	
            	if(!posmap.containsKey(arr[i])){
            		posmap.put(arr[i], arr[i+5]);
            	}
            	
                if (isAmbiguous(arr[i])){

                    if (collocations.containsKey(arr[i])){

                        HashMap<Collocation, Integer> temp_coll_hash = collocations.get(arr[i]);

                        if (i > 1){
                            ArrayList<ColWord> temp_coll = new ArrayList<ColWord>();
                            for (int j = 1; j < i; j++){
                                ColWord temp = new ColWord(arr[j+5]);
                                temp_coll.add(temp);
                            }
                            Collocation coll = new Collocation(temp_coll);

                            if (temp_coll_hash.containsKey(coll)){
                                temp_coll_hash.put(coll, temp_coll_hash.get(coll)+ Integer.parseInt(arr[0]));
                            }else {
                                temp_coll_hash.put(coll, Integer.parseInt(arr[0]));
                            }
                        }

                        if (i < 5){
                            ArrayList<ColWord> temp_right_coll = new ArrayList<ColWord>();
                            for (int j = i; j < 6; j++){
                                ColWord temp = new ColWord(arr[j+5]);
                                temp_right_coll.add(temp);
                            }
                            Collocation right_coll = new Collocation(temp_right_coll, 1);

                            if (temp_coll_hash.containsKey(right_coll)){
                                temp_coll_hash.put(right_coll, temp_coll_hash.get(right_coll) + Integer.parseInt(arr[0]));
                            }else {
                                temp_coll_hash.put(right_coll, Integer.parseInt(arr[0]));
                            }
                        }

                        collocations.put(arr[i], temp_coll_hash);

                    }else {
                        HashMap<Collocation, Integer> temp_coll_hash = new HashMap<Collocation, Integer>();

                        if (i > 1){
                            ArrayList<ColWord> temp_coll = new ArrayList<ColWord>();
                            for (int j = 1; j < i; j++){
                                ColWord temp = new ColWord(arr[j+5]);
                                temp_coll.add(temp);
                            }

                            temp_coll_hash.put(new Collocation(temp_coll), Integer.parseInt(arr[0]));
                        }


                        if (i < 5){
                            ArrayList<ColWord> temp_right_coll = new ArrayList<ColWord>();
                            for (int j = i; j < 6; j++){
                                ColWord temp = new ColWord(arr[j+5]);
                                temp_right_coll.add(temp);
                            }

                            temp_coll_hash.put(new Collocation(temp_right_coll, 1), Integer.parseInt(arr[0]));
                        }

                        collocations.put(arr[i], temp_coll_hash);
                    }

                }
            }
        }
        
        br = new BufferedReader(new FileReader("src/test.txt"));
        while((line = br.readLine()) != null){
            String arr[] = line.split(" ");
            for(int i = 0; i < arr.length; i++){
            	if(!dictionary.containsKey(arr[i])){
            		
            		// no word in dictionary. Provide correct words and rate them
            		// get around 15 words with edit distance(trigrams) and continue.
            		
            		ArrayList<String> amb_words = getAmbiguousWords(arr[i]); //  <- Change this.
                    Map<String,Integer> scoremap = new HashMap<String,Integer>();
                    ArrayList<ColWord> given_coll = new ArrayList<ColWord>();
                    ArrayList<ColWord> given_right_coll = new ArrayList<ColWord>();
            		
                    if (i > 1){                           
                        for (int j = 1; j < i; j++){
                            given_coll.add(new ColWord(posmap.get(arr[j])));
                        }
                    }

                    if (i < 5){
                        for (int j = i; j < 6; j++){
                            given_right_coll.add(new ColWord(posmap.get(arr[j])));
                        }
                    }
                    
            		for(int j = 0; j < amb_words.size(); j++){
                    	HashMap<Collocation,Integer> colmap = collocations.get(amb_words.get(j));
                    	
                    	Integer tempscore = 0;
                    	if(colmap.containsKey(given_coll))
                    		tempscore += colmap.get(given_coll);
                    	if(colmap.containsKey(given_right_coll))
                    		tempscore += colmap.get(given_right_coll);
                    	scoremap.put(amb_words.get(j), tempscore);
                    		
                    }
                    
                    scoremap = sortByValue(scoremap);
                    List<String> list1 = new ArrayList<String>(scoremap.keySet());
                    System.out.println(list1.get(0) + ", " + list1.get(1) + ", " + list1.get(2));
            		
            		break;
            	}
            	else{
            		if (isAmbiguous(arr[i])){
                        
                        ArrayList<String> amb_words = getAmbiguousWords(arr[i]);
                        Map<String,Integer> scoremap = new HashMap<String,Integer>();
                        ArrayList<ColWord> given_coll = new ArrayList<ColWord>();
                        ArrayList<ColWord> given_right_coll = new ArrayList<ColWord>();
                        
                        if (i > 1){                           
                            for (int j = 1; j < i; j++){
                                given_coll.add(new ColWord(posmap.get(arr[j])));
                            }
                        }

                        if (i < 5){
                            for (int j = i; j < 6; j++){
                                given_right_coll.add(new ColWord(posmap.get(arr[j])));
                            }
                        }
                        for(int j = 0; j < amb_words.size(); j++){
                        	HashMap<Collocation,Integer> colmap = collocations.get(amb_words.get(j));
                        	
                        	Integer tempscore = 0;
                        	if(colmap.containsKey(given_coll))
                        		tempscore += colmap.get(given_coll);
                        	if(colmap.containsKey(given_right_coll))
                        		tempscore += colmap.get(given_right_coll);
                        	scoremap.put(amb_words.get(j), tempscore);
                        		
                        }
                        
                        scoremap = sortByValue(scoremap);
                        List<String> list1 = new ArrayList<String>(scoremap.keySet());
                        System.out.println(list1.get(0) + ", " + list1.get(1) + ", " + list1.get(2));
                        //break;

                    }
            	}
            }
        }

        System.out.println(collocations);

    }
    
    public static Map<String, Integer>  sortByValue(Map<String, Integer>  unsortMap) {	 
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
