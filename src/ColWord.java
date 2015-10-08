import java.lang.*;
import java.io.*;
import java.util.*;
import java.math.*;

public class ColWord{
    
    //A word or POS tag part of the collocation
    private String word;

    //Whether this colWord is a word or POS tag
    private boolean isWord;

    //If it is a word, it's POS tag
    private String pos;

    //Constructor when only the pos is given
    //POS ColWords have word as an empty string
    public ColWord(String pos){
        this.pos = pos.toUpperCase();
        this.word = "";
        this.isWord = false;
    }

    //Constructor when both the word and pos are given
    public ColWord(String colWord, String pos){
        this.word = colWord.toUpperCase();
        this.pos = pos.toUpperCase();
        this.isWord = true;
    }

    //Get method for word
    public String getWord(){
        return this.word;
    }

    //Get method for POS
    public String getPos(){
        return this.pos;
    }

    //Method to check if this ColWord is a word or POS tag
    public boolean isWord(){
        return this.isWord;
    }

    //Method to check if this ColWord conflicts with another ColWord
    public boolean matches(ColWord colWord){
        //System.out.println("Comparing the words: " + pos + " and " + colWord.getPos());
        if (this.pos.trim().equals(colWord.getPos().trim())){
            //System.out.println("Returning true");
            return true;
        }else {
            //System.out.println("Returning false");
            return false;
        }
    }
    
    @Override
    public boolean equals(Object obj){

        if (!(obj instanceof ColWord)) return false;
        if (obj == this) return true;

    	ColWord c = (ColWord)obj;
    	if(this.pos != c.pos){
    		return false;
    	}else{
            if (this.word != c.getWord()){
                return false;
            }else {
                return true;
            }
    	}
    }
    
    @Override
    public int hashCode(){
    	return 2;
    }

}
