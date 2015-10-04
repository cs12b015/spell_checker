import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class fake {
    public static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();
    
	public static void main(String[] args) throws IOException{
		
        		String line = "{vvi, ppho1, ii, at1}-1-89/{to}-0-89/{vvi, np1, cc}-1-5/{vvgk, to}-0-5";
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
        		
        		
        		
        		
     
        		
        		
        	
        	
        
		
	}
}
br = new BufferedReader(new FileReader("data/w5c.txt"));

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

