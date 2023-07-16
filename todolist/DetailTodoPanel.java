import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DetailTodoPanel extends JPanel {
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel tagLabel;
    private JLabel deadlineLabel;
    private JLabel priorityLabel;
    private JLabel createdAtLabel;
    private JLabel updatedAtLabel;

    private Map<String, String> members;
    private EditTodoPanel editPanel;

    public DetailTodoPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel contentPanel = new JPanel(new GridLayout(9, 2, 5, 10)); // 9行目に追加

        JLabel idTitleLabel = new JLabel("ID:");
        idLabel = new JLabel();
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

        contentPanel.add(idTitleLabel);
        contentPanel.add(idLabel);
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
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoListPanel");
            }
        });
        
        deleteButton.addActionListener(e -> {
            int option = showConfirmationDialog("Delete Confirmation", "Are you sure you want to delete this todo?");
            if (option == JOptionPane.YES_OPTION) {
                deleteTodo();
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(DetailTodoPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoListPanel");
            }
        });
    }

    public void loadTodoDetails(String todoId) {
        String[] todoDetails = readTodoDetailsFromCSV(todoId);

        if (todoDetails != null) {
            String memberId = todoDetails[1];
            String name = members.get(memberId);
            if (name != null) {
                idLabel.setText(todoDetails[0]); // IDを設定
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

    private void archiveTodo() {
        String todoId = idLabel.getText(); // IDラベルからtodoIdを取得する（仮定）

        try {
            // todos.csvを読み込む
            File todosFile = new File("todos.csv");
            File archiveFile = new File("archive.csv");

            BufferedReader br = new BufferedReader(new FileReader(todosFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(archiveFile, true));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(todoId)) {
                    // アーカイブするタスクをarchive.csvに追加
                    bw.write(line);
                    bw.newLine();
                }
            }

            br.close();
            bw.close();

            // todos.csvからアーカイブしたタスクを削除する
            File tempFile = new File("temp.csv");

            br = new BufferedReader(new FileReader(todosFile));
            bw = new BufferedWriter(new FileWriter(tempFile));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(todoId)) {
                    // アーカイブしていないタスクをtemp.csvに書き込む
                    bw.write(line);
                    bw.newLine();
                }
            }

            br.close();
            bw.close();

            // todos.csvを更新した内容で置き換える
            todosFile.delete();
            tempFile.renameTo(todosFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTodo() {
        String todoId = idLabel.getText(); // IDラベルからtodoIdを取得する

        try {
            // Read todos.csv
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

            // Replace todos.csv with the updated content
            todosFile.delete();

            // Rename temp.csv to todos.csv
            if (!tempFile.renameTo(new File("todos.csv"))) {
                throw new IOException("Failed to rename temp.csv to todos.csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int showConfirmationDialog(String title, String message) {
        return JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Yes", "Cancel" }, "Yes");
    }

    public void setEditlPanel(EditTodoPanel editPanel) {
        this.editPanel = editPanel;
    }

}