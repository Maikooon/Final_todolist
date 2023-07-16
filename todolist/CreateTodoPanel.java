import javax.swing.*;
import javax.swing.border.EmptyBorder;

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

    // ラベルとボタンの配置
    public CreateTodoPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 40, 40));
        
        JPanel contentPanel = new JPanel(new GridLayout(9, 2, 5, 10)); // 9行目に追加

        JLabel titleLabel = new JLabel("Title:");
        titleTextField = new JTextField();

        JLabel contentLabel = new JLabel("Content:");
        contentTextArea = new JTextArea();
        JScrollPane contentScrollPane = new JScrollPane(contentTextArea);

        JLabel tagLabel = new JLabel("Tag:");
        String[] tagOptions = { "Personal", "Work", "Education", "Health", "Finance", "Home", "Social", "Other" };
        tagComboBox = new JComboBox<>(tagOptions);

        JLabel priorityLabel = new JLabel("Priority:");
        String[] priorityOptions = { "High", "Medium-High", "Medium", "Medium-Low", "Low" };
        priorityComboBox = new JComboBox<>(priorityOptions);

        JLabel deadlineLabel = new JLabel("Deadline:");

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearComboBox = new JComboBox<>(getYearOptions());
        monthComboBox = new JComboBox<>(getMonthOptions());
        dayComboBox = new JComboBox<>();
        datePanel.add(yearComboBox);
        datePanel.add(monthComboBox);
        datePanel.add(dayComboBox);

        // 年と月の選択が変更されたときに日の選択肢を更新する
        yearComboBox.addActionListener(new DateSelectionListener());
        monthComboBox.addActionListener(new DateSelectionListener());

        // テキストボックスとコンボボックスをパネルに追加
        contentPanel.add(titleLabel);
        contentPanel.add(titleTextField);
        contentPanel.add(contentLabel);
        contentPanel.add(contentScrollPane);
        contentPanel.add(tagLabel);
        contentPanel.add(tagComboBox);
        contentPanel.add(deadlineLabel);
        contentPanel.add(datePanel);
        contentPanel.add(priorityLabel);
        contentPanel.add(priorityComboBox);

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        headerPanel.add(backButton, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        buttonPanel.add(saveButton);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(new SaveButtonListener());

        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.previous(frame); // 先頭のページに切り替え
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

    // 「Save」ボタンが押されたときの処理（フィールドの値を取得してバリデーションチェックをする）
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
                JOptionPane.showMessageDialog(null, "Title should be between 3 and 30 characters.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!validateContent(content)) {
                JOptionPane.showMessageDialog(null, "Content should be between 3 and 300 characters.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!validateTag(tag)) {
                JOptionPane.showMessageDialog(null, "Please select a tag.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!validateDeadline(deadline)) {
                JOptionPane.showMessageDialog(null, "Please select a deadline that is today or later.", "Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }
            if (!validatePriority(priority)) {
                JOptionPane.showMessageDialog(null, "Please select a priority.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // IDの付与
            int id = assignId();

            // CSVファイルに保存
            try {
                FileWriter writer = new FileWriter("todos.csv", true);
                writer.write(toCSV(id, user_id, title, content, tag, priority, deadline, created_at, updated_at));
                writer.write("\n");
                writer.close();
                JOptionPane.showMessageDialog(null, "Saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                Frame frame = (Frame) getParent();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoListPanel");
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
            return deadline != null && deadline.isAfter(now); // 入力された締切日が今日より後の日付かどうかチェック
        }

        private boolean validatePriority(String priority) {
            return priority != null && !priority.isEmpty();
        }

        // 現在のtodoの中で最もidの値が大きいtodoのidに1を足したidを新規todoに付与する（1から昇順にidを付与する）
        private int assignId() {
            int maxId = 0;
            try {
                BufferedReader br = new BufferedReader(new FileReader("todos.csv"));
                String line;
                boolean isFirstLine = true; // ヘッダ行をスキップするためのフラグ
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // ヘッダ行をスキップ
                    }
                    String[] data = line.split(",");
                    if (data.length >= 1) {
                        int id = Integer.parseInt(data[0]);
                        if (id > maxId) {
                            maxId = id;
                        }
                    }
                }
                br.close();
                br = new BufferedReader(new FileReader("archive.csv"));
                isFirstLine = true; // ヘッダ行をスキップするためのフラグ
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // ヘッダ行をスキップ
                    }
                    String[] data = line.split(",");
                    if (data.length >= 1) {
                        int id = Integer.parseInt(data[0]);
                        if (id > maxId) {
                            maxId = id;
                        }
                    }
                }
                br.close();
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
            return maxId + 1;
        }
    }

    // CSVにtodoを保存
    String toCSV(int id, int user_id, String title, String content, String tag, String priority, LocalDate deadline,
            String created_at, String updated_at) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String deadlineFormatted = deadline.format(formatter);
        return String.format("%d,%d,%s,%s,%s,%s,%s,%s,%s", id, user_id, title, content, tag, deadlineFormatted,
                priority, created_at, updated_at);
    }
}