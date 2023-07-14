import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.io.*;

class CreateTodoPanel extends JPanel {
    private JTextField titleTextField;
    private JTextArea contentTextArea;
    private JComboBox<String> tagComboBox;
    private JComboBox<String> priorityComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> dayComboBox;

    public CreateTodoPanel() {
        setLayout(new GridLayout(8, 2, 8, 10));
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.white);

        JLabel titleLabel = new JLabel("Title:");
        titleTextField = new JTextField();

        JLabel contentLabel = new JLabel("Content:");
        contentTextArea = new JTextArea();
        JScrollPane contentScrollPane = new JScrollPane(contentTextArea);

        JLabel tagLabel = new JLabel("Tag:");
        String[] tagOptions = {"School", "Work", "Housework", "Other"};
        tagComboBox = new JComboBox<>(tagOptions);

        JLabel priorityLabel = new JLabel("Priority:");
        String[] priorityOptions = {"High", "Medium-High", "Medium", "Medium-Low", "Low"};
        priorityComboBox = new JComboBox<>(priorityOptions);

        JLabel deadlineLabel = new JLabel("Deadline:");
        JLabel yearLabel = new JLabel("Year:");
        JLabel monthLabel = new JLabel("Month:");
        JLabel dayLabel = new JLabel("Day:");

        yearComboBox = new JComboBox<>(getYearOptions());
        monthComboBox = new JComboBox<>(getMonthOptions());
        dayComboBox = new JComboBox<>();

        // 年と月の選択が変更されたときに日の選択肢を更新する
        yearComboBox.addActionListener(new DateSelectionListener());
        monthComboBox.addActionListener(new DateSelectionListener());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.previous(frame); // 先頭のページに切り替え
        });

        // テキストボックスとコンボボックスをパネルに追加
        add(titleLabel);
        add(titleTextField);
        add(contentLabel);
        add(contentScrollPane);
        add(tagLabel);
        add(tagComboBox);
        add(deadlineLabel);
        add(yearLabel);
        add(yearComboBox);
        add(monthLabel);
        add(monthComboBox);
        add(dayLabel);
        add(dayComboBox);
        add(priorityLabel);
        add(priorityComboBox);
        add(saveButton);
        add(backButton);
    }

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
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
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

    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 入力された情報を取得
            int user_id = LoginPanel.user_id;
            String title = titleTextField.getText();
            String content = contentTextArea.getText();
            String tag = (String) tagComboBox.getSelectedItem();
            String priority = (String) priorityComboBox.getSelectedItem();
            int selectedYear = (int) yearComboBox.getSelectedItem();
            int selectedMonth = Arrays.asList(getMonthOptions()).indexOf(monthComboBox.getSelectedItem()) + 1;
            int selectedDay = (int) dayComboBox.getSelectedItem();
            LocalDate deadline = LocalDate.of(selectedYear, selectedMonth, selectedDay);
            LocalDateTime now = LocalDateTime.now();
            String created_at = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String updated_at = created_at;

            // バリデーションチェック
            if (!validateTitle(title)) {
                JOptionPane.showMessageDialog(null, "Title should be between 3 and 30 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!validateContent(content)) {
                JOptionPane.showMessageDialog(null, "Content should be between 3 and 300 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!validateTag(tag)) {
                JOptionPane.showMessageDialog(null, "Please select a tag.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!validateDeadline(deadline)) {
                JOptionPane.showMessageDialog(null, "Please select a deadline that is today or later.", "Error", JOptionPane.ERROR_MESSAGE);

                return;
            }
            if (!validatePriority(priority)) {
                JOptionPane.showMessageDialog(null, "Please select a priority.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CSVファイルに保存
            try {
                FileWriter writer = new FileWriter("todos.csv", true);
                writer.write(toCSV(user_id, title, content, tag, priority, deadline, created_at, updated_at));
                writer.write("\n");
                writer.close();
                JOptionPane.showMessageDialog(null, "Saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // 入力フィールドをクリア
            titleTextField.setText("");
            contentTextArea.setText("");
            tagComboBox.setSelectedIndex(0);
            priorityComboBox.setSelectedIndex(0);
            yearComboBox.setSelectedIndex(0);
            monthComboBox.setSelectedIndex(0);
            dayComboBox.setSelectedIndex(0);
        }

        private boolean validateTitle(String title) {
            return title.length() >= 3 && title.length() <= 30;
        }

        private boolean validateContent(String content) {
            return content.length() >= 3 && content.length() <= 300;
        }

        private boolean validateTag(String tag) {
            return tag != null && !tag.isEmpty();
        }

        private boolean validateDeadline(LocalDate deadline) {
            LocalDate now = LocalDate.now();
            return deadline != null && deadline.isAfter(now);
        }

        private boolean validatePriority(String priority) {
            return priority != null && !priority.isEmpty();
        }
    }

    String toCSV(int user_id, String title, String content, String tag, String priority, LocalDate deadline, String created_at, String updated_at) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String deadlineFormatted = deadline.format(formatter);
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", user_id, title, content, tag, deadlineFormatted, priority, created_at, updated_at);
    }
}
