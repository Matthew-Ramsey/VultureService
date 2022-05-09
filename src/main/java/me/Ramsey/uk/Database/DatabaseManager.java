package me.Ramsey.uk.Database;

import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Jobs.Task;
import me.Ramsey.uk.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matthew Ramsey
 */
public class DatabaseManager {

    private final Connection dbConn;

    /**
     * Constructs a new database manager with the given connection.
     */
    public DatabaseManager() {
        try {

            dbConn = DriverManager.getConnection("jdbc:h2:./src/main/java/me/Ramsey/uk/Database/db", "username", "password");

            initComponents();
            insertDefaultData();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Initialises the database components.
     * If tables don't exist, they're created.
     * @throws SQLException If the database connection or statement is invalid.
     */
    private void initComponents() throws SQLException {
        String statement = "CREATE TABLE IF NOT EXISTS USERS (\n" +
                "    USER_UUID CHAR(36)     NOT NULL,\n" +
                "    USERNAME  VARCHAR(255) NOT NULL,\n" +
                "    PASSWORD  VARCHAR(255) DEFAULT NULL,\n" +
                "    JOB       int          NOT NULL,\n" +
                "    PRIMARY KEY (USER_UUID)\n" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS JOBS (\n" +
                "    JOB_UUID      CHAR(36)     NOT NULL,\n" +
                "    CUSTOMER_NAME VARCHAR(255) NOT NULL,\n" +
                "    DUE           LONG    NOT NULL,\n" +
                "    CREATED       LONG    NOT NULL,\n" +
                "    TASKS         JAVA_OBJECT  NOT NULL,\n" +
                "    PRIMARY KEY (JOB_UUID)\n" +
                ");" +
                "" +
                "";

        dbConn.prepareStatement(statement)
                .execute();
    }

    /**
     * Inserts default data into the database.
     * @throws SQLException If the database connection or statement is invalid.
     */
    private void insertDefaultData() throws SQLException {
        if (isEmptyTable("USERS")) {
            insertMultipleUsers(
                    new User(UUID.randomUUID(), "Ed", Job.JobType.MANAGER),
                    new User(UUID.randomUUID(), "Anita", Job.JobType.TECHNICIAN),
                    new User(UUID.randomUUID(), "Jonathan", Job.JobType.HR),
                    new User(UUID.randomUUID(), "Mike", Job.JobType.TECHNICIAN),
                    new User(UUID.randomUUID(), "Dave", Job.JobType.TECHNICIAN),
                    new User(UUID.randomUUID(), "Sue", Job.JobType.CUSTOMERSERVICE),
                    new User(UUID.randomUUID(), "Andrew", Job.JobType.CUSTOMERSERVICE),
                    new User(UUID.randomUUID(), "Tawfik", Job.JobType.IT),
                    new User(UUID.randomUUID(), "Malcolm", Job.JobType.FINANCE)
            );
        }
    }

    /**
     * Get all jobs in the database.
     * @return All jobs within the database
     * @throws SQLException If the database connection or statement is invalid.
     */
    public ArrayList<Job> getAllJobs() throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM JOBS");
        ResultSet set = statement.executeQuery();
        ArrayList<Job> jobs = new ArrayList<>();
        while(set.next()) {
            jobs.add(new Job(
                    UUID.fromString(set.getString("JOB_UUID")),
                    set.getString("CUSTOMER_NAME"),
                    set.getLong("DUE"),
                    set.getLong("CREATED"),
                    (HashMap<UUID, Task>) set.getObject("TASKS")
            ));
        }
        return jobs;
    }

    /**
     * Get every user from the database
     * @return All users within the database
     * @throws SQLException If the database connection or statement is invalid.
     */
    public ArrayList<User> getAllUsers() throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM USERS");
        ResultSet set = statement.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        while(set.next()) {
            users.add(new User(set.getObject("USER_UUID", java.util.UUID.class), set.getString("USERNAME"), Job.JobType.values()[set.getInt("JOB")]));
        }
        return users;
    }

    /**
     * get User by their username
     * @param username The username of the user to get.
     * @return The user with the given username.
     * @throws SQLException If the database connection or statement is invalid.
     */
    public User getUser(String username) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM USERS WHERE LOWER(USERNAME) = ?;");
        statement.setString(1, username.toLowerCase());
        ResultSet set = statement.executeQuery();
        if (!set.next()) return null;
        return new User(set.getObject("USER_UUID", java.util.UUID.class), set.getString("USERNAME"), Job.JobType.values()[set.getInt("JOB")]);
    }

