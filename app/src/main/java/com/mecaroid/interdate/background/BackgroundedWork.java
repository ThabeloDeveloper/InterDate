package com.mecaroid.interdate.background;

import android.app.PendingIntent;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.UUID;

import kotlinx.coroutines.flow.Flow;

public class BackgroundedWork extends WorkManager {
    @NonNull
    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @NonNull
    @Override
    public Operation enqueue(@NonNull List<? extends WorkRequest> list) {
        return null;
    }

    @NonNull
    @Override
    public WorkContinuation beginWith(@NonNull List<OneTimeWorkRequest> list) {
        return null;
    }

    @NonNull
    @Override
    public WorkContinuation beginUniqueWork(@NonNull String s, @NonNull ExistingWorkPolicy existingWorkPolicy, @NonNull List<OneTimeWorkRequest> list) {
        return null;
    }

    @NonNull
    @Override
    public Operation enqueueUniqueWork(@NonNull String s, @NonNull ExistingWorkPolicy existingWorkPolicy, @NonNull List<OneTimeWorkRequest> list) {
        return null;
    }

    @NonNull
    @Override
    public Operation enqueueUniquePeriodicWork(@NonNull String s, @NonNull ExistingPeriodicWorkPolicy existingPeriodicWorkPolicy, @NonNull PeriodicWorkRequest periodicWorkRequest) {
        return null;
    }

    @NonNull
    @Override
    public Operation cancelWorkById(@NonNull UUID uuid) {
        return null;
    }

    @NonNull
    @Override
    public Operation cancelAllWorkByTag(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public Operation cancelUniqueWork(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public Operation cancelAllWork() {
        return null;
    }

    @NonNull
    @Override
    public PendingIntent createCancelPendingIntent(@NonNull UUID uuid) {
        return null;
    }

    @NonNull
    @Override
    public Operation pruneWork() {
        return null;
    }

    @NonNull
    @Override
    public LiveData<Long> getLastCancelAllTimeMillisLiveData() {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<Long> getLastCancelAllTimeMillis() {
        return null;
    }

    @NonNull
    @Override
    public LiveData<WorkInfo> getWorkInfoByIdLiveData(@NonNull UUID uuid) {
        return null;
    }

    @NonNull
    @Override
    public Flow<WorkInfo> getWorkInfoByIdFlow(@NonNull UUID uuid) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<WorkInfo> getWorkInfoById(@NonNull UUID uuid) {
        return null;
    }

    @NonNull
    @Override
    public LiveData<List<WorkInfo>> getWorkInfosByTagLiveData(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public Flow<List<WorkInfo>> getWorkInfosByTagFlow(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<List<WorkInfo>> getWorkInfosByTag(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public LiveData<List<WorkInfo>> getWorkInfosForUniqueWorkLiveData(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public Flow<List<WorkInfo>> getWorkInfosForUniqueWorkFlow(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<List<WorkInfo>> getWorkInfosForUniqueWork(@NonNull String s) {
        return null;
    }

    @NonNull
    @Override
    public LiveData<List<WorkInfo>> getWorkInfosLiveData(@NonNull WorkQuery workQuery) {
        return null;
    }

    @NonNull
    @Override
    public Flow<List<WorkInfo>> getWorkInfosFlow(@NonNull WorkQuery workQuery) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<List<WorkInfo>> getWorkInfos(@NonNull WorkQuery workQuery) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<UpdateResult> updateWork(@NonNull WorkRequest workRequest) {
        return null;
    }
}
