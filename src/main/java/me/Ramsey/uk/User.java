package me.Ramsey.uk;

import me.Ramsey.uk.Jobs.Job;
import java.util.UUID;

/**
 * @author Matthew Ramsey
 */
public class User {

    private UUID UUID;
    private String username;
    private Job.JobType jobType;

    /**
     * Constructs a new user with the given UUID, username, and job type.
     * @param UUID The unique identifier of the user.
     * @param username The name of the user.
     * @param jobType The job of the user.
     */
    public User(UUID UUID, String username, Job.JobType jobType) {
        this.UUID = UUID;
        this.username = username;
        this.jobType = jobType;
    }

    /**
     * @return The username for the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The new username for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The job type for the user.
     */
    public Job.JobType getJob() {
        return jobType;
    }

    /**
     * @param jobType The new job type for the user.
     */
    public void setJob(Job.JobType jobType) {
        this.jobType = jobType;
    }

    /**
     * @return The unique identifier for the user.
     */
    public java.util.UUID getUUID() {
        return UUID;
    }

    /**
     * @param UUID The new unique identifier for the user.
     */
    public void setUUID(java.util.UUID UUID) {
        this.UUID = UUID;
    }
}