    /**
     * get username of a user by their UUID
     * @param uuid The UUID of the user to get.
     * @return The username of the user with the given UUID.
     * @throws SQLException If the database connection or statement is invalid.
     */
    public String getUsername(UUID uuid) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("SELECT USERNAME FROM USERS WHERE USER_UUID = ?;");
        statement.setObject(1, uuid);
        ResultSet set = statement.executeQuery();
        if (!set.next()) return null;
        return set.getString("USERNAME");
    }

    /**
     * Get specific user from the database.
     * @param username The username of the user to get.
     * @param password The password of the user to get.
     * @return The user with the given username and password.
     * @throws SQLException If the database connection or statement is invalid.
     */
    public User getUser(String username, String password) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM USERS WHERE LOWER(USERNAME) = ? AND PASSWORD = ?;");
        statement.setString(1, username.toLowerCase());
        statement.setString(2, password);
        ResultSet set = statement.executeQuery();
        if (!set.next()) return null;
        return new User(set.getObject("USER_UUID", java.util.UUID.class), set.getString("USERNAME"), Job.JobType.values()[set.getInt("JOB")]);
    }

    /**
     * Get all technicians in the database
     * @return All users with the job of technician.
     * @throws SQLException
     */
    public ArrayList<User> getAllUsersOfTechnician() throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM USERS WHERE JOB = 1");
        ResultSet set = statement.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        while(set.next()) {
            users.add(new User(set.getObject("USER_UUID", java.util.UUID.class), set.getString("USERNAME"), Job.JobType.values()[set.getInt("JOB")]));
        }
        return users;
    }

    /**
     * Insert a job into the database
     * @param job The job to be inserted into the database
     * @throws SQLException If the database connection or statement is invalid.
     */
    public void insertJob(Job job) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("INSERT INTO JOBS (JOB_UUID, CUSTOMER_NAME, DUE, CREATED, TASKS) VALUES (?, ?, ?, ?, ?);");

        statement.setObject(1, job.getJobUUID());
        statement.setString(2, job.getCustomerName());
        statement.setLong(3, job.getDueDate());
        statement.setLong(4, job.getDateCreated());
        statement.setObject(5, job.getTasks());
        statement.execute();
    }

    /**
     * Insert a user into the database
     * @param user The user to be inserted into the database
     * @throws SQLException If the database connection or statement is invalid.
     */
    public void insertUser(User user) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("INSERT INTO USERS (USER_UUID, USERNAME, PASSWORD, JOB) VALUES (?, ?, ?, ?);");

        statement.setObject(1, user.getUUID());
        statement.setString(2, user.getUsername());
        statement.setString(3, "password");
        statement.setObject(4, user.getJob().ordinal());
        statement.execute();
    }

    /**
     * Remove a user from the database
     * @param user The user to be removed from the database
     * @throws SQLException If the database connection or statement is invalid.
     */
    public void deleteUser(User user) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("DELETE FROM USERS WHERE USER_UUID = ?;");
        statement.setObject(1, user.getUUID());
        statement.execute();
    }

    /**
     * Update a specific user in the database
     * @param uuid The UUID of the user to update.
     * @param replacementInfo The replacement information to update the user with.
     * @param password The password of the user to update.
     * @throws SQLException If the database connection or statement is invalid.
     */
    public void updateUser(UUID uuid, User replacementInfo, String password) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("UPDATE USERS SET USERNAME=?, JOB=?, PASSWORD=? WHERE USER_UUID=?");

        statement.setString(1, replacementInfo.getUsername());
        statement.setObject(2, replacementInfo.getJob().ordinal());
        statement.setString(3, password);
        statement.setObject(4, uuid);
        statement.execute();
    }

    /**
     * Inserts multiple users into the database.
     * @param users The users to be inserted into the database
     * @throws SQLException If the database connection or statement is invalid.
     */
    public void insertMultipleUsers(User... users) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("INSERT INTO USERS (USER_UUID, USERNAME, PASSWORD, JOB) VALUES (?, ?, ?, ?);");
        for (User user : users) {
            statement.setObject(1, user.getUUID());
            statement.setString(2, user.getUsername());
            statement.setString(3, "password");
            statement.setObject(4, user.getJob().ordinal());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    /**
     * Update the tasks within a job.
     * @param job The job to be updated in the database
     * @throws SQLException If the database connection or statement is invalid.
     */
    public void updateJobTasks(Job job) throws SQLException {
        PreparedStatement statement = dbConn.prepareStatement("UPDATE JOBS SET TASKS=? WHERE JOB_UUID=?");
        statement.setObject(1, job.getTasks());
        statement.setObject(2, job.getJobUUID());
        statement.execute();
    }


    /**
     * Check to see if a specific table is empty
     * @param tableName The name of the table to check.
     * @return boolean depending on if the table is empty or not
     * @throws SQLException If the database connection or statement is invalid.
     */
    private boolean isEmptyTable(String tableName) throws SQLException {
        ResultSet resultSet = dbConn.prepareStatement("select count(*) where exists (select * from " + tableName + " );")
                .executeQuery();
        resultSet.next();
        return resultSet.getInt(1) == 0;
    }

    /**
     * Output the name of all tables in the database.
     * @throws SQLException
     */
    private void showAllTables() throws SQLException {
        String statement = "SHOW TABLES;";
        ResultSet set = dbConn.prepareStatement(statement).executeQuery();
        while (set.next()) {
            System.out.println(set.getString(1));
        }
    }
}
