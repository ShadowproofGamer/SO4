import java.util.Arrays;

public class EqualAllocation {
    public static int equalAllocation(Process[] processes, int frames){
        int[] errorsPerProcess = new int[processes.length];
        Arrays.fill(errorsPerProcess, 0);
        int framePerProcess = (int) Math.floor(frames / processes.length);
        for (int i = 0; i < processes.length; i++) {
            LRU lru = new LRU(framePerProcess);
            for (ReferenceSequence rs :
                    processes[i].references) {
                errorsPerProcess[i] += lru.count(rs.reference, framePerProcess);
            }
        }
        int sum = 0;
        for (int s :
                errorsPerProcess) {
            sum+=s;
        }
        System.out.println(framePerProcess);
        System.out.println("EqualAllocation: "+sum+"\neach of the processes error:"+ Arrays.toString(errorsPerProcess));

        return sum;
    }
}
