import java.util.Arrays;

public class ReferenceSequence {
    int[] reference;
    int arrivalTime;

    /**
     * Reference sequence is an array of requests for a certain pages. Processor generates many of those during his lifetime
     * @param reference array of numbers which represents references to pages
     * @param arrivalTime time when the reference sequence arrives at the system
     */
    public ReferenceSequence(int[] reference, int arrivalTime){
        this.reference =reference;
        this.arrivalTime=arrivalTime;
    }

    @Override
    public String toString() {
        return "ReferenceSequence{" +
                "reference=" + Arrays.toString(reference) +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
