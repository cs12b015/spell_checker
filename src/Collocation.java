import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;
//import org.apache.commons.lang3.builder;

public class Collocation{
    
    //Save each collocation as key with its POS tag as value.
    //Value will be a null string if key itself is a POS tag.
    //The order of hashmap keys is the collocation order.
    private ArrayList<ColWord> collocation;

    //This variable says whether the collocation belongs to the left side of the word or the right.
    //left = 0; right = 1;
    private int side;

    //The size of the collocation.
    private int size;

    //Constructor for collocation without side specified
    //Side is assumed to be left by default
    public Collocation(ArrayList<ColWord> collocation){
        this.collocation = collocation;
        this.size = collocation.size();
        this.side = 0;
    }

    //Constructor for collocation with side specified
    public Collocation(ArrayList<ColWord> collocation, int side){
        this.collocation = collocation;
        this.size = collocation.size();
        this.side = side;
    }

    //Get method for side of collocation
    public int getSide(){
        return this.side;
    }

    //Get method for size of collocation
    public int getSize(){
        return this.size;
    }
    
    public ArrayList<ColWord> getCollocation(){
    	return this.collocation;
    }

    //Get method for colWord in a collocation, given an index
    public ColWord getColWord(int i){
        return this.collocation.get(i);
    }

    @Override
    public boolean equals(Object obj){

        if (!(obj instanceof Collocation)) return false;
        if (obj == this) return true;

    	Collocation c = (Collocation)obj;
    	if(this.side != c.getSide())
    		return false;
    	else{
            if (this.size != c.getSize()){
                return false;
            }else {
                for (int i = 0; i < this.size; i++){
                	if (!this.collocation.get(i).equals(c.getColWord(i))){
                		return false;
                	}
                }
                return true;
            }
    	}
    }

    @Override
    public int hashCode(){
        return 1;
    }
    
    //This method says whether this collocation has any conflicts with the given collocation c.
    public boolean hasConflict(Collocation c){
        //Check if both the collocations lie on the same side of the word.
        //If no, then there is no point in checking for conflict. Else, check.
        if (this.side != c.getSide()){
           // System.out.println("The given collocations are not on same side");
           // printOut();
           // c.printOut();
           // System.out.println("Onto the next now...");
            return false;
        }else {
            //Check for the smaller collocation.
            int small_size = 0;
            if (this.size < c.getSize()){
                small_size = this.size;
            }else {
                small_size = c.getSize();
            }

            int conflict_size = 0;
            //If the collocations are on the left side, do a bottom up
            if (this.side == 0){
                for (int i = 1; i <= small_size; i++){
                    if (this.collocation.get(this.size-i).matches(c.getColWord(c.getSize()-i))){
                        conflict_size++;
                    }else {
                        continue;
                    }
                }
            }else {
                for (int i = 0; i < small_size; i++){
                    if (this.collocation.get(i).matches(c.getColWord(i))){
                        conflict_size++;
                    }else {
                        continue;
                    }
                }
            }

            if (conflict_size == small_size) return true;
            else{
                //System.out.println("The collocations do not match perfectly");
                //printOut();
                //c.printOut();
                //System.out.println("Onto the next now...");
             return false;
            }
        }
    }

    public void printOut(){
        for (int i = 0; i < collocation.size(); i++){
            System.out.println(collocation.get(i).getPos());
        }
    }
}
