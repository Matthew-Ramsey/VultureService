/*
 * Created by JFormDesigner on Sun May 08 18:20:31 BST 2022
 */

package me.Ramsey.uk.Panes;

import me.Ramsey.uk.Main;
import me.Ramsey.uk.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Matthew Ramsey
 */
public class SignInPane extends JLayeredPane {

    private JLayeredPane signInPane;
    private JFormattedTextField usernameField;
    private JLabel Title;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton signInButton;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    /**
     *  Constructor for the SignInPane class
     */
    public SignInPane() {
        initComponents();
    }

    /**
     * Used to sign in the user
     * Checks the username and password against the database
     * @param e The action event for button
     */
    private void signIn(ActionEvent e) {
        if (usernameField.getText().equals("") || Arrays.toString(passwordField.getPassword()).equals("")) {
            errorLabel.setText("Please enter a username and password");
            errorLabel.setVisible(true);
            return;
        }

        User user;
        try {
            user = Main.getDatabaseManager().getUser(usernameField.getText());
            if (user == null) {
                errorLabel.setText("Username not found. Please use a valid username");
                errorLabel.setVisible(true);
                return;
            }

            user = Main.getDatabaseManager().getUser(usernameField.getText(), String.valueOf(passwordField.getPassword()));
            if (user == null) {
                errorLabel.setText("Incorrect password. Please try again.");
                errorLabel.setVisible(true);
                return;
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        // Since we reached here, username and password is valid
        // Set main user for the program and open content pane
        Main.user = user;
        Main.openPane(Main.getJobPane());
    }

    /**
     * Initialises the components
     * Fills the page with the content
     */
    private void initComponents() {

        signInPane = this;
        Title = new JLabel();

        usernameLabel = new JLabel();
        usernameField = new JFormattedTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();

        signInButton = new JButton();
        errorLabel = new JLabel();

        //======== signInPage ========
        {

            //---- usernameField ----
            usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
            signInPane.add(usernameField, JLayeredPane.DEFAULT_LAYER);
            usernameField.setBounds(225, 195, 370, usernameField.getPreferredSize().height);

            //---- Title ----
            Title.setText("Vulture Services Task Management System");
            Title.setFont(new Font("Arial", Font.BOLD, 28));
            signInPane.add(Title, JLayeredPane.DEFAULT_LAYER);
            Title.setBounds(210, 10, 930, 125);

            //---- usernameLabel ----
            usernameLabel.setText("Username");
            usernameLabel.setFont(new Font("Arial", Font.PLAIN, 19));
            signInPane.add(usernameLabel, JLayeredPane.DEFAULT_LAYER);
            usernameLabel.setBounds(227, 160, 110, 45);

            //---- passwordLabel ----
            passwordLabel.setText("Password");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 19));
            signInPane.add(passwordLabel, JLayeredPane.DEFAULT_LAYER);
            passwordLabel.setBounds(227, 256, 110, 45);

            //---- signInButton ----
            signInButton.setText("Sign In");
            signInButton.setFont(new Font("Arial", Font.BOLD, 16));
            signInButton.addActionListener(e -> signIn(e));
            signInPane.add(signInButton, JLayeredPane.DEFAULT_LAYER);
            signInButton.setBounds(226, 326, 95, 40);
            signInPane.add(passwordField, JLayeredPane.DEFAULT_LAYER);
            passwordField.setBounds(227, 292, 365, 26);

            //---- errorLabel ----
            errorLabel.setText("Please  enter a username and passworder");
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 19));
            errorLabel.setForeground(new Color(153, 0, 0));
            errorLabel.setVisible(false);
            signInPane.add(errorLabel, JLayeredPane.DEFAULT_LAYER);
            errorLabel.setBounds(230, 380, 785, 145);
        }

    }

}
