public class AnomalyError {
    int processNumber;
    int numberOfPages;
    int numberOfErrors;
    public AnomalyError(int processNumber, int numberOfPages, int numberOfErrors){

        this.processNumber = processNumber;
        this.numberOfPages = numberOfPages;
        this.numberOfErrors = numberOfErrors;
    }

    @Override
    public String toString() {
        return "#" + processNumber +
                " pages:" + numberOfPages +
                " errors:" + numberOfErrors;
    }
}
