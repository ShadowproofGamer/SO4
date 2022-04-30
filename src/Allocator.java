import java.util.ArrayList;
import java.util.Arrays;

public class Allocator {
    Process[] processes;
    int frames;

    public Allocator(Process[] processes, int frames) {
        this.processes = processes;
        this.frames = frames;
    }

    public void equalAllocation(boolean optimal) {
        Process[] processes = this.processes.clone();
        int[] errorsPerProcess = new int[processes.length];
        Arrays.fill(errorsPerProcess, 0);
        int[] framePerProcess = new int[processes.length];
        Arrays.fill(framePerProcess, (int) Math.floor(frames / processes.length));
        int leftovers = 0;
        for (int i = 0; i < processes.length; i++) {
            leftovers += framePerProcess[i];
        }
        leftovers=frames-leftovers;
        if (optimal) {
            int temp = 0;
            while (leftovers > 0) {
                framePerProcess[temp % framePerProcess.length]++;
                temp++;
                leftovers--;
            }
        }
        for (int i = 0; i < processes.length; i++) {
            LRU lru = new LRU(framePerProcess[i]);
            for (ReferenceSequence rs :
                    processes[i].references) {
                errorsPerProcess[i] += lru.count(rs.reference, framePerProcess[i]);
            }
        }
        int sum = 0;
        for (int i = 0; i < processes.length; i++) {
            sum += errorsPerProcess[i];
        }
        System.out.println("\n\tEqualAllocation");
        System.out.println("framePerProcess: " + Arrays.toString(framePerProcess));
        System.out.println("leftovers: " + leftovers + " Is it optimal: " + optimal);
        System.out.println("EqualAllocation page errors: " + sum + "\nerror of each of the processes:" + Arrays.toString(errorsPerProcess));
        //LRUErrors(processes, framePerProcess, errorsPerProcess);
    }


    private void LRUErrors(Process[] processes, int frames, int[] errorsPerProcess) {
        int j = 0;
        boolean[] anomalies = new boolean[processes.length];
        ArrayList<AnomalyError> ar = new ArrayList<>();
        Arrays.fill(anomalies, false);
        for (int s :
                errorsPerProcess) {
            if (processes[j].pages.length <= frames && s > processes[j].pages.length) {
                anomalies[j] = true;
                ar.add(new AnomalyError(j, processes[j].pages.length, s));
            }
            j++;
        }

        System.out.println("Anomalies (LRU errors): " + Arrays.toString(anomalies));
        System.out.println(ar);
    }

    public void proportionalAllocation(boolean optimal) {
        Process[] processes = this.processes.clone();
        int[] errorsPerProcess = new int[processes.length];
        Arrays.fill(errorsPerProcess, 0);
        int[] framePerProcess = new int[processes.length];
        double needForSpace = 0;
        for (Process p : processes) {
            needForSpace += p.pages.length;
        }
        boolean overflow = !(((double) frames / needForSpace) >= 1);
        for (int i = 0; i < processes.length; i++) {
            framePerProcess[i] = overflow ? (int) Math.floor((processes[i].pages.length * (frames / needForSpace))) : processes[i].pages.length;
            //System.out.println(framePerProcess[i]);
            //System.out.println( ((double)frames/needForSpace)>=1);
            //System.out.println( ((double)frames/needForSpace));
            //System.out.println((int) (processes[i].pages.length*Math.floor(frames/needForSpace)));
        }
        int leftovers = 0;
        for (int counter : framePerProcess) {
            leftovers += counter;
        }
        leftovers=frames-leftovers;

        String bef = "before optimization: "+Arrays.toString(framePerProcess);
        //optimizer delegating all frames that are left:
        if (optimal && overflow) {
            ArrayList<Integer> arr = new ArrayList<>();
            while (leftovers > 0) {
                int min=9999;
                int index=0;
                for (int i = 0; i < framePerProcess.length; i++) {
                    if (framePerProcess[i]<min && !arr.contains(i)){
                        min=framePerProcess[i];
                        index=i;
                    }
                }
                framePerProcess[index]++;
                arr.add(index);
                leftovers--;
            }
        }

        for (int i = 0; i < processes.length; i++) {
            LRU lru = new LRU(framePerProcess[i]);
            for (ReferenceSequence rs :
                    processes[i].references) {
                errorsPerProcess[i] += lru.count(rs.reference, framePerProcess[i]);
            }
        }
        int sum = 0;
        for (int s : errorsPerProcess) {
            sum += s;
        }
        System.out.println("\n\tProportionalAllocation");
        System.out.println(bef);
        System.out.println("framePerProcess: " + Arrays.toString(framePerProcess));
        System.out.println("leftovers: " + leftovers + " Is it optimal: " + optimal);
        System.out.println("EqualAllocation page errors: " + sum + "\nerror of each of the processes:" + Arrays.toString(errorsPerProcess));


    }


}
