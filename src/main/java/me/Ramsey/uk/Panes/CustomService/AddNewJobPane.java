/*
 * Created by JFormDesigner on Sun May 08 20:17:49 BST 2022
 */

package me.Ramsey.uk.Panes.CustomService;

import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Jobs.Task;
import me.Ramsey.uk.Main;
import me.Ramsey.uk.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matthew Ramsey
 */
public class AddNewJobPane extends JLayeredPane {

    private HashMap<String, User> tasks;

    // Coming from Customer Service Pane
    public AddNewJobPane() {
        this.tasks = new HashMap<>();
        initComponents();
    }

    // Jumping between adding a new job and editing an existing one
    public AddNewJobPane(HashMap<String, User> tasks) {
        this.tasks = tasks;
        initComponents();
    }

    private void initComponents() {

        addNewJobPane = this;
        titleLabel = new JLabel();
        signOutButton = new JButton();
        addListEntryButton = new JButton();
        removeListEntryButton = new JButton();
        nameColumLabel = new JLabel();
        cancelButton = new JButton();
        submitButton = new JButton();
        errorLabel = new JLabel();
        textField1 = new JTextField();
        nameColumLabel2 = new JLabel();
        spinner1 = new JSpinner();
        scrollPane1 = new JScrollPane();
        list1 = new JList<>();

        //======== managementPane2 ========
        {
            addNewJobPane.setFont(new Font("Arial", Font.PLAIN, 18));

            //---- managementTitleLabel2 ----
            titleLabel.setText("Management Services");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            addNewJobPane.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
            titleLabel.setBounds(20, 15, 630, 85);

            //---- SignOutButton ----
            signOutButton.setText("Sign Out");
            signOutButton.setFont(new Font("Arial", Font.BOLD, 16));
            signOutButton.addActionListener(e -> {
                Main.signOut();
            });
            addNewJobPane.add(signOutButton, JLayeredPane.DEFAULT_LAYER);
            signOutButton.setBounds(1030, 50, 160, 40);

            //---- nameColumLabel ----
            nameColumLabel.setText("Customer Name");
            nameColumLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            addNewJobPane.add(nameColumLabel, JLayeredPane.DEFAULT_LAYER);
            nameColumLabel.setBounds(25, 105, 215, 30);

            //---- cancelButton ----
            cancelButton.setText("Cancel");
            cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
            cancelButton.addActionListener(e -> {
                Main.openPane(new CustomerServicePane());
            });
            addNewJobPane.add(cancelButton, JLayeredPane.DEFAULT_LAYER);
            cancelButton.setBounds(810, 545, 160, 40);

            //---- submitButton ----
            submitButton.setText("Submit");
            submitButton.setFont(new Font("Arial", Font.BOLD, 16));
            submitButton.addActionListener(e -> {

                if (textField1.getText().isEmpty() || (Integer) spinner1.getValue() == 0 || list1.isSelectionEmpty()) {
                    errorLabel.setText("Please ensure all fields are filled");
                    errorLabel.setVisible(true);
                }

                long time = System.currentTimeMillis();
                Date date = new Date(time);
                Date dueDate = new Date(time + (long) (Integer) spinner1.getValue() * 86400000);

                HashMap<UUID, Task> finalTasks = new HashMap<>();

                for (String desc : tasks.keySet()) {
                    finalTasks.put(
                            tasks.get(desc).getUUID(),
                            new Task(
                                    desc,
                                    false,
                                    0
                            ));
                }

                try {
                    Main.getDatabaseManager().insertJob(
                            new Job(
                                    UUID.randomUUID(),
                                    textField1.getText(),
                                    dueDate.getTime(),
                                    date.getTime(),
                                    finalTasks
                            ));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                Main.openPane(new CustomerServicePane());
            });
            addNewJobPane.add(submitButton, JLayeredPane.DEFAULT_LAYER);
            submitButton.setBounds(1025, 545, 160, 40);

            //---- errorLabel ----
            errorLabel.setText("Error. Please make sure all fields are filled.");
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setForeground(new Color(153, 0, 0));
            errorLabel.setVisible(false);
            addNewJobPane.add(errorLabel, JLayeredPane.DEFAULT_LAYER);
            errorLabel.setBounds(732, 452, 425, 30);

            //---- textField1 ----
            textField1.setFont(new Font("Arial", Font.PLAIN, 16));
            addNewJobPane.add(textField1, JLayeredPane.DEFAULT_LAYER);
            textField1.setBounds(20, 140, 330, 45);

            //---- nameColumLabel2 ----
            nameColumLabel2.setText("Estimated days");
            nameColumLabel2.setFont(new Font("Arial", Font.PLAIN, 16));
            addNewJobPane.add(nameColumLabel2, JLayeredPane.DEFAULT_LAYER);
            nameColumLabel2.setBounds(25, 200, 215, 30);
            addNewJobPane.add(spinner1, JLayeredPane.DEFAULT_LAYER);
            spinner1.setBounds(22, 233, 105, 40);

            //---- AddListEntryButton ----
            addListEntryButton.setText("+");
            addListEntryButton.setFont(new Font("Arial", Font.BOLD, 16));
            addListEntryButton.setForeground(new Color(88, 255, 9, 255));
            addListEntryButton.addActionListener(e -> {
                Main.openPane(new AddTaskPane(tasks));
            });
            addNewJobPane.add(addListEntryButton, JLayeredPane.DEFAULT_LAYER);
            addListEntryButton.setBounds(1180, 130, 50, 50);

            //---- removeListEntryButton ----
            removeListEntryButton.setText("-");
            removeListEntryButton.setFont(new Font("Arial", Font.BOLD, 16));
            removeListEntryButton.setForeground(new Color(169, 0, 45, 255));
            removeListEntryButton.addActionListener(e -> {
                tasks.remove(list1.getSelectedValue());
                Main.openPane(new AddNewJobPane(tasks));
            });
            addNewJobPane.add(removeListEntryButton, JLayeredPane.DEFAULT_LAYER);
            removeListEntryButton.setBounds(1180, 200, 50, 50);

            //======== scrollPane1 ========
            {

                //---- list1 ----
                list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                list1.setModel(new AbstractListModel<String>() {

                    String[] values = tasks.keySet().toArray(new String[0]);
                    @Override
                    public int getSize() { return values.length; }
                    @Override
                    public String getElementAt(int i) { return values[i]; }
                });
                scrollPane1.setViewportView(list1);
            }
            addNewJobPane.add(scrollPane1, JLayeredPane.DEFAULT_LAYER);
            scrollPane1.setBounds(365, 105, 810, 415);
        }

    }

    private JLayeredPane addNewJobPane;
    private JLabel titleLabel;
    private JButton signOutButton;
    private JButton addListEntryButton;
    private JButton removeListEntryButton;
    private JLabel nameColumLabel;
    private JButton cancelButton;
    private JButton submitButton;
    private JLabel errorLabel;
    private JTextField textField1;
    private JLabel nameColumLabel2;
    private JSpinner spinner1;
    private JScrollPane scrollPane1;
    private JList<String> list1;

}
