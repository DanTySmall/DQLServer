import java.util.*;
import java.io.*;
public class Parser{


    public Parser(){

    }

    public List<Character> parse(String file){

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            StringBuilder sb = new StringBuilder();
            int c;

            // Loops and parses the text
            while ((c = reader.read()) != -1){

                // Detect Whitespace
                if(c == '\n' || c ==' ' || c == '\t'){
                    String lexeme = sb.toString().toUpperCase();
                    if (lexeme.equals("SELECT")){
                        System.out.println("SELECT TOKEN DETECTED");
                    }
                    System.out.print('\n');
                    sb.setLength(0);
                    continue;
                }else{
                    sb.append((char) c);
                }
                System.out.print((char) c);
            }

            // Check For the Last Word
            System.out.print('\n');


        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
