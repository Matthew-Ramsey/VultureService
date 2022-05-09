package me.Ramsey.uk.Panes.Management;

import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Jobs.Task;
import me.Ramsey.uk.Main;
import me.Ramsey.uk.Panes.Technician.TechnicianPane;
import me.Ramsey.uk.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Matthew Ramsey
 */
public class ManagementPane extends JLayeredPane {

    /**
     * creates a new management pane
     */
    public ManagementPane() {
        initComponents();
    }

    private JLayeredPane managementPane = this;
    private JLabel managementTitleLabel;
    private JButton signOutButton;

    private JTabbedPane managementTab;
    private JLayeredPane userManagementPane;

    private JLabel userNameLabel;
    private JLabel userJobLabel;
    private JButton viewTasksButton;
    private JButton editUserButton;
    private JButton deleteUserButton;
    private JLabel nameColumLabel;
    private JLabel jobColumnLabel;
    private JButton addUserButton;

    /**
     * initialises the components
     * fills the management content
     */
    private void initComponents() {

        managementTitleLabel = new JLabel();
        signOutButton = new JButton();
        managementTab = new JTabbedPane();
        userManagementPane = new JLayeredPane();
        userNameLabel = new JLabel();
        userJobLabel = new JLabel();
        editUserButton = new JButton();
        deleteUserButton = new JButton();
        viewTasksButton = new JButton();
        nameColumLabel = new JLabel();
        jobColumnLabel = new JLabel();
        addUserButton = new JButton();

        //======== managementPane ========
        {
            managementPane.setFont(new Font("Arial", Font.PLAIN, 18));

            //---- managementTitleLabel ----
            managementTitleLabel.setText("Management Services");
            managementTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            managementPane.add(managementTitleLabel, JLayeredPane.DEFAULT_LAYER);
            managementTitleLabel.setBounds(20, 15, 630, 85);

            //---- signOutButton ----
            signOutButton.setText("Sign Out");
            signOutButton.setFont(new Font("Arial", Font.BOLD, 16));
            signOutButton.addActionListener(e -> {
                Main.signOut();
            });
            managementPane.add(signOutButton, JLayeredPane.DEFAULT_LAYER);
            signOutButton.setBounds(1030, 50, 160, 40);

            //======== managementTab ========
            {

                //======== userManagementPane ========
                {

                    /*
                     * IMPORTANT NON USER LABELS/BUTTONS
                     */

                    //---- addUserButton ----
                    addUserButton.setText("Add New User");
                    addUserButton.setFont(new Font("Arial", Font.BOLD, 16));
                    addUserButton.addActionListener(e -> {
                        Main.openPane(new EditUserPane(new User(UUID.randomUUID(), "Username", Job.JobType.TECHNICIAN), this, true));
                    });
                    userManagementPane.add(addUserButton, JLayeredPane.DEFAULT_LAYER);
                    addUserButton.setBounds(1028, 11, 155, 40);

                    //---- nameColumLabel ----
                    nameColumLabel.setText("Name");
                    nameColumLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    userManagementPane.add(nameColumLabel, JLayeredPane.DEFAULT_LAYER);
                    nameColumLabel.setBounds(25, 0, 170, 40);

                    //---- jobColumnLabel ----
                    jobColumnLabel.setText("Job");
                    jobColumnLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    userManagementPane.add(jobColumnLabel, JLayeredPane.DEFAULT_LAYER);
                    jobColumnLabel.setBounds(200, 0, 170, 40);

                    /*
                     * ALL USER ENTRIES
                     */

                    ArrayList<User> users;
                    try {
                        users = Main.getDatabaseManager().getAllUsers();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if (users == null) {
                        throw new RuntimeException("Users is null");
                    }

                    int i=1;
                    // Making each of the individual entries and buttons for each user
                    for (User user : users) {
                        //---- userNameLabel ----
                        JLabel userNameLabel = new JLabel();
                        userNameLabel.setText(user.getUsername());
                        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userManagementPane.add(userNameLabel, JLayeredPane.DEFAULT_LAYER);
                        userNameLabel.setBounds(25, 40 * i, 170, 40);

                        //---- userJobLabel ----
                        JLabel userJobLabel = new JLabel();
                        userJobLabel.setText(user.getJob().name());
                        userJobLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userManagementPane.add(userJobLabel, JLayeredPane.DEFAULT_LAYER);
                        userJobLabel.setBounds(200, 40 * i, 170, 40);

                        //---- viewTasksButton ----
                        JButton viewTasksButton = new JButton();
                        viewTasksButton.setText("View Tasks");
                        viewTasksButton.setFont(new Font("Arial", Font.BOLD, 16));
                        viewTasksButton.addActionListener(e -> {
                            try {
                                ArrayList<Job> jobsIncludingUser = Main.getDatabaseManager().getAllJobs().stream().filter(job -> job.getTasks().containsKey(user.getUUID())).collect(Collectors.toCollection(ArrayList::new));

                                ArrayList<Task> tasks = new ArrayList<>();
                                for (Job job : jobsIncludingUser) {
                                    for (UUID uuid : job.getTasks().keySet()) {
                                        if (uuid.equals(user.getUUID())) {
                                            tasks.add(job.getTasks().get(uuid));
                                        }
                                    }
                                }

                                Main.openPane(new TechnicianPane(tasks, jobsIncludingUser));

                            } catch (SQLException exception) {
                                throw new RuntimeException(exception);
                            }
                        });
                        userManagementPane.add(viewTasksButton, JLayeredPane.DEFAULT_LAYER);
                        viewTasksButton.setBounds(410, 40 * i, 145, 40);

                        //---- editUserButton ----
                        JButton editUserButton = new JButton();
                        editUserButton.setText("Edit User");
                        editUserButton.setFont(new Font("Arial", Font.BOLD, 16));
                        editUserButton.addActionListener(e -> {
                            Main.openPane(new EditUserPane(user, this));
                        });
                        userManagementPane.add(editUserButton, JLayeredPane.DEFAULT_LAYER);
                        editUserButton.setBounds(555, 40 * i, 130, 40);

                        i++;
                    }

                }
                managementTab.addTab("User Management", userManagementPane);
            }
            managementPane.add(managementTab, JLayeredPane.DEFAULT_LAYER);
            managementTab.setBounds(5, 95, 1230, 515);
        }
    }

}
