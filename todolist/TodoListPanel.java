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

public class TodoListPanel extends JPanel {
    private List<String[]> todos;
    private List<String[]> members;
    private DetailTodoPanel detailPanel; // 詳細情報を表示するパネル

    public TodoListPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        todos = readCSV("todos.csv");
        members = readCSV("member.csv");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        for (String[] todo : todos) {
            String todoId = todo[0];
            String memberId = todo[1];
            String memberName = findMemberName(memberId);
            String title = todo[2];
            String tag = todo[3];
            String deadline = todo[4];
            String priority = todo[5];

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
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(TodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "TodoPanel");
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(TodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "CreateTodoPanel");
            }
        });
    }

    private void onTodoButtonClick(String todoId) {
        // TodoのIDを渡してDetailTodoPanelを更新
        detailPanel.loadTodoDetails(todoId);

        // パネルを表示
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(TodoListPanel.this);
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
}