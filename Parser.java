import java.util.*;
import java.io.*;
public class Parser{

    public HashMap<String, TokenType> tokenTable;
    public class Token{

        public TokenType TT;
        public int value_int;
        public float value_float;
        public String name;

        public Token(TokenType TT, int value_int, float valude_float, String name){
           this.TT = TT;
           this.value_int = value_int;
           this.value_float = value_float;
           this.name = name;

        }
    }



    // The Constructor was here
    public Parser(){

        // Populate Lookup Table
        tokenTable = new HashMap<String, TokenType>();
        tokenTable.put("SELECT", TokenType.SELECT);
        tokenTable.put("FROM", TokenType.FROM);
        tokenTable.put("WHERE", TokenType.WHERE);
        tokenTable.put("INSERT", TokenType.INSERT);
        tokenTable.put("INTO", TokenType.INTO);
        tokenTable.put("UPDATE", TokenType.UPDATE);
        tokenTable.put("DELETE", TokenType.DELETE);
        tokenTable.put("DROP", TokenType.DROP);
        tokenTable.put("ALTER", TokenType.ALTER);
        tokenTable.put("TABLE", TokenType.TABLE);
        tokenTable.put("AND", TokenType.AND);
        tokenTable.put("OR", TokenType.OR);
        tokenTable.put("NOT", TokenType.NOT);
        tokenTable.put("ORDER", TokenType.ORDER);
        tokenTable.put("BY", TokenType.BY);
        tokenTable.put(",", TokenType.COMMA);
        tokenTable.put(";", TokenType.SEMICOLON);
        tokenTable.put("(", TokenType.LPAREN);
        tokenTable.put(")", TokenType.RPAREN);
        tokenTable.put(".", TokenType.PERIOD);
        tokenTable.put("*", TokenType.ASTERISK);

    }

    // Token List
    public enum TokenType{
        INTEGER,
        FLOAT,
        STRING,
        IDENTIFIER,
        SELECT,
        FROM,
        WHERE,
        INSERT,
        INTO,
        UPDATE,
        DELETE,
        DROP,
        ALTER,
        TABLE,
        AND,
        OR,
        NOT,
        ORDER,
        BY,
        COMMA,
        SEMICOLON,
        LPAREN,
        RPAREN,
        PERIOD,
        ASTERISK,
        EOF
    }

    public TokenType lookup(String token){


        return null;
    }

    // Returns a LL of Tokens
    public LinkedList<Token> parse(String file){

        // Create The List of Tokens
        LinkedList<Token> TokenList = new LinkedList<Token>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            StringBuilder sb = new StringBuilder();
            int c;

            // Loops and parses the text
            while ((c = reader.read()) != -1){


                if(c == '\n' || c ==' ' || c == '\t'){ // Detect Whitespace
                    String lexeme = sb.toString().toUpperCase();
                    System.out.println("Index is : " + tokenTable.get(lexeme));

                    // if (lexeme.equals("SELECT")){
                    //     System.out.println(" SELECT TOKEN DETECTED");
                    //     TokenList.add(new Token(TokenType.SELECT, 0,0,null));
                    // }


                    System.out.print('\n');
                    sb.setLength(0);
                    continue;
                }else{ // Add To Buffer
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
