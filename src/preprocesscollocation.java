import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class preprocesscollocation {
    public static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();
    
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("data/colocationtest.txt"));
		String line= null;
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
	}
}
