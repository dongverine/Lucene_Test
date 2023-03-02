package Util;

public class TimeChecker {
    private long startTime = 0;
    private long endTime = 0;

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setEndTime() {
        this.endTime = System.currentTimeMillis();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public double getCheckTimeDot(){
        long milTime = endTime - startTime;
        double secTime = ((double)milTime)/((double)1000);
        return secTime;
    }

    public void printCheckTime(){
        System.out.printf("Execution time in seconds: %.2f sec\n" , (this.getCheckTimeDot()));
    }
}
