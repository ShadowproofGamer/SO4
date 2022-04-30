import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Generator gen = new Generator();
        Process[] proc = gen.generateProcess();
        for (Process p :
                proc) {
            System.out.println(p);
        }
    }
}
