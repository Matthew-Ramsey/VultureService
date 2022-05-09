/*
 * Created by JFormDesigner on Mon May 09 10:53:05 BST 2022
 */

package me.Ramsey.uk.Panes.Technician;

import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Jobs.Task;
import me.Ramsey.uk.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matthew Ramsey
 */
public class TechnicianPane extends JLayeredPane {

    ArrayList<Task> tasks;
    ArrayList<Job> jobs;

    /**
     * creates a new technician pane
     * @param tasks the tasks to display
     * @param jobs the jobs to display
     */
    public TechnicianPane(ArrayList<Task> tasks, ArrayList<Job> jobs) {
        this.tasks = tasks;
        this.jobs = jobs;
        this.tasks.removeIf(Task::isComplete);
        initComponents();
    }

    /**
     * initialises the components
     * fills the technician content
     */
    private void initComponents() {

        technicianPane = this;
        titleLabel = new JLabel();
        signOutButton = new JButton();

        nameLabel = new JLabel();
        scrollPane = new JScrollPane();
        list = new JList<>();

        submitButton = new JButton();

        //======== technicianPane ========
        {

            //---- signOutButton ----
            signOutButton.setText("Sign Out");
            signOutButton.setFont(new Font("Arial", Font.BOLD, 16));
            signOutButton.addActionListener(e -> {
                Main.signOut();
            });
            technicianPane.add(signOutButton, JLayeredPane.DEFAULT_LAYER);
            signOutButton.setBounds(1030, 50, 160, 40);

            //---- submitButton ----
            submitButton.setText("Confirm Task Completed");
            submitButton.setFont(new Font("Arial", Font.BOLD, 16));

            /*
              Button for marking a task as complete.
              Sets the task as complete then updates it within the database
             */
            submitButton.addActionListener(e -> {
                if (list.getSelectedIndex() != -1) {
                    Task task = tasks.get(list.getSelectedIndex());
                    task.setComplete(true);
                    jobs.forEach(job -> {
                        if (job.getTasks().containsValue(task)) {
                            HashMap<UUID, Task> allJobTasks = job.getTasks();
                            allJobTasks.put(Main.user.getUUID(), task);
                            job.setTasks(allJobTasks);
                            try {
                                Main.getDatabaseManager().updateJobTasks(job);
                                System.out.println("Updated job tasks");
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
                Main.openPane(new TechnicianPane(tasks, jobs));
            });
            technicianPane.add(submitButton, JLayeredPane.DEFAULT_LAYER);
            submitButton.setBounds(860, 510, 305, 45);

            //======== scrollPane ========
            {

                //---- list ----
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setModel(new AbstractListModel<String>() {
                    final String[] values;
                    {
                        values = new String[tasks.size()];
                        for (int i = 0; i < tasks.size(); i++) {
                            values[i] = tasks.get(i).getTaskDesc();
                        }
                    }
                    @Override
                    public int getSize() { return values.length; }
                    @Override
                    public String getElementAt(int i) { return values[i]; }
                });
                scrollPane.setViewportView(list);
            }
            technicianPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);
            scrollPane.setBounds(20, 120, 1115, 370);

            //---- titleLabel ----
            titleLabel.setText("Technician Services");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            technicianPane.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
            titleLabel.setBounds(new Rectangle(new Point(15, 35), titleLabel.getPreferredSize()));

            //---- nameLabel ----
            nameLabel.setText("Tasks To Complete");
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            technicianPane.add(nameLabel, JLayeredPane.DEFAULT_LAYER);
            nameLabel.setBounds(20, 70, 170, 40);
        }
    }

    private JLayeredPane technicianPane;
    private JButton signOutButton;
    private JButton submitButton;
    private JScrollPane scrollPane;
    private JList<String> list;
    private JLabel titleLabel;
    private JLabel nameLabel;

}
