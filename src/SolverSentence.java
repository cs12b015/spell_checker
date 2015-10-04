import java.io.*;
import java.util.*;
import java.math.*;

public class SolverSentence {


    public static HashMap<String, BigInteger> dictionary = new HashMap<String, BigInteger>();
    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();
    public static HashMap<String, HashMap<String, Integer>> likelihood = new HashMap<String, HashMap<String, Integer>>();
    public static Map<String, ArrayList<String>> trigrams = new HashMap<String, ArrayList<String>>();
    public static Map<String, String> posmap = new HashMap<String, String>();
    public static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();
    public static HashMap<String, BigInteger> final_result = new HashMap<String, BigInteger>();
    public static String wrong_line;


    public static void main(String[] args) throws NumberFormatException, IOException {

        BufferedReader br = new BufferedReader(new FileReader("../data/test_db.csv"));
        String line =  null;
        //String word = args[0].toUpperCase();

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");
            BigInteger abcd = new BigInteger(arr[1]);
            dictionary.put(arr[0],abcd);
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


        br = new BufferedReader(new FileReader("../data/likelihood.txt"));
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
                    tempvalue.put(entry[0].trim().toUpperCase(), Integer.parseInt(entry[1].trim()));
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
            trigrams.put(tristring, myarray);	
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
                        ColWord worrd = new ColWord(item);
                        collocate.add(worrd);
                    }
                    int side =Integer.parseInt(eachcolocation[1]);
                    Collocation newcol = new Collocation(collocate,side);
                    Integer nummb = Integer.parseInt(eachcolocation[2]);
                    tempvalue.put(newcol, nummb);  
                }
                collocations.put(temp1, tempvalue); 
            }
            counter1++;
        }

        br = new BufferedReader(new FileReader("../data/sentencechecktest.txt"));
        line=null;
        while((line=br.readLine())!=null){
            line=line.toUpperCase();
            wrong_line = line.toUpperCase();
            String[] array = line.split(" ");
            for(int i=0;i<array.length;i++){
                if(!dictionary.containsKey(array[i]))
                {
                    SpellCheck1 sp1 = new SpellCheck1(dictionary, trigrams, array[i]);
                    //sp1.sortCorrectWordsBySoundex();
                    sp1.sortCorrectWords();
                    //sp1.printOut();
                    array[i]=sp1.getResult().get(0);
                }
            }
            String newline = "";
            for(int i=0;i<array.length;i++){
                newline=newline+" "+array[i];
            }
            //System.out.println(newline.trim());
            TrainContext1 tc1 = new TrainContext1(dictionary, homophonedb, likelihood, newline.trim());
            //System.out.println(tc1.getResult());
            HashMap<String, BigInteger> tc1_result = tc1.getResult();

            TrainCollocation1 tcol1 = new TrainCollocation1(homophonedb, dictionary, posmap, collocations, newline.trim());
            Map<String, Integer> tcol1_result = tcol1.getResult();

            Iterator it = tc1_result.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                if (tcol1_result.containsKey((String)pair.getKey())){
                    BigInteger pair_value = (BigInteger)pair.getValue();
                    final_result.put((String)pair.getKey(), pair_value.add(BigInteger.valueOf(tcol1_result.get(pair.getKey()))));
                }else {
                    final_result.put((String)pair.getKey(), (BigInteger)pair.getValue());
                }
                it.remove();
            }

            printOut();
        }
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

    public static void printOut(){
        String arr[] = wrong_line.split(" ");

        for (int i = 0; i < arr.length; i++){
            if (isAmbiguous(arr[i])){
                ArrayList<String> temp_words = getAmbiguousWords(arr[i]);                
                BigInteger prev_best = BigInteger.valueOf(0);
                for (String word : temp_words){
                    if (final_result.get(word).compareTo(prev_best) > 0){
                        prev_best = final_result.get(word);
                        arr[i] = word;
                    }
                }
            }
        }

        String new_line = "";
        for (int i = 0; i < arr.length; i++){
            new_line = new_line + " " + arr[i];
        }

        System.out.println(wrong_line + "   " + new_line.trim());
    }
}

