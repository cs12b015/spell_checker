import java.io.*;
import java.util.*;
import java.math.*;

public class Test {
    public static void main(String[] args){
        String str = "dharani";
        for (int i = 0; i < str.length()-2; i++){
            System.out.println(str.substring(i,i+3));
        }
    }
}
