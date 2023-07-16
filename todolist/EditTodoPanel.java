import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Todoの編集画面
// 画面の生成部分：DetailTodoPanelとほとんど同じ
// データの保存部分：CreateTodoPanelとほとんど同じ

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
        // レイアウト部分：ボーダーレイアウトの中にグリッドレイアウト
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 40, 40));

        // メインのパネル
        JPanel contentPanel = new JPanel(new GridLayout(9, 3, 5, 10));

        JLabel idTitleLabel = new JLabel("Task ID:");
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

        // 日付選択部分を1つのパネルにまとめる
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearComboBox = new JComboBox<>(getYearOptions());
        monthComboBox = new JComboBox<>(getMonthOptions());
        yearComboBox.addActionListener(new DateSelectionListener());
        monthComboBox.addActionListener(new DateSelectionListener());
        dayComboBox = new JComboBox<>(); // 年と月の選択が変更されたときに日の選択肢を更新する
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

        // メインのパネルにラベルとフィールドを追加
        contentPanel.add(idTitleLabel);
        contentPanel.add(idLabel);
        contentPanel.add(new JLabel()); // レイアウト調整のための空のラベル
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

        // Backボタンの生成
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        headerPanel.add(backButton, BorderLayout.WEST);

        // Saveボタンの生成
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(saveButton);

        // 全体のレイアウト
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ボタンアクション
        saveButton.addActionListener(new SaveButtonListener());
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(EditTodoPanel.this);
            MyWindow myWindow = (MyWindow) frame;
            myWindow.reloadPanels();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoListPanel");
        });

        // インスタンス変数membersに値を代入
        members = readMembersFromCSV("member.csv");
    }

    // 年と月の選択が変更されたときに日の選択肢を更新する
    class DateSelectionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            updateDayOptions();
        }
    }

    // 年のセレクトボックス：現在の西暦から10年分のボタンを生成
    private Integer[] getYearOptions() {
        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[10];
        for (int i = 0; i < 10; i++) {
            years[i] = currentYear + i;
        }
        return years;
    }

    // 月のセレクトボックス：英語表記
    private String[] getMonthOptions() {
        return new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
    }

    // 日のセレクトボックス：年月に合わせて何日まで表示するかを調節
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

    // Saveボタンが押されたときの処理
    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // フィールドの値が入力されていなかったら元のデータを代入
            int id = Integer.valueOf(idLabel.getText());
            int user_id = LoginPanel.user_id;
            String title = titleTextField.getText();
            title = (title == null || title.isEmpty()) ? titleLabel.getText() : title;
            String content = contentTextArea.getText();
            content = (content == null || content.isEmpty()) ? contentLabel.getText() : content;
            String tag = (String) tagComboBox.getSelectedItem();
            tag = (tag == null || title.isEmpty()) ? tagLabel.getText() : tag;
            String priority = (String) priorityComboBox.getSelectedItem();
            priority = (priority == null || priority.isEmpty()) ? priorityLabel.getText() : priority;
            int selectedYear = yearComboBox.getSelectedItem() != null ? (int) yearComboBox.getSelectedItem() : 0;
            int selectedMonth = monthComboBox.getSelectedItem() != null ? Arrays.asList(getMonthOptions()).indexOf(monthComboBox.getSelectedItem()) + 1 : 0;
            int selectedDay = dayComboBox.getSelectedItem() != null ? (int) dayComboBox.getSelectedItem() : 0;
            LocalDate deadline = selectedYear != 0 && selectedMonth != 0 && selectedDay != 0 ? LocalDate.of(selectedYear, selectedMonth, selectedDay) : null;
            if (deadline == null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                deadline = LocalDate.parse(deadlineLabel.getText(), formatter);
            }
            LocalDateTime now = LocalDateTime.now();
            String created_at = createdAtLabel.getText();
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
            if (!validateDeadline(deadline)) {
                JOptionPane.showMessageDialog(null, "Please select a deadline that is today or later.", "Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            // todos.csvから一度削除
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
                        if (!data[0].equals(String.valueOf(id))) {
                            bw.write(line);
                            bw.newLine();
                        }
                    }
                }
                br.close();
                bw.close();

                todosFile.delete(); // todos.csvを更新した内容で置き換える
                if (!tempFile.renameTo(new File("todos.csv"))) {
                    throw new IOException("Failed to rename temp.csv to todos.csv");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // todos.csvに再度保存
            try {
                FileWriter writer = new FileWriter("todos.csv", true);
                writer.write(toCSV(id, user_id, title, content, tag, priority, deadline, created_at, updated_at));
                writer.write("\n");
                writer.close();
                JOptionPane.showMessageDialog(null, "Saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(EditTodoPanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.reloadPanels();
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

        // バリデーションの内容
        private boolean validateTitle(String title) {
            return title.length() >= 3 && title.length() <= 30;
        }

        private boolean validateContent(String content) {
            return content.length() >= 3 && content.length() <= 300;
        }

        private boolean validateDeadline(LocalDate deadline) {
            LocalDate now = LocalDate.now();
            return deadline.isAfter(now); // 入力された締切日が今日以降の日付かチェック
        }

    }

    // CSVに保存するためのデータ整形
    private String toCSV(int id, int user_id, String title, String content, String tag, String priority, LocalDate deadline,
            String created_at, String updated_at) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String deadlineFormatted = deadline.format(formatter);
        return String.format("%d,%d,%s,%s,%s,%s,%s,%s,%s", id, user_id, title, content, tag, deadlineFormatted,
                priority, created_at, updated_at);
    }

    // 編集画面に遷移する前に値をセットする
    // DetailTodoPanelでこのメソッドを呼び出している
    public void loadTodoDetails(String todoId) {
        String[] todoDetails = readTodoDetailsFromCSV(todoId);
        if (todoDetails != null) {
            String memberId = todoDetails[1];
            String name = members.get(memberId);
            if (name != null) {
                idLabel.setText(todoDetails[0]);
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

    // データの不整合が起きたときにラベルをN/Aにする
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

    // 引数でmember.csvを受け取ってユーザ情報を読み込む
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

    // 引数でtodoIdを受け取ってtodos.csvからTodoの詳細情報を読み込む
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