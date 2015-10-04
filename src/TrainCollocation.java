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
        
        BufferedReader br = new BufferedReader(new FileReader("data/test_db.csv"));
        String line =  null;

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0].toLowerCase(),abcd);
        }
		
        br = new BufferedReader(new FileReader("data/homophonedb.txt"));


        while((line = br.readLine()) != null){
            String arr[] = line.split(",");
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < arr.length; i++){
                temp.add(arr[i].trim());
                //System.out.println(arr[i].trim());
            }

            homophonedb.add(temp);
        }

        br = new BufferedReader(new FileReader("data/colocationtest.txt"));
        line= null;
        int counter=0;
        String temp="";
        while((line=br.readLine())!=null){
            if(counter % 2 == 0){
                temp=line;
                /*System.out.println(line);*/
            }
            else{
                                
                String[] keyValuePairs = line.split("/");  
                HashMap<Collocation, Integer> tempvalue = new HashMap<Collocation, Integer> ();               
                
                for(String pair : keyValuePairs)                
                {
                    String[] eachcolocation = pair.split("-"); 
                    
                    String posarraystring =eachcolocation[0];
                    posarraystring = posarraystring.substring(1, posarraystring.length()-1);
                    
                    ArrayList<ColWord> collocate = new ArrayList<ColWord>();
                    String[] posarray=posarraystring.split(", ");
                    for(String item : posarray){    
                        ColWord worrd = new ColWord(item);
                        collocate.add(worrd);
                    }
                    int side =Integer.parseInt(eachcolocation[1]);
                    Collocation newcol = new Collocation(collocate,side);
                    Integer nummb = Integer.parseInt(eachcolocation[2]);
                    tempvalue.put(newcol, nummb);  
                }
                collocations.put(temp, tempvalue); 
            }
            counter++;
        }

        
        
        br = new BufferedReader(new FileReader("data/test.txt"));
        
        while((line = br.readLine()) != null){
            String arr[] = line.split(" ");
            for(int k = 0; k < arr.length -1; k++)
            	System.out.print(arr[k]+ " ");
            System.out.print(arr[arr.length - 1]);
            System.out.print("\t");
            for(int i = 0; i < arr.length; i++){
            	if(!dictionary.containsKey(arr[i])){
            		
            		// no word in dictionary. Provide correct words and rate them
            		// get around 15 words with edit distance(trigrams) and continue.
            		//System.out.println(arr[i]);
            		ArrayList<String> amb_words = getAmbiguousWords(arr[i]); //  <- Change this.
                    Map<String,Integer> scoremap = new HashMap<String,Integer>();
                    ArrayList<ColWord> given_coll = new ArrayList<ColWord>();
                    ArrayList<ColWord> given_right_coll = new ArrayList<ColWord>();
            		
                    if (i > 1){                           
                        for (int j = 1; j < i && j < arr.length; j++){
                            given_coll.add(new ColWord(posmap.get(arr[j])));
                        }
                    }

                    if (i < 5){
                        for (int j = i+1; j < 6 && j < arr.length; j++){
                            given_right_coll.add(new ColWord(posmap.get(arr[j])));
                        }
                    }
                    
            		for(int j = 0; j < amb_words.size(); j++){
            			if(!collocations.containsKey(amb_words.get(j))){
            				scoremap.put(amb_words.get(j), 0);
            				continue;
            			}
                    	HashMap<Collocation,Integer> colmap = collocations.get(amb_words.get(j));
                    	
                    	Integer tempscore = 0;
                    	if(given_coll != null){
                    		//System.out.println("colmap - " + colmap );
                    		//System.out.println("givencoll - " + given_coll);
                    		Iterator it = colmap.entrySet().iterator();
                    	    while (it.hasNext()) {
                    	        Map.Entry pair = (Map.Entry)it.next();
                    	        int tempstr = getStrength((Collocation)pair.getKey(), new Collocation(given_coll, 0));
                    	        tempscore += tempstr*tempstr * (Integer)pair.getValue();
                    	        
                    	        //tempscore += (getStrength((Collocation)pair.getKey(), new Collocation(given_right_coll, 1))^2)*(Integer)pair.getValue();
                    	        //System.out.println(pair.getKey() + " = " + pair.getValue());
                    	        //it.remove(); // avoids a ConcurrentModificationException
                    	    }
                    	}
                    	if(given_right_coll != null){
                    		Iterator it = colmap.entrySet().iterator();
                    		while (it.hasNext()) {
                    	        Map.Entry pair = (Map.Entry)it.next();
                    	        int tempstr = getStrength((Collocation)pair.getKey(), new Collocation(given_right_coll, 1));
                    	        tempscore += tempstr*tempstr * (Integer)pair.getValue();
                    	        
                    	        //tempscore += (getStrength((Collocation)pair.getKey(), new Collocation(given_right_coll, 1))^2)*(Integer)pair.getValue();
                    	        //System.out.println(pair.getKey() + " = " + pair.getValue());
                    	        //it.remove(); // avoids a ConcurrentModificationException
                    	    }

                    		
                    	}
                    	scoremap.put(amb_words.get(j), tempscore);
                    		
                    }
                    
                    scoremap = sortByValue(scoremap);
                    List<String> list1 = new ArrayList<String>(scoremap.keySet());
                    //System.out.println(list1.get(0) + ", " + list1.get(1) + ", " + list1.get(2));
            		//System.out.println(list1);
            		//break;
            	}
            	else{
            		if (isAmbiguous(arr[i])){
            			
                        ArrayList<String> amb_words = getAmbiguousWords(arr[i]);
                       // System.out.println(amb_words);
                        Map<String,Integer> scoremap = new HashMap<String,Integer>();
                        ArrayList<ColWord> given_coll = new ArrayList<ColWord>();
                        ArrayList<ColWord> given_right_coll = new ArrayList<ColWord>();
                        
                        if (i > 1){                           
                            for (int j = 1; j < i && j < arr.length; j++){
                                given_coll.add(new ColWord(posmap.get(arr[j])));
                                //System.out.println("givencoll - " + given_coll);
                            }
                        }
                        if (i < 5){
                            for (int j = i; j < 6 && j < arr.length; j++){
                            	//System.out.println(j);
                                given_right_coll.add(new ColWord(posmap.get(arr[j])));
                                //System.out.println("givenrightcoll - " + given_right_coll);
                            }
                        }
                        for(int j = 0; j < amb_words.size(); j++){
                        	//System.out.println(amb_words.get(j));
                        	if(!collocations.containsKey(amb_words.get(j))){
                				scoremap.put(amb_words.get(j), 0);
                				continue;
                			}
                        	HashMap<Collocation,Integer> colmap = collocations.get(amb_words.get(j));
                        	//System.out.println(colmap);
                        	Integer tempscore = 0;
                        	if(given_coll != null){
                        		//System.out.println("colmap - " + colmap );
                        		//System.out.println("givencoll - " + given_coll);
                        		Iterator it = colmap.entrySet().iterator();
                        	    while (it.hasNext()) {
                        	        Map.Entry pair = (Map.Entry)it.next();
                        	        int tempstr = getStrength((Collocation)pair.getKey(), new Collocation(given_coll, 0));
                        	        tempscore += tempstr*tempstr * (Integer)pair.getValue();
                        	        
                        	        //tempscore += (getStrength((Collocation)pair.getKey(), new Collocation(given_right_coll, 1))^2)*(Integer)pair.getValue();
                        	        //System.out.println(pair.getKey() + " = " + pair.getValue());
                        	        //it.remove(); // avoids a ConcurrentModificationException
                        	    }
                        	}
                        	if(given_right_coll != null){
                        		Iterator it = colmap.entrySet().iterator();
                        		while (it.hasNext()) {
                        	        Map.Entry pair = (Map.Entry)it.next();
                        	        int tempstr = getStrength((Collocation)pair.getKey(), new Collocation(given_right_coll, 1));
                        	       // System.out.println(tempstr);
                        	        tempscore += tempstr*tempstr * (Integer)pair.getValue();
                        	        
                        	        //tempscore += (getStrength((Collocation)pair.getKey(), new Collocation(given_right_coll, 1))^2)*(Integer)pair.getValue();
                        	        //System.out.println(pair.getKey() + " = " + pair.getValue());
                        	        //it.remove(); // avoids a ConcurrentModificationException
                        	    }

                        		
                        	}
                        	scoremap.put(amb_words.get(j), tempscore);
                        		
                        }
                        
                        scoremap = sortByValue(scoremap);
                        //System.out.println(scoremap);
                        int value1 = 0, value2 = 0;
                        Iterator it = scoremap.entrySet().iterator();
                        if(it.hasNext()){
                        	 Map.Entry pair = (Map.Entry)it.next();
                        	 value1 = (int)pair.getValue();
                        	 if(it.hasNext()){
                        		 pair = (Map.Entry)it.next();
                        		 value2 = (int)pair.getValue();
                        	 }
                        }
                        List<String> keylist = new ArrayList<String>(scoremap.keySet());
                        
                        if(keylist.get(0) != arr[i]){
                        	for(int k = 0; k < i; k++)
                        		System.out.print(arr[k]+" ");
                        	System.out.print(keylist.get(0));
                        	for(int k = i+1; k < arr.length; k++)
                        		System.out.print(" " + arr[k]);
                        	System.out.print("\t" + value1 + "\t");
                        	
                        	for(int k = 0; k < i; k++)
                        		System.out.print(arr[k]+" ");
                        	System.out.print(keylist.get(1));
                        	for(int k = i+1; k < arr.length; k++)
                        		System.out.print(" " + arr[k]);
                        	System.out.print("\t" + value2);
                        }
                        else{
                        	for(int k = 0; k < i; k++)
                        		System.out.print(arr[k]+" ");
                        	System.out.print(keylist.get(1));
                        	for(int k = i+1; k < arr.length; k++)
                        		System.out.print(" " + arr[k]);
                        	System.out.print("\t" + value2 + "\t");
                        	
                        	for(int k = 0; k < i; k++)
                        		System.out.print(arr[k]+" ");
                        	System.out.print(keylist.get(0));
                        	for(int k = i+1; k < arr.length; k++)
                        		System.out.print(" " + arr[k]);
                        	System.out.print("\t" + value1);
                        }
                        System.out.print("\t");
                       // List<Integer> valuelist = new ArrayList<Integer>(scoremap.valueSet());
                        //System.out.println(list1);
                        //System.out.println(list1.get(0) + ", " + list1.get(1) + ", " + list1.get(2));
                        break;

                    }
            	}
            }
        }
        

        //System.out.println(collocations);

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
    
    public static int getStrength(Collocation x, Collocation y){
    	
    	if(x.getSide() != y.getSide()){
    		//System.out.println("ok");
    		return 1;
    	}
    	return 1+lcs(x.getCollocation(), y.getCollocation(), x.getSize(), y.getSize());
    	
    }
    
    public static int lcs(ArrayList<ColWord> x, ArrayList<ColWord> y, int xsize, int ysize){
    	if (xsize == 0 || ysize == 0)
    	     return 0;
    	   if (x.get(xsize-1).equals(y.get(ysize-1)))
    	     return 1 + lcs(x, y, xsize-1, ysize-1);
    	   else
    	     return Math.max(lcs(x, y, xsize, ysize-1), lcs(x, y, xsize-1, ysize));
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

