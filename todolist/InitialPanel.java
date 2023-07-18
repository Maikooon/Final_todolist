import javax.swing.*;
import java.awt.*;

//  Initial panel

class InitialPanel extends JPanel {
    InitialPanel() {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        add(centerPanel, BorderLayout.CENTER);

        // title
        JLabel titleLabel = new JLabel("Welcome ToDo List!!!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.insets = new Insets(0, 0, 30, 0);
        centerPanel.add(titleLabel, titleConstraints);

        // button panel 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 1;
        centerPanel.add(buttonPanel, buttonConstraints);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                CardLayout cardLayout = (CardLayout) ((Frame) container).getLayout();
                cardLayout.show(container, "LoginPanel");
            }
        });
        buttonPanel.add(loginButton);

        // Sign Up  buttn
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(200, 50));
        signUpButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                CardLayout cardLayout = (CardLayout) ((Frame) container).getLayout();
                cardLayout.show(container, "SignUpPanel");
            }
        });
        buttonPanel.add(signUpButton);
    }
}
