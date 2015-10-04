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
        		        		
        		String[] keyValuePairs = line.split("|");  
        		System.out.println(counter/2+" "+keyValuePairs[1]);
        		
        		/*HashMap<String, Integer> tempvalue = new HashMap<String, Integer> ();               

        		for(String pair : keyValuePairs)                
        		{
        		    String[] entry = pair.split("=");         
        		    tempvalue.put(entry[0].trim(), Integer.parseInt(entry[1].trim()));
        		}
        		
        		likelihood.put(temp, tempvalue);	*/
        	}
        	counter++;
        }
		
	}
}
