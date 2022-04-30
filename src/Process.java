import java.util.Arrays;

public class Process {
    public int[] pages;
    public ReferenceSequence[] references;
    public int startTime;

    /**
     * Process is a unit with an array of references to pages that the process it needs.
     * Essentially it stores
     * @param pages pages belonging to this process
     * @param references array of reference sequences
     * @param startTime time of arrival in the processor/memory
     */
    public Process(int[] pages, ReferenceSequence[] references, int startTime){
        this.pages=pages;
        this.references=references;
        this.startTime=startTime;
    }

    /**
     * Gets the size of array containing all corresponding pages which is proportional to the process needs for VM/frames
     * @return size of the process
     */
    public int getSize(){
        return pages.length;
    }

    @Override
    public String toString() {
        return "Process{" +
                "pages=" + Arrays.toString(pages) +
                ", references=" + Arrays.toString(references) +
                ", startTime=" + startTime +
                '}';
    }
}
