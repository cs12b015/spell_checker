import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class TrainCollocation{

    public static ArrayList<ArrayList<String>> homophonedb = new ArrayList<ArrayList<String>>();

    public static HashMap<String, HashMap<Collocation, Integer>> collocations = new HashMap<String, HashMap<Collocation, Integer>>();
    
    public static void main(String[] args) throws NumberFormatException, IOException {
        
        BufferedReader br = new BufferedReader(new FileReader("homophonedb.txt"));
        String line =  null;

        while((line = br.readLine()) != null){
            String arr[] = line.split(",");
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < arr.length; i++){
                temp.add(arr[i].trim());
                //System.out.println(arr[i].trim());
            }

            homophonedb.add(temp);
        }

        br = new BufferedReader(new FileReader("w5c.txt"));

        while((line=br.readLine())!=null){
            String arr[] = line.split("\t");

            for (int i = 1; i < 6; i++){
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

        System.out.println(collocations);

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
