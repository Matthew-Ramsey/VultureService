package me.Ramsey.uk;

import me.Ramsey.uk.Database.DatabaseManager;
import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Jobs.Task;
import me.Ramsey.uk.Panes.CustomService.CustomerServicePane;
import me.Ramsey.uk.Panes.MainWindow;
import me.Ramsey.uk.Panes.Management.ManagementPane;
import me.Ramsey.uk.Panes.SignInPane;
import me.Ramsey.uk.Panes.Technician.TechnicianPane;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Matthew Ramsey
 */
public class Main {

    public static JFrame mainWindow;
    public static User user = null;
    private static DatabaseManager databaseManager;
    public static void main(String[] args) {

        databaseManager = new DatabaseManager();

        mainWindow = new MainWindow();
        openPane(new SignInPane());
        mainWindow.setVisible(true);

    }

    /**
     * Opens a pane in the main window
     * @param pane The pane to open.
     */
    public static void openPane(JLayeredPane pane) {
        mainWindow.setContentPane(pane);
        mainWindow.revalidate();
    }

    /**
     * Main method for signing users out.
     * returns the user to the sign in pane.
     */
    public static void signOut() {
        user = null;
        openPane(new SignInPane());
    }

    /**
     * @return JLayeredPane that the user needs to open.
     */
    public static JLayeredPane getJobPane() {
        //rather than having jobPane, it should be new <JobType>Pane
        switch (user.getJob()) {
            case MANAGER:
                return new ManagementPane();
            case TECHNICIAN:
                try {
                    ArrayList<Job> jobsIncludingUser = getDatabaseManager().getAllJobs().stream().filter(job -> job.getTasks().containsKey(user.getUUID())).collect(Collectors.toCollection(ArrayList::new));

                    ArrayList<Task> tasks = new ArrayList<>();
                    for (Job job : jobsIncludingUser) {
                        for (UUID uuid : job.getTasks().keySet()) {
                            if (uuid.equals(user.getUUID())) {
                                tasks.add(job.getTasks().get(uuid));
                            }
                        }
                    }

                    return new TechnicianPane(tasks, jobsIncludingUser);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            default:
                return new CustomerServicePane();
        }
    }

    /**
     * @return the database handler class.
     */
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}