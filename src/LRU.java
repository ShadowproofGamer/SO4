public class LRU {
    int[] frame;
    int[] time;
    int t=-1;
    boolean firstRun = true;
    public LRU(int frames){
        frame=new int[frames];
        time=new int[frames];
    }

    /**
     *
     * @param references array of references to pages
     * @param frames number of frames delegated to this computer/thread
     * @return returns number of missing page errors
     */
    public int count(int[] references, int frames) {


        int result=0;
        if (firstRun){
            for (int i = 0; i < frames; i++) {
                frame[i]=-1;
            }
            firstRun=false;
        }

        for (int r : references) {
            boolean b=false;
            for (int i = 0; i< frames; i++) {
                if (frame[i] == r) {
                    time[i]=0;
                    b = true;
                    break;
                }
            }
            if (!b){
                result++;
                t++;
                if (t< frames){
                    frame[t]=r;
                    time[t]=0;

                }
                else {
                    int maxIndex=-1;
                    int maxValue=-1;
                    for (int j = 0; j< frames; j++) {
                        if (time[j]>maxValue){
                            maxIndex=j;
                            maxValue=time[j];
                        }
                    }
                    frame[maxIndex]=r;
                    time[maxIndex]=0;
                }
            }
            for (int k = 0; k< frames; k++) {
                time[k]++;
            }

        }

        return result;
    }
}
