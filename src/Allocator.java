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
        leftovers = frames - leftovers;
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
                //
                //System.out.println(Arrays.toString(errorsPerProcess));
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
        leftovers = frames - leftovers;

        String bef = "before optimization: " + Arrays.toString(framePerProcess);
        //optimizer delegating all frames that are left:
        if (optimal && overflow) {
            ArrayList<Integer> arr = new ArrayList<>();
            while (leftovers > 0) {
                double min = 9999;
                int index = 0;
                for (int i = 0; i < framePerProcess.length; i++) {
                    if (((double) framePerProcess[i] / (double) processes[i].pages.length) < min && !arr.contains(i)) {
                        min = framePerProcess[i];
                        index = i;
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
        System.out.println("framePerProcess: \t" + Arrays.toString(framePerProcess));
        System.out.println("leftovers: " + leftovers + " Is it optimal: " + optimal);
        System.out.println("EqualAllocation page errors: " + sum + "\nerror of each of the processes:" + Arrays.toString(errorsPerProcess));


    }

    public void zoneModelAllocation(boolean optimal) {
        int delta = 20;
        Process[] processes = this.processes.clone();
        int[] errorsPerProcess = new int[processes.length];
        Arrays.fill(errorsPerProcess, 0);
        int[] framePerProcess = new int[processes.length];


        int leftovers = recalc(processes, framePerProcess, delta, optimal);

        MultithreadingLRU lru = new MultithreadingLRU(framePerProcess.clone());
        for (int i = 0; i < processes[0].references.length; i++) {
            for (int j = 0; j < processes.length; j++) {

                errorsPerProcess[j] += lru.count(processes[j].references[i].reference, j);
            }
            leftovers = recalc(processes, framePerProcess, delta, optimal);
            lru.reSize(framePerProcess);
        }
        int sum = 0;
        for (int perProcess : errorsPerProcess) {
            sum += perProcess;
        }
        System.out.println("\n\tZoneModelAllocation");
        System.out.println("last state of framePerProcess: " + Arrays.toString(framePerProcess));
        System.out.println("leftovers: " + leftovers + " Is it optimal: " + optimal);
        System.out.println("EqualAllocation page errors: " + sum + "\nerror of each of the processes:" + Arrays.toString(errorsPerProcess));
    }

    private int recalc(Process[] processes, int[] framePerProcess, int delta, boolean optimal){
        for (int i = 0; i < processes.length; i++) {
            ArrayList<Integer> arr = new ArrayList<>();
            for (int j = 0; j < delta; j++) {
                int temp = processes[i].references[0].reference[j];
                if (!arr.contains(temp)) arr.add(temp);
            }
            framePerProcess[i] = arr.size();
        }

        int leftovers = 0;
        for (int i = 0; i < processes.length; i++) {
            leftovers += framePerProcess[i];
        }
        leftovers = frames - leftovers;
        if (optimal) {
            int temp = 0;
            while (leftovers > 0) {
                if (processes[temp % framePerProcess.length].pages.length>framePerProcess[temp % framePerProcess.length]){
                    framePerProcess[temp % framePerProcess.length]++;
                    leftovers--;
                }
                temp++;
            }
        }
        return leftovers;
    }

    public void errorManagementAllocation(boolean optimal) {
        //init
        Process[] processes = this.processes.clone();
        int[] errorsPerProcess = new int[processes.length];
        Arrays.fill(errorsPerProcess, 0);
        int[] framePerProcess = new int[processes.length];
        Arrays.fill(framePerProcess, (int) Math.floor(frames / processes.length));
        int leftovers = 0;
        for (int i = 0; i < processes.length; i++) {
            leftovers += framePerProcess[i];
        }
        leftovers = frames - leftovers;
        if (optimal) {
            int temp = 0;
            while (leftovers > 0) {
                if (processes[temp % framePerProcess.length].pages.length>framePerProcess[temp % framePerProcess.length]){
                    framePerProcess[temp % framePerProcess.length]++;
                    leftovers--;
                }
                //framePerProcess[temp % framePerProcess.length]++;
                temp++;
                //leftovers--;
            }
        }

        int deltaMin = 3;
        int deltaMax = 18;
        MultithreadingLRU lru = new MultithreadingLRU(framePerProcess.clone());
        /*
        i= numer sekwencji wywołań, j=numer procesu
        i= number of current reference sequence, j=process number
        */
        for (int i = 0; i < processes[0].references.length; i++) {
            int[] tempError = new int[processes.length];
            for (int j = 0; j < processes.length; j++) {

                tempError[j] = lru.count(processes[j].references[i].reference, j);
                errorsPerProcess[j] += tempError[j];

            }
            //System.out.println(Arrays.toString(tempError));
            for (int j = 0; j < framePerProcess.length; j++) {
                if (tempError[j] < deltaMin) {
                    framePerProcess[j]--;
                    leftovers++;
                }
            }
            for (int j = 0; j < framePerProcess.length; j++) {
                if (leftovers == 0) break;
                if (tempError[j] > deltaMax) {
                    framePerProcess[j]++;
                    leftovers--;
                }
                if (optimal) {
                    int temp = 0;
                    while (leftovers > 0) {
                        framePerProcess[temp % framePerProcess.length]++;
                        temp++;
                        leftovers--;
                    }
                }
            }
            //System.out.println("all: "+ Arrays.toString(framePerProcess));
            lru.reSize(framePerProcess);
            //System.out.println(Arrays.toString(framePerProcess));
            Arrays.fill(tempError, 0);
        }

        int sum = 0;
        for (int perProcess : errorsPerProcess) {
            sum += perProcess;
        }
        System.out.println("\n\tErrorManagementAllocation");
        System.out.println("last state of framePerProcess: " + Arrays.toString(framePerProcess));
        System.out.println("leftovers: " + leftovers + " Is it optimal: " + optimal);
        System.out.println("EqualAllocation page errors: " + sum + "\nerror of each of the processes:" + Arrays.toString(errorsPerProcess));
    }
}
