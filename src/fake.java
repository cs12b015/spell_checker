import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class fake {
    public static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();
    
	public static void main(String[] args) throws IOException{
		
        		String line = "{vvi, ppho1, ii, at1}-1-89|{to}-0-89|{vvi, np1, cc}-1-5|{vvgk, to}-0-5";
        		
        		String[] keyValuePairs = line.split("|");  
        		
        		HashMap<Collocation, Integer> tempvalue = new HashMap<Collocation, Integer> ();               
        		
        		for(String pair : keyValuePairs)                
        		{
        		    System.out.println(pair);
        		}
        		
        		
        		
        	
        	
        
		
	}
}
