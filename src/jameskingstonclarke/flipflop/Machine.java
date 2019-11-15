package jameskingstonclarke.flipflop;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Machine {


    // Precidence (0 -highest)
    // 0 - flip
    // 0 - flop
    // 1 - memory
    // 1 - pointer

    private byte[] memory;
    private ArrayList<Lexer.Token> tokens;
    private int pc, pointer;
    private Stack loop_stack;

    public Machine(ArrayList<Lexer.Token>  tokens){
        this.memory = new byte[2^8];
        this.tokens = tokens;
        this.pc = 0;
        this.pointer = 0;
        this.loop_stack = new Stack();
    }

    public void run(){
        while(tokens.get(pc) != Lexer.Token.END){
            switch(tokens.get(pc)){
                case PRINT:{
                    System.out.println(memory[pointer]);
                    this.pc++;
                    break;
                }
                case INPUT:{
                    memory[pointer]=new Scanner(System.in).nextByte();
                    this.pc++;
                    break;
                }
                case POINTER:
                case MEMORY:{
                    pointermemory();
                    break;
                }
                case LOOP:{
                    loop_stack.push(this.pc);
                    this.pc++;
                    break;
                }
                case ENDLOOP:{
                    if (memory[pointer]>0)
                        pc=(int)loop_stack.pop();
                    else
                        pc++;
                    break;
                }
                default:{
                    throw new RuntimeException("Unexpected symbol: "+this.tokens.get(this.pc));
                }
            }
        }
        System.out.println("done!");
    }

    private void pointermemory(){
        Lexer.Token operator = consume();
        flipflop(operator);
    }

    private byte flipflop(Lexer.Token operator) {
        Lexer.Token ff = consume();
        byte value;
        // Recursiveness
        if(expect(Lexer.Token.FLIP) || expect(Lexer.Token.FLOP)){
            value = flipflop(operator);
        }else{
            if(ff == Lexer.Token.FLIP)
                value=(byte)1;
            else if(ff == Lexer.Token.FLOP)
                value=(byte)-1;
            else
                throw new RuntimeException("Unexpected token: "+ff);
        }
        if(operator== Lexer.Token.MEMORY)
            memory[pointer]+=value;
        else if(operator == Lexer.Token.POINTER)
            pointer+=value;
        return memory[pointer];
    }

    private boolean expect(Lexer.Token token){
        Lexer.Token t = tokens.get(this.pc);
        if(t != token)
            return false;
        return true;
    }

    private Lexer.Token consume(){
        Lexer.Token t = tokens.get(this.pc);
        this.pc++;
        return t;
    }

    private Lexer.Token consume(Lexer.Token token, String msg){
        Lexer.Token t = tokens.get(this.pc);
        if(t != token)
            throw new RuntimeException(msg);
        this.pc++;
        return t;
    }
}