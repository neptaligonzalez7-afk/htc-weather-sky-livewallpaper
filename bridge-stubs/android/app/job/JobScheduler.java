package android.app.job;

public class JobScheduler {
    public static final int RESULT_SUCCESS = 1;
    public int schedule(JobInfo job) { return RESULT_SUCCESS; }
    public void cancel(int jobId) {}
}
