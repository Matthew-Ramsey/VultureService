package me.Ramsey.uk.Jobs;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matthew Ramsey
 */
public class Job {

    private UUID jobUUID;
    private String CustomerName;
    private long dueDate;
    private long dateCreated;
    private HashMap<UUID, Task> tasks;

    /**
     * Constructs a new job with the given UUID, customer name, due date, and date created.
     * @param jobUUID The unique identifier of the job.
     * @param customerName The name of the customer.
     * @param dueDate The due date of the job.
     * @param dateCreated The date the job was created.
     * @param tasks The tasks for the job.
     */
    public Job(UUID jobUUID, String customerName, long dueDate, long dateCreated, HashMap<UUID, Task> tasks) {
        this.jobUUID = jobUUID;
        this.CustomerName = customerName;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
        this.tasks = tasks;
    }


    public UUID getJobUUID() {
        return jobUUID;
    }

    public void setJobUUID(UUID jobUUID) {
        this.jobUUID = jobUUID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public HashMap<UUID, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<UUID, Task> tasks) {
        this.tasks = tasks;
    }

    public enum JobType {
        MANAGER, TECHNICIAN, HR, CUSTOMERSERVICE, IT, FINANCE
    }
}
