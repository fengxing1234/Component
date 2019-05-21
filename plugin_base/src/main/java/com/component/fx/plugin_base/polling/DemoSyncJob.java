package com.component.fx.plugin_base.polling;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

public class DemoSyncJob extends Job {

    public static final String TAG = "DemoSyncJob";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        // run your job here
        Log.d(TAG, "onRunJob: ");
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(DemoSyncJob.TAG)
                .setExecutionWindow(30_000L, 40_000L)
                .build()
                .schedule();
    }
}
