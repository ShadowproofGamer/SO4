import java.util.Arrays;

public class MultithreadingLRU {
    int[] framePerProcess;
    int[][] frame;
    int[][] time;
    int[] t;
    boolean[] firstRun;
    int n=0;
    int[] num;
    public MultithreadingLRU(int[] framePerProcess){
        this.framePerProcess=framePerProcess.clone();
        frame= new int[framePerProcess.length][];
        time= new int[framePerProcess.length][];
        firstRun = new boolean[framePerProcess.length];
        Arrays.fill(firstRun, true);
        t = new int[framePerProcess.length];
        Arrays.fill(t, -1);
        for (int i = 0; i < framePerProcess.length; i++) {
            frame[i]=new int[framePerProcess[i]];
            time[i]=new int[framePerProcess[i]];

        }
    }

    /**
     *
     * @param references array of references to pages
     * @return returns number of missing page errors
     */
    public int count(int[] references, int processNumber) {
        //System.out.println("count number: "+n++);
        int result=0;
        if (firstRun[processNumber]){
            Arrays.fill(frame[processNumber], -1);
            firstRun[processNumber]=false;
        }
        for (int r : references) {
            boolean b=false;
            //System.out.println(framePerProcess[processNumber]);
            //System.out.println(Arrays.toString(frame[processNumber]));
            for (int i = 0; i< framePerProcess[processNumber]; i++) {
                //System.out.println(i);
                //System.out.println(Arrays.toString(framePerProcess));
                //System.out.println("prNum: "+processNumber+"  i: "+i+"  r:"+r);
                if (frame[processNumber][i] == r) {
                    time[processNumber][i]=0;
                    b = true;
                    break;
                }
            }
            if (!b){
                result++;
                t[processNumber]++;
                if (t[processNumber]< framePerProcess[processNumber]){
                    frame[processNumber][t[processNumber]]=r;
                    time[processNumber][t[processNumber]]=0;

                }
                else {
                    int maxIndex=-1;
                    int maxValue=-1;
                    for (int j = 0; j< framePerProcess[processNumber]; j++) {
                        if (time[processNumber][j]>maxValue){
                            maxIndex=j;
                            maxValue=time[processNumber][j];
                        }
                    }
                    frame[processNumber][maxIndex]=r;
                    time[processNumber][maxIndex]=0;
                }
            }
            for (int k = 0; k< framePerProcess[processNumber]; k++) {
                time[processNumber][k]++;
            }

        }
        num=framePerProcess.clone();
        //System.out.println("countFPP: "+ Arrays.toString(framePerProcess));
        //System.out.println("countNum: "+ Arrays.toString(num));
        return result;
    }
    public void reSize(int[] fPP){
        //System.out.println(Arrays.toString(fPP));
        //System.out.println("res: "+Arrays.toString(framePerProcess));
        //System.out.println("num: "+Arrays.toString(num));
        for (int i = 0; i < fPP.length; i++) {
            if (Arrays.equals(fPP, num))break;
            //System.out.println(Arrays.toString(fPP));
            //System.out.println(Arrays.toString(framePerProcess));
            //System.out.println("reSizeIter: "+i+"   "+(fPP[i]<framePerProcess[i]));
            //System.out.println(Arrays.toString(num)+" co tu się dzieje?");
            if (fPP[i]<num[i]){
                //System.out.println(fPP[i]+"   "+num[i]);
                int[] temp = new int[fPP[i]];
                Arrays.fill(temp, -1);
                for (int j = 0; j < temp.length; j++) {
                    temp[j]=frame[i][j];
                }
                frame[i]=temp.clone();
                //System.out.println("tmp: "+ Arrays.toString(temp));


            }
            if (fPP[i]>num[i]){
                int[] temp = new int[fPP[i]];
                int[] temp2 = new int[fPP[i]];
                Arrays.fill(temp, -1);
                Arrays.fill(temp2, -1);
                for (int j = 0; j < frame[i].length; j++) {
                    temp[j]=frame[i][j];
                    temp2[j]= time[i][j];
                }

                time[i]=temp2.clone();
                frame[i]=temp.clone();
                //System.out.println("tmp: "+ Arrays.toString(temp));
            }
            //System.out.println("fl: "+frame[i].length);
            //System.out.println("fpp: "+fPP[i]);
        }
        framePerProcess=fPP;
        //System.out.println(Arrays.deepToString(frame));
        //System.out.println(Arrays.toString(framePerProcess));
    }
}
