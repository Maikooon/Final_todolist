import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Todo detail panel
// display todo from TodoListPanel

public class ArchiveDetailTodoPanel extends JPanel {
    private JLabel idLabel;
    private JLabel userIdLabel;
    private JLabel nameLabel;
    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel tagLabel;
    private JLabel deadlineLabel;
    private JLabel priorityLabel;
    private JLabel createdAtLabel;
    private JLabel updatedAtLabel;
    private Map<String, String> members;
    private JPanel buttonPanel;

    public ArchiveDetailTodoPanel() {
        // layout : gridlayout
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));

        // main panel
        JPanel contentPanel = new JPanel(new GridLayout(9, 2, 5, 10));

        idLabel = new JLabel();
        userIdLabel = new JLabel();
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

        // add label and field to main panel
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

        // generate Back button and add button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        JButton addButton = new JButton("+");
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // generate Unarchive button
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton unarchiveButton = new JButton("Unarchive");
        buttonPanel.add(unarchiveButton);

        // over layout
        // setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // button action
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(ArchiveDetailTodoPanel.this);
            MyWindow myWindow = (MyWindow) frame;
            myWindow.reloadPanels();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "ArchiveTodoListPanel");
        });
        addButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(ArchiveDetailTodoPanel.this);
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "CreateTodoPanel");
        });
        unarchiveButton.addActionListener(e -> {
            int option = showConfirmationDialog("Unarchive Confirmation",
                    "Are you sure you want to unarchive this todo?");
            if (option == JOptionPane.YES_OPTION) {
                archiveTodo();
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(ArchiveDetailTodoPanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.reloadPanels();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoListPanel");
            }
        });
        // input into members
        members = readMembersFromCSV("member.csv");
    }

    // set x from move to detail pane
    // call methods on TodoListpanel
    public void loadTodoDetails(String todoId) {
        String[] todoDetails = readTodoDetailsFromCSV(todoId);
        if (todoDetails != null) {
            String memberId = todoDetails[1];
            String name = members.get(memberId);
            if (name != null) {
                idLabel.setText(todoDetails[0]);
                userIdLabel.setText(todoDetails[1]);
                nameLabel.setText(name);
                titleLabel.setText(todoDetails[2]);
                contentLabel.setText(todoDetails[3]);
                tagLabel.setText(todoDetails[4]);
                deadlineLabel.setText(todoDetails[5]);
                priorityLabel.setText(todoDetails[6]);
                createdAtLabel.setText(todoDetails[7]);
                updatedAtLabel.setText(todoDetails[8]);
            } else {
                setDefaultLabels();
            }
        } else {
            setDefaultLabels();
        }
    }

    // change label
    private void setDefaultLabels() {
        idLabel.setText("N/A");
        nameLabel.setText("N/A");
        titleLabel.setText("N/A");
        contentLabel.setText("N/A");
        tagLabel.setText("N/A");
        deadlineLabel.setText("N/A");
        priorityLabel.setText("N/A");
        createdAtLabel.setText("N/A");
        updatedAtLabel.setText("N/A");
    }

    // read informatino fron member.csv
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

    // get todo info from archive.csv
    private String[] readTodoDetailsFromCSV(String todoId) {
        String fileName = "archive.csv";

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

    // archive
    private void archiveTodo() {
        String todoId = idLabel.getText(); // get todoId
        String selectedTodo = null;
        try {
            File todosFile = new File("todos.csv");
            File archiveFile = new File("archive.csv");

            try (BufferedReader br = new BufferedReader(new FileReader(archiveFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equals(todoId)) {
                        selectedTodo = line;
                        break;
                    }
                }
            }

            if (selectedTodo != null) {
                File tempFile = new File("temp.csv"); //exi file
                try (BufferedReader br = new BufferedReader(new FileReader(archiveFile));
                        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.equals(selectedTodo)) {
                            bw.write(line);
                            bw.newLine();
                        }
                    }
                }
                archiveFile.delete(); // replace new info 
                tempFile.renameTo(archiveFile);

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(todosFile, true))) {
                    bw.write(selectedTodo);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // push the UnArchive button 
    private int showConfirmationDialog(String title, String message) {
        return JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Yes", "Cancel" }, "Yes");
    }
}