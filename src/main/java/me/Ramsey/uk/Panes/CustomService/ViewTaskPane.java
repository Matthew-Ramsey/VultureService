/*
 * Created by JFormDesigner on Sun May 08 19:14:36 BST 2022
 */

package me.Ramsey.uk.Panes.CustomService;

import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Matthew Ramsey
 */
public class ViewTaskPane extends JLayeredPane {

    private Job job;
    public ViewTaskPane(Job job) {
        this.job = job;
        initComponents();
    }

    private void initComponents() {

        viewTaskPane = this;
        titleLabel = new JLabel();
        signOutButton = new JButton();
        customerServiceTab = new JTabbedPane();
        userManagementPane = new JLayeredPane();
        nameLabel = new JLabel();
        addNewJob = new JButton();
        createdDateLabel = new JLabel();
        completedLabel = new JLabel();

        //======== viewTaskPane ========
        {
            viewTaskPane.setFont(new Font("Arial", Font.PLAIN, 18));

            //---- managementTitleLabel ----
            titleLabel.setText("Customer Services");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            viewTaskPane.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
            titleLabel.setBounds(20, 15, 630, 85);

            //---- signOutButton ----
            signOutButton.setText("Sign Out");
            signOutButton.setFont(new Font("Arial", Font.BOLD, 16));
            signOutButton.addActionListener(e -> Main.signOut());
            viewTaskPane.add(signOutButton, JLayeredPane.DEFAULT_LAYER);
            signOutButton.setBounds(1030, 50, 160, 40);

            //======== managementTab ========
            {

                //======== jobViewingPane ========
                {

                    //---- addNewJob ----
                    addNewJob.setText("Add New Job");
                    addNewJob.setFont(new Font("Arial", Font.BOLD, 16));
                    addNewJob.addActionListener(e -> {
                        Main.openPane(new AddNewJobPane());
                    });
                    userManagementPane.add(addNewJob, JLayeredPane.DEFAULT_LAYER);
                    addNewJob.setBounds(1030, 11, 155, 40);


                    int i=1;
                    for (UUID uuid : job.getTasks().keySet()) {

                        //---- nameLabel ----
                        nameLabel = new JLabel();
                        nameLabel.setText(job.getTasks().get(uuid).getTaskDesc());
                        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userManagementPane.add(nameLabel, JLayeredPane.DEFAULT_LAYER);
                        nameLabel.setBounds(25, 40 * i, 170, 40);

                        //---- dateLabel ----
                        createdDateLabel = new JLabel();
                        try {
                            createdDateLabel.setText("Assigned to: " + Main.getDatabaseManager().getUsername(uuid) + ",   " + "Is Completed: " + job.getTasks().get(uuid).isComplete());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        createdDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userManagementPane.add(createdDateLabel, JLayeredPane.DEFAULT_LAYER);
                        createdDateLabel.setBounds(25, (40 * i) + 20, 400, 40);

                        i++;

                    }
                }
                customerServiceTab.addTab("View Jobs", userManagementPane);
            }
            viewTaskPane.add(customerServiceTab, JLayeredPane.DEFAULT_LAYER);
            customerServiceTab.setBounds(5, 95, 1230, 515);
        }
    }

    private JLayeredPane viewTaskPane;
    private JLabel titleLabel;
    private JButton signOutButton;
    private JTabbedPane customerServiceTab;
    private JLayeredPane userManagementPane;
    private JLabel nameLabel;
    private JLabel createdDateLabel;
    private JLabel completedLabel;
    private JButton addNewJob;

}
