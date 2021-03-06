import java.util.Random;

import static java.lang.Math.abs;

public class Generator {
    Random rand = new Random();
    //variables for Reference sequence generator:
    /**
     * number of references per reference sequence
     */
    int numberOfReferences = 100;
    /**
     * minimal number of references per reference sequence
     */
    int minNumberOfReferences = numberOfReferences / 2;
    /**
     * number of pages per process
     */
    int pages = 7;
    /**
     * if > -1 than the processes should have random amount of pages,
     * with Min at {@code pagesLowerBound} and high at {@link #pages}
     */
    int pagesLowerBound = -1;
    //random generator seeds/roots
    int lower = 2;
    int higher = 4;
    int same = 12;
    int random = 1;
    //variables for generating processes and reference sequences
    /**
     * amount of processes generated
     */
    int processAmount = 12;
    /**
     * increment of time of every process
     */
    int processStart = 0;
    /**
     * number of sequence reference arrays in each process
     */
    int sequencesLength = 10;

    /**
     *Basic constructor. All values are preset.
     */
    public Generator() {
    }

    /**
     * Fully customizable constructor. Every aspect of process generation can be modified with this parameters:
     * @param numberOfReferences number of references per reference sequence
     * @param minNumberOfReferences minimal number of references per reference sequence
     * @param pages number/maximal number of pages per process
     * @param pagesLowerBound minimal number of pages per process
     * @param lower probability for lower page value
     * @param higher probability for higher page value
     * @param same probability for same page value
     * @param random probability for random page value
     * @param processAmount amount of processes generated
     * @param processStart increment of time of every process
     * @param sequencesLength number of sequence reference arrays in each process
     */
    public Generator(int numberOfReferences, int minNumberOfReferences, int pages, int pagesLowerBound, int lower, int higher, int same, int random,
                     int processAmount, int processStart, int sequencesLength) {
        this.numberOfReferences = numberOfReferences;
        this.minNumberOfReferences = minNumberOfReferences;
        this.pages = pages;
        this.pagesLowerBound = pagesLowerBound;
        this.lower = lower;
        this.higher = higher;
        this.same = same;
        this.random = random;
        this.processAmount = processAmount;
        this.processStart = processStart;
        this.sequencesLength = sequencesLength;
    }

    /**
     *
     * @return
     */
    public Process[] generateProcess() {
        int startTime = 0;
        Process[] result = new Process[processAmount];
        int temp = 0;
        /**
         * {@code tPages} dictates the amount of pages each of the process' require
         * minimal amount is bound by {@link pagesLowerBound} and
         * maximal amount is bound by {@link pages}
         */
        int tPages = pages;
        for (int i = 0; i < processAmount; i++) {
            if (pagesLowerBound>=0) tPages = rand.nextInt(pages-pagesLowerBound)+pagesLowerBound;
            int[] p = new int[tPages];
            for (int j = 0; j < tPages; j++) {
                p[j] = temp + j;
                //temp++;
            }
            ReferenceSequence[] rs = generateReferenceSequenceArray(startTime, temp);
            temp += tPages;
            result[i] = new Process(p, rs, startTime);
            startTime += processStart;
        }
        return result;
    }

    private ReferenceSequence[] generateReferenceSequenceArray(int startTime, int startPage) {
        ReferenceSequence[] result = new ReferenceSequence[sequencesLength];
        int start = startTime;
        for (int i = 0; i < sequencesLength; i++) {
            result[i] = new ReferenceSequence(generateIncLocalityOptions(startPage), start);
            start += processStart;
        }
        return result;
    }

    private int[] generateIncLocalityOptions(int startPage) {
        //System.out.println("jeden mniejsze: " + lower + "\tjeden wi??ksze: " + higher + "\ttakie same:" + same + "\tlosowe:" + random);
        //Random rand = new Random();
        int temp = 0;
        int[] result = new int[numberOfReferences];

        for (int i = 0; i < numberOfReferences; i++) {
            int op = rand.nextInt(lower + higher + same + random);
            int r = temp;
            if (op <= lower) {
                r = temp - 1;
                if (r < 0) r += pages;
            } else if (op <= (lower + higher)) r = abs(temp + 1) % pages;
            else if (op <= (lower + higher + random)) r = rand.nextInt(pages);
            temp = r;
            r += startPage;
            result[i] = r;

        }
        return result;
    }
}
