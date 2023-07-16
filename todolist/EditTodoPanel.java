import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditTodoPanel extends JPanel {
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel tagLabel;
    private JLabel deadlineLabel;
    private JLabel priorityLabel;
    private JLabel createdAtLabel;
    private JLabel updatedAtLabel;
    private JTextField titleTextField;
    private JTextArea contentTextArea;
    private JComboBox<String> tagComboBox;
    private JComboBox<String> priorityComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> dayComboBox;

    private Map<String, String> members;

    public EditTodoPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 40, 40));

        JPanel contentPanel = new JPanel(new GridLayout(9, 3, 5, 10)); // 9行目に追加

        JLabel idTitleLabel = new JLabel("ID:");
        idLabel = new JLabel();
        JLabel nameTitleLabel = new JLabel("Name:");
        nameLabel = new JLabel();
        JLabel titleTitleLabel = new JLabel("Title:");
        titleLabel = new JLabel();
        titleTextField = new JTextField();
        JLabel contentTitleLabel = new JLabel("Content:");
        contentLabel = new JLabel();
        contentTextArea = new JTextArea();
        JScrollPane contentScrollPane = new JScrollPane(contentTextArea);
        JLabel tagTitleLabel = new JLabel("Tag:");
        tagLabel = new JLabel();
        String[] tagOptions = { "Personal", "Work", "Education", "Health", "Finance", "Home", "Social", "Other" };
        tagComboBox = new JComboBox<>(tagOptions);
        JLabel deadlineTitleLabel = new JLabel("Deadline:");
        deadlineLabel = new JLabel();
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearComboBox = new JComboBox<>(getYearOptions());
        monthComboBox = new JComboBox<>(getMonthOptions());
        // 年と月の選択が変更されたときに日の選択肢を更新する
        yearComboBox.addActionListener(new DateSelectionListener());
        monthComboBox.addActionListener(new DateSelectionListener());
        dayComboBox = new JComboBox<>();
        datePanel.add(yearComboBox);
        datePanel.add(monthComboBox);
        datePanel.add(dayComboBox);
        JLabel priorityTitleLabel = new JLabel("Priority:");
        priorityLabel = new JLabel();
        String[] priorityOptions = { "High", "Medium-High", "Medium", "Medium-Low", "Low" };
        priorityComboBox = new JComboBox<>(priorityOptions);
        JLabel createdAtTitleLabel = new JLabel("Created At:");
        createdAtLabel = new JLabel();
        JLabel updatedAtTitleLabel = new JLabel("Updated At:");
        updatedAtLabel = new JLabel();

        contentPanel.add(idTitleLabel);
        contentPanel.add(idLabel);
        contentPanel.add(new JLabel());
        contentPanel.add(nameTitleLabel);
        contentPanel.add(nameLabel);
        contentPanel.add(new JLabel());
        contentPanel.add(titleTitleLabel);
        contentPanel.add(titleLabel);
        contentPanel.add(titleTextField);
        contentPanel.add(contentTitleLabel);
        contentPanel.add(contentLabel);
        contentPanel.add(contentScrollPane);
        contentPanel.add(tagTitleLabel);
        contentPanel.add(tagLabel);
        contentPanel.add(tagComboBox);
        contentPanel.add(deadlineTitleLabel);
        contentPanel.add(deadlineLabel);
        contentPanel.add(datePanel);
        contentPanel.add(priorityTitleLabel);
        contentPanel.add(priorityLabel);
        contentPanel.add(priorityComboBox);
        contentPanel.add(createdAtTitleLabel);
        contentPanel.add(createdAtLabel);
        contentPanel.add(new JLabel());
        contentPanel.add(updatedAtTitleLabel);
        contentPanel.add(updatedAtLabel);
        contentPanel.add(new JLabel());

        members = readMembersFromCSV("member.csv");

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        headerPanel.add(backButton, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("Edit");
        buttonPanel.add(editButton);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(EditTodoPanel.this);
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoListPanel");
        });
    }

    // 年と月の選択が変更されたときに日の選択肢を更新する
    class DateSelectionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            updateDayOptions();
        }
    }

    private Integer[] getYearOptions() {
        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[10];
        for (int i = 0; i < 10; i++) {
            years[i] = currentYear + i;
        }
        return years;
    }

    private String[] getMonthOptions() {
        return new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
    }

    private void updateDayOptions() {
        int selectedYear = (int) yearComboBox.getSelectedItem();
        int selectedMonth = Arrays.asList(getMonthOptions()).indexOf(monthComboBox.getSelectedItem()) + 1;
        int daysInMonth = LocalDate.of(selectedYear, selectedMonth, 1).lengthOfMonth();

        Integer[] days = new Integer[daysInMonth];
        for (int i = 0; i < daysInMonth; i++) {
            days[i] = i + 1;
        }

        DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>(days);
        dayComboBox.setModel(model);
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

}