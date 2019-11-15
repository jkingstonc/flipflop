package jameskingstonclarke.flipflop;

public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer("F:/OneDrive - Lancaster University/programming/java/flipflop/test.ff");
        Machine m = new Machine(
                lexer.lex()
        );
        m.run();
    }
}
