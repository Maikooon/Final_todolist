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

public class MyTodoListPanel extends JPanel {
    private List<String[]> todos;
    private List<String[]> members;
    private DetailTodoPanel detailPanel; // 詳細情報を表示するパネル

    public MyTodoListPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));

        todos = readCSV("todos.csv");
        members = readCSV("member.csv");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        for (String[] todo : todos) {
            String todoId = todo[0];
            String memberId = todo[1];
            String memberName = findMemberName(memberId);
            String title = todo[2];
            String tag = todo[4];
            String deadline = todo[5];
            String priority = todo[6];

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

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        JButton addButton = new JButton("+");

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyTodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "MainPanel");
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

    private void onTodoButtonClick(String todoId) {
        // TodoのIDを渡してDetailTodoPanelを更新
        detailPanel.loadTodoDetails(todoId);

        // パネルを表示
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyTodoListPanel.this);
        CardLayout cardLayout = (CardLayout) frame.getLayout();
        cardLayout.show(frame, "DetailTodoPanel");
    }

    public void setDetailPanel(DetailTodoPanel detailPanel) {
        this.detailPanel = detailPanel;
    }

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

    private String findMemberName(String memberId) {
        for (String[] member : members) {
            if (member[0].equals(memberId)) {
                return member[1];
            }
        }
        return "Unknown";
    }

    private boolean isMyTodo(String memberId) {
        String user_id = String.valueOf(LoginPanel.user_id);
        if (user_id.equals(memberId)) {
            return true;
        }
        return false;
    }
}