import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.*;

public class onlyspellcheck {
    public static Map<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static Map<String, ArrayList<String>> trigrams = new HashMap<String, ArrayList<String>>();
    
    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new FileReader("data/test_db.csv"));
        String line =  null;

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
        }
        
        br = new BufferedReader(new FileReader("data/trigrams_db.csv"));
        line=null;
        while((line=br.readLine())!=null){
        	String tristring = line.substring(0,3);
        	
        	String arraystring= line.substring(4);
        	arraystring=arraystring.substring(1, arraystring.length()-1);
        	String[] keyValuePairs = arraystring.split(", "); 
        	ArrayList<String> myarray= new ArrayList<String>();
        	for(String pair : keyValuePairs)                
    		{
        		myarray.add(pair);
    		}
        	trigrams.put(tristring, myarray);	
        	
        }
        PrintStream out = new PrintStream(new FileOutputStream("data/temp.txt"));
        System.setOut(out);    
    	
    	
    	
    	br = new BufferedReader(new FileReader("data/spellchecktest.txt"));
        line=null;
        while((line=br.readLine())!=null){
        	/*System.out.println(line);*/
        	ArrayList<String> correctwords=genetatecorrect(line.toUpperCase());
        	System.out.println(correctwords);
        	
        }
        
    }

	public static ArrayList<String> genetatecorrect(String word) {
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
		for(int i=0;i<possiblecandidates.size();i++){
			if(edit_distance(possiblecandidates.get(i),word,possiblecandidates.get(i).length(),word.length())<3)
			{
				orginalcandidates.add(possiblecandidates.get(i));
			}
		}
		return orginalcandidates;
	}
	 public static int edit_distance(String string1,String string2,int m,int n){
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
}
