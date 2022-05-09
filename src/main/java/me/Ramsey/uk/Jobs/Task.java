package me.Ramsey.uk.Jobs;

import java.io.Serializable;

/**
 * @author Matthew Ramsey
 */
public class Task implements Serializable {

        private String taskDesc;
        private boolean isComplete;
        private long dateCompleted;

    /**
     * Constructs a new task with the given description and completion status.
     * @param taskDesc The description of the task.
     * @param isComplete Whether the task is complete.
     * @param dateCompleted The date the task was completed.
     */
        public Task(String taskDesc, boolean isComplete, long dateCompleted) {
            this.taskDesc = taskDesc;
            this.isComplete = isComplete;
            this.dateCompleted = dateCompleted;
        }

        public boolean isComplete() {
            return isComplete;
        }

        public void setComplete(boolean complete) {
            isComplete = complete;
        }

        public String getTaskDesc() {
            return taskDesc;
        }

        public void setTaskDesc(String taskDesc) {
            this.taskDesc = taskDesc;
        }

        public long getDateCompleted() {
            return dateCompleted;
        }

        public void setDateCompleted(long dateCompleted) {
            this.dateCompleted = dateCompleted;
        }
    }