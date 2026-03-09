import java.util.*;
import java.io.*;
public class Parser{

    public HashMap<String, TokenType> tokenTable;
    public class Token{

        public TokenType TT;
        public int value_int;
        public float value_float;
        public String name;

        public Token(TokenType TT, int value_int, float value_float, String name){
           this.TT = TT;
           this.value_int = value_int;
           this.value_float = value_float;
           this.name = name;

        }
    }


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
        tokenTable.put("CREATE", TokenType.CREATE);
        tokenTable.put("TABLE", TokenType.TABLE);
        tokenTable.put("VALUES", TokenType.VALUES);
        tokenTable.put("PRIMARY", TokenType.PRIMARY);
        tokenTable.put("KEY", TokenType.KEY);
        tokenTable.put("INT", TokenType.INT);
        tokenTable.put("NULL", TokenType.NULL);
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
        CREATE,
        TABLE,
        VALUES,
        PRIMARY,
        KEY,
        INT,
        NULL,
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

        // Check for Keywords and Punctuation
        TokenType type = tokenTable.get(token);
        // If it is not in the table it is a number(int/float) or an id
        if(type != null){
            return type;
        }

        // Check for Number
        try{

            Integer.parseInt(token);
            return TokenType.INTEGER;

        }catch(NumberFormatException e){

            try {

                Float.parseFloat(token);
                return TokenType.FLOAT;
            } catch (NumberFormatException f) {
                // Not a float either
                return TokenType.IDENTIFIER;
            }


        }
    }

    // Returns a LL of Tokens
    public LinkedList<Token> parse(String file){

        // Create The List of Tokens
        LinkedList<Token> TokenList = new LinkedList<Token>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            StringBuilder sb = new StringBuilder();
            int c;

            // Loops and parses the text
            System.out.println("===== Source Code =====");
            while ((c = reader.read()) != -1){
                System.out.print((char) c);

                if(c == '\n' || c ==' ' || c == '\t'){ // Detect Whitespace

                    // End of Word
                    String lexeme = sb.toString().toUpperCase();
                    if(lexeme.length() == 0) continue;
                    TokenType type = lookup(lexeme);
                    // Construct Node for Token List
                    switch (type){

                        // Error: All Tokens Must Have a Type
                        case null ->{
                            System.out.println("Error Parsing Source Code");
                            System.exit(1);
                        }

                        case TokenType.IDENTIFIER->{
                            Token newToken = new Token(TokenType.IDENTIFIER,0,0.0f, lexeme);
                            TokenList.add(newToken);
                        }

                        case TokenType.INTEGER->{
                            Token newToken = new Token(TokenType.INTEGER, Integer.parseInt(lexeme),0.0f, null);
                            TokenList.add(newToken);
                        }

                        case TokenType.FLOAT -> {
                            Token newToken = new Token(TokenType.FLOAT,0,0.0f, lexeme);
                            TokenList.add(newToken);
                        }
                        default->{
                            Token newToken = new Token(type,0,0.0f, null);
                            TokenList.add(newToken);
                        }



                    }

                    sb.setLength(0);


                    continue;
                }else{ // Add To Buffer
                    sb.append((char) c);
                }
            }

            // Check For the Last Word
            System.out.print('\n');

            for(Token t: TokenList){
                if (t.TT == TokenType.IDENTIFIER){
                    System.out.println(t.TT + " Name: " + t.name );
                } else if (t.TT == TokenType.INTEGER){
                    System.out.println(t.TT + " Value: " + t.value_int);
                }else if (t.TT == TokenType.FLOAT){
                    System.out.println(t.TT + " Value: " + t.value_float);
                } else {
                    System.out.println(t.TT);
                }

            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
