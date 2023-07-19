import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Todo detail panel 
//shows TodoListPanel

public class DetailTodoPanel extends JPanel {
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
    private EditTodoPanel editPanel; // use foe detail panel
    private JPanel buttonPanel;

    public DetailTodoPanel() {
        //layout 
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));

        // main
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

        // add button and label to main panel
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

        // generate Back Add
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        JButton addButton = new JButton("+");
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // Edit・Archive・Delete
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("Edit");
        JButton archiveButton = new JButton("Archive");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(editButton);
        buttonPanel.add(archiveButton);
        buttonPanel.add(deleteButton);

        // layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // action 5 buttons 
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
            MyWindow myWindow = (MyWindow) frame;
            myWindow.reloadPanels();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoListPanel");
        });
        addButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "CreateTodoPanel");
        });
        editButton.addActionListener(e -> {
            String todoId = idLabel.getText();
            editPanel.loadTodoDetails(todoId);
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "EditTodoPanel");
            editPanel.loadTodoDetails(todoId);
        });
        archiveButton.addActionListener(e -> {
            int option = showConfirmationDialog("Archive Confirmation", "Are you sure you want to archive this todo?");
            if (option == JOptionPane.YES_OPTION) {
                archiveTodo();
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.reloadPanels();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoListPanel");
            }
        });
        deleteButton.addActionListener(e -> {
            int option = showConfirmationDialog("Delete Confirmation", "Are you sure you want to delete this todo?");
            if (option == JOptionPane.YES_OPTION) {
                deleteTodo();
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.reloadPanels();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoListPanel");
            }
        });

        members = readMembersFromCSV("member.csv");
    }

    //  set value before move to new page 
    // call on TodoListpanel
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

                // if not my task  
                //delete Edit,archive ,delete button 
                if (!isMyTodo(memberId)) {
                    Container container = buttonPanel.getParent();
                    container.remove(buttonPanel);
                    container.revalidate();
                    container.repaint();
                }
            } else {
                setDefaultLabels();
            }
        } else {
            setDefaultLabels();
        }
    }

    // failed label N/A
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

    // get data form member.csv
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

    // Fetches detailed data from todos.csv file for the given todoId.
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

    // whether mine or not
    private boolean isMyTodo(String todoId) {
        String loginUserId = String.valueOf(LoginPanel.user_id);
        return loginUserId.equals(userIdLabel.getText());
    }

    // archive
private void archiveTodo() {
    String todoId = idLabel.getText(); 

    try {
        File todosFile = new File("todos.csv");
        File archiveFile = new File("archive.csv");
        File tempFile = new File("temp.csv"); // trash file

        BufferedReader br = new BufferedReader(new FileReader(todosFile));
        BufferedWriter archiveBW = new BufferedWriter(new FileWriter(archiveFile, true));
        BufferedWriter tempBW = new BufferedWriter(new FileWriter(tempFile));

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data[0].equals(todoId)) {
                archiveBW.write(line); // archive -  add archive.csv
                archiveBW.newLine();
            } else {
                tempBW.write(line); // not archiv --- wite temp.csv
                tempBW.newLine();
            }
        }

        br.close();
        archiveBW.close();
        tempBW.close();

        todosFile.delete(); // trelace todos.csv to new one
        tempFile.renameTo(todosFile);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // delete 
    private void deleteTodo() {
        String todoId = idLabel.getText(); // get todoid from id label 

        try {
            File todosFile = new File("todos.csv");
            File tempFile = new File("temp.csv");

            BufferedReader br = new BufferedReader(new FileReader(todosFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                } else {
                    String[] data = line.split(",");
                    if (!data[0].equals(todoId)) {
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }
            br.close();
            bw.close();

            todosFile.delete(); // replaeve todos.csv to new one
            if (!tempFile.renameTo(new File("todos.csv"))) {
                throw new IOException("Failed to rename temp.csv to todos.csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Archive button 
    //dialog : Delete butonn is pushd 
    private int showConfirmationDialog(String title, String message) {
        return JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Yes", "Cancel" }, "Yes");
    }

    // call on MyWindow to set the data 
    public void setEditlPanel(EditTodoPanel editPanel) {
        this.editPanel = editPanel;
    }

}