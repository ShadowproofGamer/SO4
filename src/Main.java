import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Generator gen = new Generator();
        Process[] proc = gen.generateProcess();
        int[] pagesPerProcess= new int[proc.length];
        int i=1;
        for (Process p : proc) {
            System.out.println(i+". "+ p);
            pagesPerProcess[i-1]=p.pages.length;
            i++;
        }
        System.out.println("Pages per process: "+Arrays.toString(pagesPerProcess));
        int frames = 80;
        Allocator allocator = new Allocator(proc.clone(), frames);

        allocator.equalAllocation(true);
        allocator.proportionalAllocation(true);
    }
}
