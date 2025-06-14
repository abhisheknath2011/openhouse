package com.linkedin.openhouse.jobs.scheduler.tasks;

import com.linkedin.openhouse.jobs.client.JobsClient;
import com.linkedin.openhouse.jobs.client.TablesClient;
import com.linkedin.openhouse.jobs.util.DirectoryMetadata;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A callable class to apply an operation to a table directory by running a Spark job. Takes care of
 * the job lifecycle using /jobs API.
 */
@Slf4j
@Getter
public abstract class TableDirectoryOperationTask extends OperationTask<DirectoryMetadata> {
  public TableDirectoryOperationTask(
      JobsClient jobsClient,
      TablesClient tablesClient,
      DirectoryMetadata metadata,
      long pollIntervalMs,
      long queuedTimeoutMs,
      long taskTimeoutMs) {
    super(jobsClient, tablesClient, metadata, pollIntervalMs, queuedTimeoutMs, taskTimeoutMs);
  }

  public TableDirectoryOperationTask(
      JobsClient jobsClient, TablesClient tablesClient, DirectoryMetadata metadata) {
    super(jobsClient, tablesClient, metadata);
  }

  protected boolean launchJob() {
    String jobName = String.format("%s_%s", getType(), metadata.getPath());
    jobId = jobsClient.launch(jobName, getType(), metadata.getCreator(), getArgs()).orElse(null);
    return jobId != null;
  }

  @Override
  protected boolean shouldRun() {
    return true;
  }
}
