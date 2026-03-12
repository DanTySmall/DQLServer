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
        tokenTable.put("CREATE", TokenType.CREATE);
        tokenTable.put("TABLE", TokenType.TABLE);
        tokenTable.put("VALUES", TokenType.VALUES);
        tokenTable.put("PRIMARY", TokenType.PRIMARY);
        tokenTable.put("KEY", TokenType.KEY);
        tokenTable.put("INT", TokenType.INT);
        tokenTable.put("VARCHAR", TokenType.VARCHAR);
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
        tokenTable.put("=", TokenType.EQUALS);

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
        VARCHAR,
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
        EQUALS,
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

                // Check if character is punctuation that should be a separate token
                boolean isPunctuation = (c == ',' || c == ';' || c == '(' || c == ')' ||
                                        c == '.' || c == '*' || c == '=');

                if (c == '\n' || c == ' ' || c == '\t' || isPunctuation){ // Detect Whitespace and Punctuation

                    // Process any buffered word first
                    String lexeme = sb.toString().toUpperCase();
                    if(lexeme.length() > 0) {
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
                    }

                    // Now handle the punctuation character itself
                    if (isPunctuation) {
                        String punctStr = String.valueOf((char) c);
                        TokenType punctType = lookup(punctStr);
                        if (punctType != null) {
                            Token newToken = new Token(punctType,0,0.0f, null);
                            TokenList.add(newToken);
                        }
                    }

                    continue;
                }else{ // Add To Buffer
                    sb.append((char) c);
                }
            }

            // Check For the Last Word
            System.out.print('\n');


        } catch (IOException e){
            e.printStackTrace();
        }
        syntax(TokenList);
        return null;
    }

    // Detecting Grammar
    public void syntax(LinkedList<Token> TokenList){

        System.out.println("===== Syntax Construction=====");

        switch (TokenList.getFirst().TT){
            case SELECT -> selectStatement(TokenList);
            case CREATE -> createTableStatement(TokenList);
            case DROP   -> dropTableStatement(TokenList);
            case INSERT -> insertStatement(TokenList);
            case DELETE -> deleteStatement(TokenList);
            default -> {
                System.out.println("Error: Unknown Statement");
                System.exit(1);
            }
        }
    }

    public void printTokens(LinkedList<Token> TokenList){

        // Print all the Tokens
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

    }

    public void selectStatement(LinkedList<Token> TokenList){

        printTokens(TokenList);

        // Remove The Select Keyword
        TokenList.removeFirst();

        // Gathering Columns
        ArrayList<String> columns = new ArrayList<String>();
        if (TokenList.getFirst().TT != TokenType.IDENTIFIER && TokenList.getFirst().TT != TokenType.ASTERISK ) {

            System.out.println("Error Detecting Column");
            System.exit(1);

        }

        Token t = TokenList.getFirst();

        if (t.TT == TokenType.ASTERISK) {
            // This is Going to take all columns
            System.out.println();
            System.out.println("Taking all Columns");
        } else {

            while (t.TT == TokenType.IDENTIFIER) {

                columns.add(t.name);

                if (t.TT == TokenType.COMMA) {
                    TokenList.removeFirst();
                    t = TokenList.getFirst();

                }

                TokenList.removeFirst();
                t = TokenList.getFirst();
            }

            System.out.println();
            System.out.print("Taking columns ");
            for (String s : columns) {
                System.out.print(s + " ");
            }
        }

        // From
        if (t.TT != TokenType.FROM) {
            System.out.println("Error: FROM Clause Not Detected");
            System.exit(1);
        }


        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.IDENTIFIER) {
            System.out.println("Error: Table Not Detected");
            System.exit(1);

        }

        String table;
        table = t.name;
        System.out.println("From Table " + table);
        TokenList.removeFirst();
        t = TokenList.getFirst();

        // Optional WHERE clause
        if (t.TT == TokenType.WHERE){
            TokenList.removeFirst();
            t = TokenList.getFirst();
            parseWhere(TokenList);
            t = TokenList.getFirst();
        }

        if (t.TT != TokenType.SEMICOLON) {
            System.out.println("Error: ; not detected");
            System.exit(1);
        }

        try{
            TokenList.removeFirst();
            t = TokenList.getFirst();
        }catch(Exception e){
        }

    }

    public void createTableStatement(LinkedList<Token> TokenList){

        printTokens(TokenList);

        // Remove CREATE keyword
        TokenList.removeFirst();
        Token t = TokenList.getFirst();

        // Expect TABLE keyword
        if (t.TT != TokenType.TABLE){
            System.out.println("Error: TABLE not detected");
            System.exit(1);
        }

        TokenList.removeFirst();
        t = TokenList.getFirst();

        // Expect table name
        if (t.TT != TokenType.IDENTIFIER){
            System.out.println("Error: Table name not detected");
            System.exit(1);
        }

        String tableName = t.name;
        System.out.println("\nCreating table: " + tableName);
        TokenList.removeFirst();
        t = TokenList.getFirst();

        // Expect opening parenthesis
        if (t.TT != TokenType.LPAREN){
            System.out.println("Error: ( not detected");
            System.exit(1);
        }

        TokenList.removeFirst();
        t = TokenList.getFirst();

        // Parse column definitions until closing parenthesis
        ArrayList<String> columns = new ArrayList<String>();

        while (t.TT != TokenType.RPAREN){

            // Handle standalone PRIMARY KEY (col) clause
            if (t.TT == TokenType.PRIMARY){
                TokenList.removeFirst();
                t = TokenList.getFirst();

                if (t.TT != TokenType.KEY){
                    System.out.println("Error: KEY not detected after PRIMARY");
                    System.exit(1);
                }

                TokenList.removeFirst();
                t = TokenList.getFirst();

                if (t.TT != TokenType.LPAREN){
                    System.out.println("Error: ( not detected after PRIMARY KEY");
                    System.exit(1);
                }

                TokenList.removeFirst();
                t = TokenList.getFirst();

                if (t.TT != TokenType.IDENTIFIER){
                    System.out.println("Error: Column name not detected in PRIMARY KEY");
                    System.exit(1);
                }

                System.out.println("Primary Key: " + t.name);
                TokenList.removeFirst();
                t = TokenList.getFirst();

                if (t.TT != TokenType.RPAREN){
                    System.out.println("Error: ) not detected after PRIMARY KEY column");
                    System.exit(1);
                }

                TokenList.removeFirst();
                t = TokenList.getFirst();

                if (t.TT == TokenType.COMMA){
                    TokenList.removeFirst();
                    t = TokenList.getFirst();
                }

                continue;
            }

            // Expect column name
            if (t.TT != TokenType.IDENTIFIER){
                System.out.println("Error: Column name not detected");
                System.exit(1);
            }

            String colName = t.name;
            TokenList.removeFirst();
            t = TokenList.getFirst();

            // Expect data type (e.g. INT, VARCHAR)
            if (t.TT != TokenType.INT && t.TT != TokenType.VARCHAR){
                System.out.println("Error: Data type not detected for column " + colName);
                System.exit(1);
            }

            String colType = t.TT == TokenType.INT ? "INT" : "VARCHAR";
            columns.add(colName);
            System.out.println("Column: " + colName + " Type: " + colType);
            TokenList.removeFirst();
            t = TokenList.getFirst();

            // Optional inline PRIMARY KEY
            if (t.TT == TokenType.PRIMARY){
                TokenList.removeFirst();
                t = TokenList.getFirst();

                if (t.TT != TokenType.KEY){
                    System.out.println("Error: KEY not detected after PRIMARY");
                    System.exit(1);
                }

                System.out.println("Column " + colName + " is PRIMARY KEY");
                TokenList.removeFirst();
                t = TokenList.getFirst();
            }

            // Comma separates column definitions
            if (t.TT == TokenType.COMMA){
                TokenList.removeFirst();
                t = TokenList.getFirst();
            }
        }

        System.out.print("Columns: ");
        for (String col : columns){
            System.out.print(col + " ");
        }
        System.out.println();

        // Remove closing parenthesis
        TokenList.removeFirst();
        t = TokenList.getFirst();

        // Expect semicolon
        if (t.TT != TokenType.SEMICOLON){
            System.out.println("Error: ; not detected");
            System.exit(1);
        }

        try {
            TokenList.removeFirst();
        } catch (Exception e){
            // End of token list
        }

    }

    // Parses WHERE col = val [AND/OR col = val ...]
    public void parseWhere(LinkedList<Token> TokenList){

        Token t = TokenList.getFirst();

        while (t.TT != TokenType.SEMICOLON){

            // Expect column name
            if (t.TT != TokenType.IDENTIFIER){
                System.out.println("Error: Column name expected in WHERE clause");
                System.exit(1);
            }
            String col = t.name;
            TokenList.removeFirst();
            t = TokenList.getFirst();

            // Expect =
            if (t.TT != TokenType.EQUALS){
                System.out.println("Error: = expected in WHERE clause");
                System.exit(1);
            }
            TokenList.removeFirst();
            t = TokenList.getFirst();

            // Expect value (int, float, or identifier)
            if (t.TT == TokenType.INTEGER){
                System.out.println("WHERE " + col + " = " + t.value_int);
            } else if (t.TT == TokenType.FLOAT){
                System.out.println("WHERE " + col + " = " + t.name);
            } else if (t.TT == TokenType.IDENTIFIER){
                System.out.println("WHERE " + col + " = " + t.name);
            } else {
                System.out.println("Error: Value expected in WHERE clause");
                System.exit(1);
            }
            TokenList.removeFirst();
            t = TokenList.getFirst();

            // Optional AND / OR to chain conditions
            if (t.TT == TokenType.AND || t.TT == TokenType.OR){
                System.out.println(t.TT);
                TokenList.removeFirst();
                t = TokenList.getFirst();
            } else {
                break;
            }
        }
    }

    // DROP TABLE name ;
    public void dropTableStatement(LinkedList<Token> TokenList){

        printTokens(TokenList);

        // Remove DROP keyword
        TokenList.removeFirst();
        Token t = TokenList.getFirst();

        if (t.TT != TokenType.TABLE){
            System.out.println("Error: TABLE not detected");
            System.exit(1);
        }

        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.IDENTIFIER){
            System.out.println("Error: Table name not detected");
            System.exit(1);
        }

        String tableName = t.name;
        System.out.println("\nDropping table: " + tableName);
        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.SEMICOLON){
            System.out.println("Error: ; not detected");
            System.exit(1);
        }

        System.out.println("Table " + tableName + " has been dropped successfully");

        try {
            TokenList.removeFirst();
        } catch (Exception e){
        }
    }

    // INSERT INTO name VALUES ( val1 , val2 , ... ) ;
    public void insertStatement(LinkedList<Token> TokenList){

        printTokens(TokenList);

        // Remove INSERT keyword
        TokenList.removeFirst();
        Token t = TokenList.getFirst();

        if (t.TT != TokenType.INTO){
            System.out.println("Error: INTO not detected");
            System.exit(1);
        }

        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.IDENTIFIER){
            System.out.println("Error: Table name not detected");
            System.exit(1);
        }

        String tableName = t.name;
        System.out.println("\nInserting into table: " + tableName);
        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.VALUES){
            System.out.println("Error: VALUES not detected");
            System.exit(1);
        }

        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.LPAREN){
            System.out.println("Error: ( not detected");
            System.exit(1);
        }

        TokenList.removeFirst();
        t = TokenList.getFirst();

        // Parse values
        ArrayList<String> values = new ArrayList<String>();

        while (t.TT != TokenType.RPAREN){

            if (t.TT == TokenType.INTEGER){
                values.add(String.valueOf(t.value_int));
            } else if (t.TT == TokenType.FLOAT){
                values.add(t.name);
            } else if (t.TT == TokenType.IDENTIFIER){
                values.add(t.name);
            } else {
                System.out.println("Error: Value expected");
                System.exit(1);
            }

            TokenList.removeFirst();
            t = TokenList.getFirst();

            if (t.TT == TokenType.COMMA){
                TokenList.removeFirst();
                t = TokenList.getFirst();
            }
        }

        System.out.print("Values: ");
        for (String v : values){
            System.out.print(v + " ");
        }
        System.out.println();

        // Remove closing paren
        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.SEMICOLON){
            System.out.println("Error: ; not detected");
            System.exit(1);
        }

        try {
            TokenList.removeFirst();
        } catch (Exception e){
        }
    }

    // DELETE FROM name [WHERE col = val] ;
    public void deleteStatement(LinkedList<Token> TokenList){

        printTokens(TokenList);

        // Remove DELETE keyword
        TokenList.removeFirst();
        Token t = TokenList.getFirst();

        if (t.TT != TokenType.FROM){
            System.out.println("Error: FROM not detected");
            System.exit(1);
        }

        TokenList.removeFirst();
        t = TokenList.getFirst();

        if (t.TT != TokenType.IDENTIFIER){
            System.out.println("Error: Table name not detected");
            System.exit(1);
        }

        String tableName = t.name;
        System.out.println("\nDeleting from table: " + tableName);
        TokenList.removeFirst();
        t = TokenList.getFirst();

        // Optional WHERE clause
        boolean hasWhere = false;
        if (t.TT == TokenType.WHERE){
            hasWhere = true;
            TokenList.removeFirst();
            t = TokenList.getFirst();
            parseWhere(TokenList);
            t = TokenList.getFirst();
        }

        if (t.TT != TokenType.SEMICOLON){
            System.out.println("Error: ; not detected");
            System.exit(1);
        }

        if (hasWhere){
            System.out.println("Rows matching WHERE condition deleted from " + tableName);
        } else {
            System.out.println("All rows deleted from table " + tableName);
        }

        try {
            TokenList.removeFirst();
        } catch (Exception e){
        }
    }
}
