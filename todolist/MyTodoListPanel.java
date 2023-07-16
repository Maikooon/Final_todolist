import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// My Todo List
// 自分のtodoとPiblicの人のtodoが見れる

public class MyTodoListPanel extends JPanel {
    private List<String[]> todos;
    private List<String[]> members;
    private DetailTodoPanel detailPanel; // 詳細情報を表示するパネル

    public MyTodoListPanel() {
        todos = readCSV("todos.csv");
        members = readCSV("member.csv");

        // 全体のレイアウト
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));

        // 一覧（中央）部分のパネル
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // 各todoについて1つずつボタンを生成
        for (String[] todo : todos) {
            String todoId = todo[0];
            String memberId = todo[1];
            String memberName = findMemberName(memberId);
            String title = todo[2];
            String tag = todo[4];
            String deadline = todo[5];
            String priority = todo[6];
            // 自分のtodoではなかったらcontinue
            if (!isMyTodo(memberId)) {
                continue;
            }
            JButton todoButton = new JButton(memberName + ": " + title + " (" + tag + ") " + deadline + " - " + priority);
            todoButton.setPreferredSize(new Dimension(350, 50));
            todoButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onTodoButtonClick(todoId);
                }
            });
            buttonPanel.add(todoButton);
        }

        // 一覧が多い場合はスクロールできるようにする
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // ヘッダーボタン
        JButton backButton = new JButton("Back");
        JButton archiveButton = new JButton("Archive List");
        JButton addButton = new JButton("+");
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(archiveButton);
        rightButtonPanel.add(addButton);
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(rightButtonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ボタンアクション
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyTodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "MainPanel");
            }
        });
        archiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyTodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "ArchiveTodoListPanel");
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyTodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "CreateTodoPanel");
            }
        });
    }

    // ボタンが押されたらDetailTodoPanelの中身を作って遷移する
    private void onTodoButtonClick(String todoId) {
        detailPanel.loadTodoDetails(todoId);
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyTodoListPanel.this);
        CardLayout cardLayout = (CardLayout) frame.getLayout();
        cardLayout.show(frame, "DetailTodoPanel");
    }

    // MyWindowで呼び出す
    public void setDetailPanel(DetailTodoPanel detailPanel) {
        this.detailPanel = detailPanel;
    }

    // CSVファイルを読み込む
    private List<String[]> readCSV(String fileName) {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // ヘッダ行を飛ばす
                }
                String[] data = line.split(",");
                records.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    // ユーザのidからnameを検索
    private String findMemberName(String memberId) {
        for (String[] member : members) {
            if (member[0].equals(memberId)) {
                return member[1];
            }
        }
        return "Unknown";
    }

    // 自分のタスクかどうか
    private boolean isMyTodo(String memberId) {
        String user_id = String.valueOf(LoginPanel.user_id);
        if (user_id.equals(memberId)) {
            return true;
        }
        return false;
    }
}