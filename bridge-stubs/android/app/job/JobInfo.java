package android.app.job;

import android.content.ComponentName;

public class JobInfo {
    public static class Builder {
        public Builder(int jobId, ComponentName name) {}
        public Builder setMinimumLatency(long minLatencyMillis) { return this; }
        public Builder setOverrideDeadline(long maxExecutionDelayMillis) { return this; }
        public Builder setPersisted(boolean isPersisted) { return this; }
        public JobInfo build() { return new JobInfo(); }
    }
}
