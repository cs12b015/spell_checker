import java.io.*;
import java.util.*;
import java.math.*;

public class Solver {


    public static HashMap<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();
    public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();
    public static Map<String, ArrayList<String>> trigrams = new HashMap<String, ArrayList<String>>();
    public static Map<String, String> posmap = new HashMap<String, String>();
    public static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();

    public static void main(String[] args) throws NumberFormatException, IOException {

        BufferedReader br = new BufferedReader(new FileReader("../data/test_db.csv"));
        String line =  null;
        //String word = args[0].toUpperCase();

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0].toUpperCase(),abcd);
        }


        br = new BufferedReader(new FileReader("../data/homophonedb.txt"));

        while((line = br.readLine()) != null){
            String arr[] = line.split(",");
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < arr.length; i++){
                temp.add(arr[i].trim().toUpperCase());
                //System.out.println(arr[i].trim());
            }

            homophonedb.add(temp);
        }
        //System.out.println(System.currentTimeMillis() - init_time);
        //System.out.print(homophonedb);


        br = new BufferedReader(new FileReader("../data/likelihood1.txt"));
        line =  null;
        int counter=0;
        String temp="";

        while((line=br.readLine())!=null){
            if(counter % 2 == 0){
                temp=line;
                /*System.out.println(line);*/
            }
            else{
                line = line.substring(1, line.length()-1);
                String[] keyValuePairs = line.split(",");  
                HashMap<String, Integer> tempvalue = new HashMap<String, Integer> ();               
                for(String pair : keyValuePairs)                
                {
                    String[] entry = pair.split("=");         
                    if (entry.length > 1){
                        tempvalue.put(entry[0].trim().toUpperCase(), Integer.parseInt(entry[1].trim()));
                    }else {
                        System.out.println(entry[0]);
                        continue;
                    }
                }
                likelihood.put(temp.toUpperCase(), tempvalue);
            }
            counter++;
        }


        br = new BufferedReader(new FileReader("../data/trigrams_db.csv"));
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
        	trigrams.put(tristring.toUpperCase(), myarray);	
        }

        br = new BufferedReader(new FileReader("../data/posmap.txt"));
        line = null;
        while ((line = br.readLine()) != null ){
            String arr[] = line.split(" ");
            posmap.put(arr[0].toUpperCase(), arr[1].toUpperCase());
        }

        br = new BufferedReader(new FileReader("../data/colocationtest.txt"));
        line= null;
        int counter1=0;
        String temp1 = "";
        while((line=br.readLine())!=null){
            if(counter1 % 2 == 0){
                temp1=line;
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
                        ColWord worrd = new ColWord(item.toUpperCase());
                        collocate.add(worrd);
                    }
                    int side =Integer.parseInt(eachcolocation[1]);
                    Collocation newcol = new Collocation(collocate,side);
                    Integer nummb = Integer.parseInt(eachcolocation[2]);
                    tempvalue.put(newcol, nummb);  
                }
                collocations.put(temp1.toUpperCase(), tempvalue); 
            }
            counter1++;
        }
        //System.out.println(collocations);


        //SpellCheck1 sp1 = new SpellCheck1(dictionary, trigrams, "belivee");
        //sp1.sortCorrectWords();
        //System.out.println(sp1.getResult());
        //sp1.sortCorrectWords();
        //sp1.printOut();
//        TrainCollocation1 tcol1 = new TrainCollocation1(homophonedb, dictionary, posmap, collocations, "college offers coarse conducted by highly qualified staff");
        //System.out.println(tcol1.getResult());
 //       tcol1.printOut();

        //Phrase p = new Phrase(homophonedb, dictionary, likelihood, "world war piece");
        //p.solve(trigrams);

        Sentence s = new Sentence(homophonedb, dictionary, likelihood, posmap, collocations, "from the moon to the eath");
        s.solve(trigrams);
        //p.printOut();
        //TrainContext1 tc1 = new TrainContext1(dictionary, homophonedb, likelihood, "peace of cake");
        //System.out.println(tc1.getResult());
        /*
           if (sp.isCorrect(word)){
           System.out.println("yes it is a correct word");
           }else {
           sp.kedit(word,2);
           sp.setCorrectWords();
           sp.sortCorrectWords();
           sp.printOut();
           }
           */
        //        sp.kedit(word,2);
        //        sp.setCorrectWords();
        //        sp.sortCorrectWords();
        //        sp.printOut();
    }

}
