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

        JButton allTodoListButton = new JButton("All ToDo List");
        allTodoListButton.setPreferredSize(new Dimension(200, 50));
        allTodoListButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MainPanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.reloadPanels();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoListPanel");
            }
        });
        buttonPanel.add(allTodoListButton);

        JButton myTodoListButton = new JButton("My ToDo List");
        myTodoListButton.setPreferredSize(new Dimension(200, 50));
        myTodoListButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MainPanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.reloadPanels();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "MyTodoListPanel");
            }
        });
        buttonPanel.add(myTodoListButton);

        JButton myPageButton = new JButton("My Page");
        myPageButton.setPreferredSize(new Dimension(200, 50));
        myPageButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                CardLayout cardLayout = (CardLayout) ((Frame) container).getLayout();
                cardLayout.show(container, "MyPagePanel");
            }
        });
        buttonPanel.add(myPageButton);
    }
}
