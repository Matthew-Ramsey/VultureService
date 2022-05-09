/*
 * Created by JFormDesigner on Sun May 08 17:30:55 BST 2022
 */

package me.Ramsey.uk.Panes.Management;

import com.sun.istack.internal.Nullable;
import me.Ramsey.uk.Jobs.Job;
import me.Ramsey.uk.Main;
import me.Ramsey.uk.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Matthew Ramsey
 */
public class EditUserPane extends JLayeredPane {

    private User user;
    private JLayeredPane previous;
    private boolean isNewUser = false;

    /**
     * Constructor for the EditUserPane class
     * @param user the user to edit
     * @param previous the previous pane
     */
    public EditUserPane(User user, @Nullable JLayeredPane previous) {
        this.user = user;
        this.previous = previous;
        initComponents();
    }

    /**
     * Constructor for the EditUserPane class
     * @param user the user to edit
     * @param previous the previous pane
     * @param isNewUser whether the user is new or not
     */
    public EditUserPane(User user, @Nullable JLayeredPane previous, boolean isNewUser) {
        this.user = user;
        this.previous = previous;
        this.isNewUser = isNewUser;
        initComponents();
    }

    private void logOutButton(ActionEvent e) {
        Main.signOut();
    }

    /**
     * Submit button once the user info is finished
     * @param e
     */
    private void submit(ActionEvent e) {
        if (textField1.getText().isEmpty() || passwordField1.getPassword().length == 0 || jobList.getSelectedValue() == null) {
            errorLabel.setVisible(true);
            return;
        }

        User replacementInfo = new User(UUID.randomUUID(), textField1.getText(), Job.JobType.valueOf(jobList.getSelectedValue()));
        try {
            if (!isNewUser) Main.getDatabaseManager().updateUser(user.getUUID(), replacementInfo, String.valueOf(passwordField1.getPassword()));
            else Main.getDatabaseManager().insertUser(new User(UUID.randomUUID(), textField1.getText(), Job.JobType.valueOf(jobList.getSelectedValue())));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            Main.openPane(new ManagementPane());
        }
    }

    private void cancel(ActionEvent e) {
        Main.openPane(previous);
    }

    private void initComponents() {

        editUserPane = this;
        titleLabel = new JLabel();
        signOutButton = new JButton();

        deleteUserButton = new JButton();
        nameLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        jobList = new JList<>();
        cancelButton = new JButton();
        submitButton = new JButton();
        errorLabel = new JLabel();
        textField1 = new JTextField();
        nameLabel2 = new JLabel();
        passwordLabel = new JLabel();
        passwordField1 = new JPasswordField();

        //======== managementPane2 ========
        {
            editUserPane.setFont(new Font("Arial", Font.PLAIN, 18));

            //---- managementTitleLabel2 ----
            titleLabel.setText("Management Services");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            editUserPane.add(titleLabel, JLayeredPane.DEFAULT_LAYER);
            titleLabel.setBounds(20, 15, 630, 85);

            //---- SignOutButton ----
            signOutButton.setText("Sign Out");
            signOutButton.setFont(new Font("Arial", Font.BOLD, 16));
            signOutButton.addActionListener(e -> logOutButton(e));
            editUserPane.add(signOutButton, JLayeredPane.DEFAULT_LAYER);
            signOutButton.setBounds(1030, 50, 160, 40);

            //---- deleteUserButton ----
            deleteUserButton.setText("Delete User");
            deleteUserButton.setFont(new Font("Arial", Font.BOLD, 16));
            deleteUserButton.addActionListener(e -> {
                try {
                    Main.getDatabaseManager().deleteUser(user);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    Main.openPane(new ManagementPane());
                }
            });
            editUserPane.add(deleteUserButton, JLayeredPane.DEFAULT_LAYER);
            deleteUserButton.setBounds(1030, 150, 160, 40);

            //---- nameColumLabel ----
            nameLabel.setText("Name");
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            editUserPane.add(nameLabel, JLayeredPane.DEFAULT_LAYER);
            nameLabel.setBounds(25, 105, 140, 30);

            //======== scrollPane1 ========
            {

                //---- jobList ----
                jobList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jobList.setModel(new AbstractListModel<String>() {
                    String[] values = {
                            "MANAGER",
                            "TECHNICIAN",
                            "HR",
                            "CUSTOMERSERVICE",
                            "IT",
                            "FINANCE"
                    };
                    @Override
                    public int getSize() { return values.length; }
                    @Override
                    public String getElementAt(int i) { return values[i]; }
                });
                jobList.setFont(new Font("Arial", Font.PLAIN, 16));
                scrollPane1.setViewportView(jobList);
            }
            editUserPane.add(scrollPane1, JLayeredPane.DEFAULT_LAYER);
            scrollPane1.setBounds(25, 250, 315, 245);

            //---- cancelButton ----
            cancelButton.setText("Cancel");
            cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
            cancelButton.addActionListener(e -> cancel(e));
            editUserPane.add(cancelButton, JLayeredPane.DEFAULT_LAYER);
            cancelButton.setBounds(730, 490, 160, 40);

            //---- submitButton ----
            submitButton.setText("Submit");
            submitButton.setFont(new Font("Arial", Font.BOLD, 16));
            submitButton.addActionListener(e -> submit(e));
            editUserPane.add(submitButton, JLayeredPane.DEFAULT_LAYER);
            submitButton.setBounds(920, 490, 160, 40);

            //---- nameColumLabel2 ----
            errorLabel.setText("Error. Please make sure all fields are filled.");
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            errorLabel.setForeground(new Color(153, 0, 0));
            errorLabel.setVisible(false);
            editUserPane.add(errorLabel, JLayeredPane.DEFAULT_LAYER);
            errorLabel.setBounds(732, 452, 425, 30);

            //---- textField1 ----
            textField1.setText(user.getUsername());
            textField1.setFont(new Font("Arial", Font.PLAIN, 16));
            editUserPane.add(textField1, JLayeredPane.DEFAULT_LAYER);
            textField1.setBounds(20, 140, 330, 45);

            //---- nameColumLabel3 ----
            nameLabel2.setText("Job");
            nameLabel2.setFont(new Font("Arial", Font.PLAIN, 16));
            editUserPane.add(nameLabel2, JLayeredPane.DEFAULT_LAYER);
            nameLabel2.setBounds(25, 215, 140, 30);

            //---- nameColumLabel4 ----
            passwordLabel.setText("Password");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            editUserPane.add(passwordLabel, JLayeredPane.DEFAULT_LAYER);
            passwordLabel.setBounds(400, 110, 145, 25);

            //---- passwordField1 ----
            passwordField1.setText("password");
            editUserPane.add(passwordField1, JLayeredPane.DEFAULT_LAYER);
            passwordField1.setBounds(400, 140, 285, 40);
        }

    }


    private JLayeredPane editUserPane;
    private JLabel titleLabel;
    private JButton signOutButton;
    private JButton deleteUserButton;
    private JLabel nameLabel;
    private JScrollPane scrollPane1;
    private JList<String> jobList;
    private JButton cancelButton;
    private JButton submitButton;
    private JLabel errorLabel;
    private JTextField textField1;
    private JLabel nameLabel2;
    private JLabel passwordLabel;
    private JPasswordField passwordField1;

}
