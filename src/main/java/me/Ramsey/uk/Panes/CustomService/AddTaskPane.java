/*
 * Created by JFormDesigner on Sun May 08 21:18:30 BST 2022
 */

package me.Ramsey.uk.Panes.CustomService;

import me.Ramsey.uk.Main;
import me.Ramsey.uk.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Matthew Ramsey
 */
public class AddTaskPane extends JLayeredPane {

    private HashMap<String, User> tasks;

    public AddTaskPane(HashMap<String, User> tasks) {
        this.tasks = tasks;
        initComponents();
    }

    private void initComponents() {

        addTaskPane = this;
        taskContentField = new JFormattedTextField();
        usernameLabel = new JLabel();
        submitButton = new JButton();
        errorLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        list1 = new JList();

        //======== signInPage ========
        {

            //---- taskContentField ----
            taskContentField.setFont(new Font("Arial", Font.PLAIN, 14));
            addTaskPane.add(taskContentField, JLayeredPane.DEFAULT_LAYER);
            taskContentField.setBounds(15, 75, 770, 410);

            //---- usernameLabel ----
            usernameLabel.setText("Add Task");
            usernameLabel.setFont(new Font("Arial", Font.PLAIN, 19));
            addTaskPane.add(usernameLabel, JLayeredPane.DEFAULT_LAYER);
            usernameLabel.setBounds(15, 15, 110, 45);

            //---- submitButton ----
            submitButton.setText("Submit");
            submitButton.setFont(new Font("Arial", Font.BOLD, 16));
            submitButton.addActionListener(e -> {
                if (taskContentField.getText().isEmpty() || list1.getSelectedValue() == null) {
                    errorLabel.setText("Please ensure all fields are filled");
                    return;
                }

                //return to customer service page
                HashMap<String, User> tasks = this.tasks;
                try {
                    tasks.put(taskContentField.getText(), Main.getDatabaseManager().getUser(list1.getSelectedValue()));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                Main.openPane(new AddNewJobPane(tasks));

            });
            addTaskPane.add(submitButton, JLayeredPane.DEFAULT_LAYER);
            submitButton.setBounds(1060, 510, 105, 45);

            //---- errorLabel ----
            errorLabel.setText("Please  enter a username and password");
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 19));
            errorLabel.setForeground(new Color(153, 0, 0));
            errorLabel.setVisible(false);
            addTaskPane.add(errorLabel, JLayeredPane.DEFAULT_LAYER);
            errorLabel.setBounds(230, 380, 785, 145);

            //======== scrollPane1 ========
            {

                //---- list1 ----
                list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list1.setModel(new AbstractListModel<String>() {
                    final String[] values;
                    {
                        try {

                            ArrayList<User> technicians = Main.getDatabaseManager().getAllUsersOfTechnician();
                            values = new String[technicians.size()];
                            for (int i = 0; i < technicians.size(); i++) {
                                values[i] = technicians.get(i).getUsername();
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public int getSize() { return values.length; }
                    @Override
                    public String getElementAt(int i) { return values[i]; }
                });
                scrollPane1.setViewportView(list1);
            }
            addTaskPane.add(scrollPane1, JLayeredPane.DEFAULT_LAYER);
            scrollPane1.setBounds(805, 80, 360, 400);
        }

    }

    private JLayeredPane addTaskPane;
    private JFormattedTextField taskContentField;
    private JLabel usernameLabel;
    private JButton submitButton;
    private JLabel errorLabel;
    private JScrollPane scrollPane1;
    private JList<String> list1;

}
