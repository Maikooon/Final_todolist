import javax.swing.*;
import java.awt.*;

class MainPanel extends JPanel {
    MainPanel() {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 1;
        centerPanel.add(buttonPanel, buttonConstraints);

        JButton loginButton = new JButton("My ToDo List");
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                CardLayout cardLayout = (CardLayout) ((Frame) container).getLayout();
                cardLayout.show(container, "TodoListPanel");
            }
        });
        buttonPanel.add(loginButton);

        JButton signUpButton = new JButton("Logout");
        signUpButton.setPreferredSize(new Dimension(200, 50));
        signUpButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                CardLayout cardLayout = (CardLayout) ((Frame) container).getLayout();
                cardLayout.show(container, "LoginPanel");
            }
        });
        buttonPanel.add(signUpButton);
    }
}
