import java.util.*;
import java.io.*;
public class Parser{


    public Parser(){

    }

    public List<Character> parse(String file){

        try(BufferedReader reader = new BufferedReader(new FileReader("file.txt"))){
            int c;
            while ((c = reader.read()) != -1){
                System.out.print((char) c);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
