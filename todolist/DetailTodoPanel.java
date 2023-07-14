import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailTodoPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel tagLabel;
    private JLabel deadlineLabel;
    private JLabel priorityLabel;
    private JLabel createdAtLabel;
    private JLabel updatedAtLabel;

    private Map<String, String> members;

    public DetailTodoPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel contentPanel = new JPanel(new GridLayout(8, 2, 5, 10));

        JLabel nameTitleLabel = new JLabel("Name:");
        nameLabel = new JLabel();
        JLabel titleTitleLabel = new JLabel("Title:");
        titleLabel = new JLabel();
        JLabel contentTitleLabel = new JLabel("Content:");
        contentLabel = new JLabel();
        JLabel tagTitleLabel = new JLabel("Tag:");
        tagLabel = new JLabel();
        JLabel deadlineTitleLabel = new JLabel("Deadline:");
        deadlineLabel = new JLabel();
        JLabel priorityTitleLabel = new JLabel("Priority:");
        priorityLabel = new JLabel();
        JLabel createdAtTitleLabel = new JLabel("Created At:");
        createdAtLabel = new JLabel();
        JLabel updatedAtTitleLabel = new JLabel("Updated At:");
        updatedAtLabel = new JLabel();

        contentPanel.add(nameTitleLabel);
        contentPanel.add(nameLabel);
        contentPanel.add(titleTitleLabel);
        contentPanel.add(titleLabel);
        contentPanel.add(contentTitleLabel);
        contentPanel.add(contentLabel);
        contentPanel.add(tagTitleLabel);
        contentPanel.add(tagLabel);
        contentPanel.add(deadlineTitleLabel);
        contentPanel.add(deadlineLabel);
        contentPanel.add(priorityTitleLabel);
        contentPanel.add(priorityLabel);
        contentPanel.add(createdAtTitleLabel);
        contentPanel.add(createdAtLabel);
        contentPanel.add(updatedAtTitleLabel);
        contentPanel.add(updatedAtLabel);

        members = readMembersFromCSV("member.csv");

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        JButton addButton = new JButton("+");

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("Edit");
        JButton archiveButton = new JButton("Archive");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(editButton);
        buttonPanel.add(archiveButton);
        buttonPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoListPanel");
        });

        addButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "CreateTodoPanel");
        });
    }

    public void loadTodoDetails(String todoId) {
        String[] todoDetails = readTodoDetailsFromCSV(todoId);

        if (todoDetails != null) {
            String memberId = todoDetails[1];
            String name = members.get(memberId);
            if (name != null) {
                nameLabel.setText(name);
                titleLabel.setText(todoDetails[2]);
                contentLabel.setText(todoDetails[3]);
                tagLabel.setText(todoDetails[4]);
                deadlineLabel.setText(todoDetails[5]);
                priorityLabel.setText(todoDetails[6]);
                createdAtLabel.setText(todoDetails[7]);
                updatedAtLabel.setText(todoDetails[8]);
            } else {
                nameLabel.setText("N/A");
                titleLabel.setText("N/A");
                contentLabel.setText("N/A");
                tagLabel.setText("N/A");
                deadlineLabel.setText("N/A");
                priorityLabel.setText("N/A");
                createdAtLabel.setText("N/A");
                updatedAtLabel.setText("N/A");
            }
        } else {
            nameLabel.setText("N/A");
            titleLabel.setText("N/A");
            contentLabel.setText("N/A");
            tagLabel.setText("N/A");
            deadlineLabel.setText("N/A");
            priorityLabel.setText("N/A");
            createdAtLabel.setText("N/A");
            updatedAtLabel.setText("N/A");
        }
    }

    private Map<String, String> readMembersFromCSV(String fileName) {
        Map<String, String> members = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String memberId = data[0];
                String name = data[1];
                members.put(memberId, name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return members;
    }

    private String[] readTodoDetailsFromCSV(String todoId) {
        String fileName = "todos.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(todoId)) {
                    return data;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
