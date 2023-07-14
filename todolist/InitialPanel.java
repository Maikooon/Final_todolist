import javax.swing.*;
import java.awt.*;

class InitialPanel extends JPanel {
    InitialPanel() {
        setLayout(null);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(250, 100, 200, 50);
        loginButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                CardLayout cardLayout = (CardLayout) ((Frame) container).getLayout();
                cardLayout.show(container, "LoginPanel");
            }
        });
        add(loginButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(250, 200, 200, 50);
        signUpButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                CardLayout cardLayout = (CardLayout) ((Frame) container).getLayout();
                cardLayout.show(container, "SignUpPanel");
            }
        });
        add(signUpButton);
    }
}
