import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.io.*;

class EditTodoPanel extends JPanel {
    private JLabel idLabel;
    private JTextField titleTextField;
    private JTextArea contentTextArea;
    private JComboBox<String> tagComboBox;
    private JComboBox<String> priorityComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> dayComboBox;

    private String todoId; // 編集対象のtodoのID
    private String userId; // 編集対象のtodoのユーザID
    private String createdAt; // 編集対象のtodoの作成日時
    

    public EditTodoPanel(String todoId) {
        this.todoId = todoId;

        setLayout(new GridLayout(9, 2, 8, 10));
        setPreferredSize(new Dimension(400, 400));

        JLabel idTextLabel = new JLabel("ID:" + todoId);
        idLabel = new JLabel();

        JLabel titleLabel = new JLabel("Title:");
        titleTextField = new JTextField();

        JLabel contentLabel = new JLabel("Content:");
        contentTextArea = new JTextArea("");
        JScrollPane contentScrollPane = new JScrollPane(contentTextArea);

        JLabel tagLabel = new JLabel("Tag:");
        String[] tagOptions = { "Personal", "Work", "Education", "Health", "Finance", "Home", "Social", "Other" };
        tagComboBox = new JComboBox<>(tagOptions);

        JLabel priorityLabel = new JLabel("Priority:");
        String[] priorityOptions = { "High", "Medium-High", "Medium", "Medium-Low", "Low" };
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

        // ラベルとフィールドをパネルに追加
        add(idTextLabel);
        add(idLabel);
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

        loadTodoDetails(todoId); // 編集対象のtodoの詳細情報を読み込んでフィールドにセット
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
            userId = todoDetails[1];
            idLabel.setText(todoId);
            titleTextField.setText(todoDetails[2]);
            contentTextArea.setText(todoDetails[3]);
            tagComboBox.setSelectedItem(todoDetails[4]);
            priorityComboBox.setSelectedItem(todoDetails[6]);

            String[] deadlineParts = todoDetails[5].split("-");
            int year = Integer.parseInt(deadlineParts[0]);
            int month = Integer.parseInt(deadlineParts[1]);
            int day = Integer.parseInt(deadlineParts[2]);
            yearComboBox.setSelectedItem(year);
            monthComboBox.setSelectedItem(getMonthName(month));
            updateDayOptions();
            dayComboBox.setSelectedItem(day);

            createdAt = todoDetails[7];
        }
    }

    private String getMonthName(int month) {
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        return months[month - 1];
    }

    // 「Save」ボタンが押されたときの処理（フィールドの値を取得してバリデーションチェックをする）
    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 入力された情報を取得
            String todoId = idLabel.getText();
            String title = titleTextField.getText();
            String content = contentTextArea.getText();
            String tag = (String) tagComboBox.getSelectedItem();
            String priority = (String) priorityComboBox.getSelectedItem();
            int selectedYear = (int) yearComboBox.getSelectedItem();
            int selectedMonth = Arrays.asList(getMonthOptions()).indexOf(monthComboBox.getSelectedItem()) + 1;
            int selectedDay = (int) dayComboBox.getSelectedItem();
            LocalDate deadline = LocalDate.of(selectedYear, selectedMonth, selectedDay);
            LocalDateTime now = LocalDateTime.now();
            String updated_at = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

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

            // CSVファイルを更新
            if (updateTodoInCSV(todoId, userId, title, content, tag, priority, deadline, createdAt, updated_at)) {
                JOptionPane.showMessageDialog(null, "Todo updated successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                Frame frame = (Frame) getParent();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.previous(frame); // 先頭のページに切り替え
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update todo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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

        private boolean updateTodoInCSV(String todoId, String userId, String title, String content, String tag,
                String priority, LocalDate deadline, String createdAt, String updated_at) {
            String fileName = "todos.csv";
            String tempFileName = "temp.csv";

            try (BufferedReader br = new BufferedReader(new FileReader(fileName));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(tempFileName))) {
                String line;
                boolean isFirstLine = true; // ヘッダ行をスキップするためのフラグ
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        bw.write(line); // ヘッダ行をそのまま書き込む
                        bw.newLine();
                        isFirstLine = false;
                        continue;
                    }
                    String[] data = line.split(",");
                    if (data.length >= 1 && data[0].equals(todoId)) {
                        // 更新対象の行の場合、新しいデータで上書きする
                        bw.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", todoId, userId, title, content, tag,
                                deadline.toString(), priority, createdAt, updated_at));
                    } else {
                        // 更新対象でない行はそのまま書き込む
                        bw.write(line);
                    }
                    bw.newLine();
                }

                // temp.csvをtodos.csvにリネームすることで更新を反映
                File oldFile = new File(fileName);
                File newFile = new File(tempFileName);
                if (!oldFile.delete()) {
                    throw new IOException("Failed to delete " + fileName);
                }
                if (!newFile.renameTo(oldFile)) {
                    throw new IOException("Failed to rename " + tempFileName + " to " + fileName);
                }

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

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
