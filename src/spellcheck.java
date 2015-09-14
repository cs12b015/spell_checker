import java.io.*;
import java.util.*;
import java.math.*;

public class spellcheck {
	public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		System.out.println(args[0]);
		BufferedReader br = new BufferedReader(new FileReader("src/test_db.csv"));
	    String line =  null;
	    
	    while((line=br.readLine())!=null){
	        String arr[] = line.split("\t");
	        BigInteger abcd = new BigInteger(arr[1]);
	            dictionary.put(arr[0],abcd );
	    }
	    
	    if(dictionary.containsKey(args[0].toUpperCase()))
	    {
	    	System.out.println("yes it is a correct word");
	    }
	    else{
	    	System.out.println("no it is a wrong word");
	    }	
	    
		
		
	}	
	
	
	
	
	
	
}

