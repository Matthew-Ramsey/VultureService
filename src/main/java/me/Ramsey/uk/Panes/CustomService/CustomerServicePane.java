/*
 * Created by JFormDesigner on Sun May 08 19:14:36 BST 2022
 */

package me.Ramsey.uk.Panes.CustomService;

import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Matthew Ramsey
 */
public class CustomerServicePane extends JLayeredPane {

    public CustomerServicePane() {
        initComponents();
    }

    private void initComponents() {

        customerServicePane = this;
        titleLabel = new JLabel();
        signOutButton = new JButton();
        managementTab = new JTabbedPane();
        userTaskPanel = new JLayeredPane();
        nameLabel = new JLabel();
        costLabel = new JLabel();
        viewTasksButton = new JButton();
        addNewJob = new JButton();
        dueDateLabel = new JLabel();
        createdDateLabel = new JLabel();

        //======== managementPane ========
        {

            customerServicePane.setFont(new Font("Arial", Font.PLAIN, 18));

            //---- managementTitleLabel ----
            titleLabel.setText("Customer Services");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            customerServicePane.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
            titleLabel.setBounds(20, 15, 630, 85);

            //---- signOutButton ----
            signOutButton.setText("Sign Out");
            signOutButton.setFont(new Font("Arial", Font.BOLD, 16));
            signOutButton.addActionListener(e -> Main.signOut());
            customerServicePane.add(signOutButton, JLayeredPane.DEFAULT_LAYER);
            signOutButton.setBounds(1030, 50, 160, 40);

            //======== managementTab ========
            {

                //======== jobViewingPane ========
                {

                    ArrayList<Job> jobs;
                    try {
                        jobs = Main.getDatabaseManager().getAllJobs();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    //---- addNewJob ----
                    addNewJob.setText("Add New Job");
                    addNewJob.setFont(new Font("Arial", Font.BOLD, 16));
                    addNewJob.addActionListener(e -> {
                        Main.openPane(new AddNewJobPane());
                    });
                    userTaskPanel.add(addNewJob, JLayeredPane.DEFAULT_LAYER);
                    addNewJob.setBounds(1030, 11, 155, 40);


                    int i=1;
                    for (Job job : jobs) {

                        //---- nameLabel ----
                        nameLabel = new JLabel();
                        nameLabel.setText(job.getCustomerName());
                        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userTaskPanel.add(nameLabel, JLayeredPane.DEFAULT_LAYER);
                        nameLabel.setBounds(25, 40 * i, 170, 40);

                        //---- costLabel ----
                        //TODO calculate cost
                        costLabel = new JLabel();
                        costLabel.setText("$1200");
                        costLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userTaskPanel.add(costLabel, JLayeredPane.DEFAULT_LAYER);
                        costLabel.setBounds(625, 40 * i, 60, 40);

                        //---- createdDateLabel ----
                        createdDateLabel = new JLabel();
                        createdDateLabel.setText("Created: " + new Date(job.getDateCreated()));
                        createdDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userTaskPanel.add(createdDateLabel, JLayeredPane.DEFAULT_LAYER);
                        createdDateLabel.setBounds(160, 40 * i, 210, 40);

                        //---- dueDateLabel ----
                        dueDateLabel = new JLabel();
                        dueDateLabel.setText("Due: " + new Date(job.getDueDate()));
                        dueDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        userTaskPanel.add(dueDateLabel, JLayeredPane.DEFAULT_LAYER);
                        dueDateLabel.setBounds(400, 40 * i, 210, 40);

                        // BUTTONS BELOW

                        //---- viewTasksButton ----
                        viewTasksButton = new JButton();
                        viewTasksButton.setText("View Tasks");
                        viewTasksButton.setFont(new Font("Arial", Font.BOLD, 16));
                        viewTasksButton.addActionListener(e -> {
                            Main.openPane(new ViewTaskPane(job));
                        });
                        userTaskPanel.add(viewTasksButton, JLayeredPane.DEFAULT_LAYER);
                        viewTasksButton.setBounds(700, 40 * i, 145, 40);
                        //x = 585, y = 40 * i, width = 145, height = 40

                        i++;

                    }
                }
                managementTab.addTab("View Jobs", userTaskPanel);
            }
            customerServicePane.add(managementTab, JLayeredPane.DEFAULT_LAYER);
            managementTab.setBounds(5, 95, 1230, 515);
        }
    }

    private JLayeredPane customerServicePane;
    private JLabel titleLabel;
    private JButton signOutButton;
    private JTabbedPane managementTab;
    private JLayeredPane userTaskPanel;
    private JLabel nameLabel;
    private JLabel costLabel;
    private JButton viewTasksButton;
    private JLabel dueDateLabel;
    private JLabel createdDateLabel;
    private JButton addNewJob;

}
