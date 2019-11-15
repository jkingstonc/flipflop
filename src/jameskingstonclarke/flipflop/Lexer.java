package jameskingstonclarke.flipflop;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Lexer {
    public enum Token{
        END,
        MEMORY,
        POINTER,
        FLIP,
        FLOP,
        LOOP,
        ENDLOOP,
        PRINT,
        INPUT,
    }

    private String program;
    private int pointer;
    private ArrayList<Token> tokens;

    public Lexer(String file){
        try{
            this.program = Files.readString(Paths.get(file), StandardCharsets.US_ASCII);
        }catch(IOException e){
            e.printStackTrace();
        }
        this.pointer = 0;
        this.tokens = new ArrayList<>();
    }

    public ArrayList<Token> lex(){
        while(pointer<program.length()){
            char c = program.charAt(pointer);
            pointer++;
            switch(c){
                case ' ':
                case '\n':
                case '\t':
                    break;
                case '[':
                    tokens.add(Token.LOOP); break;
                case ']':
                    tokens.add(Token.ENDLOOP); break;
                case '>':
                    tokens.add(Token.PRINT); break;
                case '<':
                    tokens.add(Token.INPUT); break;
                case '#':
                    tokens.add(Token.POINTER); break;
                case '@':
                    tokens.add(Token.MEMORY); break;
                case 'f':{
                    String s = ""+c;
                    char next;
                    while(pointer<program.length()){
                        s+=program.charAt(pointer);
                        pointer++;
                        if(s.equals("flip")){
                            tokens.add(Token.FLIP);
                            break;
                        }else if(s.equals("flop")) {
                            tokens.add(Token.FLOP);
                            break;
                        }
                    }
                    break;
                }
                default:
                    throw new RuntimeException("Unexpected symbol '"+c+"'");
            }
        }
        tokens.add(Token.END);
        return tokens;
    }
}
