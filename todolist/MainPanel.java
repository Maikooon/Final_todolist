import javax.swing.*;
import java.awt.*;

class MainPanel extends JPanel {
    MainPanel() {
        setLayout(new BorderLayout());
        // move to toDoList、MytoDoList,My Profile

        JPanel centerPanel = new JPanel(new GridBagLayout());
        add(centerPanel, BorderLayout.CENTER);

        //add "HOME"  label
        JLabel titleLabel = new JLabel("HOME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.insets = new Insets(0, 0, 30, 0);
        centerPanel.add(titleLabel, titleConstraints);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 2;
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

        JButton MyTodoListButton = new JButton("My ToDo List");
        MyTodoListButton.setPreferredSize(new Dimension(200, 50));
        MyTodoListButton.addActionListener(e -> {
            Container container = this.getParent();
            if (container instanceof Frame) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MainPanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.reloadPanels();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "MyTodoListPanel");
            }
        });
        buttonPanel.add(MyTodoListButton);

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
