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
        this.pos = pos;
        this.word = "";
        this.isWord = false;
    }

    //Constructor when both the word and pos are given
    public ColWord(String colWord, String pos){
        this.word = colWord;
        this.pos = pos;
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
        if (this.pos != colWord.getPos()){
            return false;
        }else {
            return true;
        }
    }
}
