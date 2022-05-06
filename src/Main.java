import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Generator gen = new Generator();
        Process[] proc = gen.generateProcess();

        details(proc);
        int frames = 85;
        Allocator allocator = new Allocator(proc.clone(), frames);

        allocator.equalAllocation(true);
        allocator.proportionalAllocation(true);
        allocator.zoneModelAllocation(true);
        allocator.errorManagementAllocation(true);

    }
    private static void details(Process[] proc){
        int[] pagesPerProcess= new int[proc.length];
        int i=1;
        for (Process p : proc) {
            System.out.println(i+". "+ p);
            pagesPerProcess[i-1]=p.pages.length;
            i++;
        }
        System.out.println("Pages per process: "+Arrays.toString(pagesPerProcess));
    }
}
