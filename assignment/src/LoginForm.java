import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.zimlewis.*;

public class LoginForm extends JPanel{
    
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    Signal onLoginPressed = new Signal();


    
    public LoginForm(){
        setLayout(null);
        setBorder(new TitledBorder("Login"));

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");

        // Change components dimenstion
        usernameLabel.setBounds(120, 200, 100, 20);
        passwordLabel.setBounds(120, 240, 100, 20);

        usernameField.setBounds(220, 200, 250, 20);
        passwordField.setBounds(220, 240, 250, 20);

        loginButton.setBounds(120 , 280 , 110 + 120 + 120 , 40);

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onLoginPressed.emitSignal();
            }
        });

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

    }
}
