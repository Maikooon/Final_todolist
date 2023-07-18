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
import java.util.*;

// My Todo List
public class MyTodoListPanel extends JPanel {
    private List<String[]> todos;
    private List<String[]> members;
    private DetailTodoPanel detailPanel; // 詳細情報を表示するパネル
    private JPanel ToDoPanel = new JPanel();
    private JPanel LeftSelectPanel = new JPanel();

    public MyTodoListPanel() {
        todos = readCSV("todos.csv");
        members = readCSV("member.csv");

        // 全体のレイアウト
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 70, 20, 70));

        // 一覧（中央）部分のパネルは、左右で分かれており、左側がLeftSelectPanel.右側がToDoPanelとなっている
        LeftSelectPanel.setLayout(new BoxLayout(LeftSelectPanel, BoxLayout.Y_AXIS));
        ToDoPanel.setLayout(new GridLayout(15,1));


        //sortについてDeadLineとPriorityの二つのボタンを生成
        LeftSelectPanel.add(new JLabel(" "));
        LeftSelectPanel.add(new JLabel(" "));
        LeftSelectPanel.add(new JLabel(" "));
        JLabel sortTitle = new JLabel("SORT");
        LeftSelectPanel.add(sortTitle);
        JCheckBox DeadLineCheckBox = new JCheckBox("DeadLine");
        DeadLineCheckBox.setSelected(false);
        LeftSelectPanel.add(DeadLineCheckBox);
        JCheckBox PriorityCheckBox = new JCheckBox("Priority");
        PriorityCheckBox.setSelected(false);
        LeftSelectPanel.add(PriorityCheckBox);

        //OKボタン作成
        LeftSelectPanel.add(new JLabel(" "));//空白行
        LeftSelectPanel.add(new JLabel(" "));//空白行
        JButton SelectOKButton = new JButton("OK");
        SelectOKButton.setPreferredSize(new Dimension(300, 200));
        LeftSelectPanel.add(SelectOKButton);
        displayTodos();


        // OKボタンが押されたとき各todoについて1つずつボタンを生成
        SelectOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (DeadLineCheckBox.isSelected() && PriorityCheckBox.isSelected()){
                    sortTodosBoth();//Priority、DeadLineどちらもチェックが入った場合
                }else if (DeadLineCheckBox.isSelected()){
                    sortTodosDeadLine();//DeadLineのみチェックが入った場合
                }else if (PriorityCheckBox.isSelected()){
                    sortTodosPriority();//Priorityのみチェックが入った場合
                }
                displayTodos();//結果を表示する
            }
        });


        // 一覧が多い場合はスクロールできるようにする
        JScrollPane scrollPane1 = new JScrollPane(LeftSelectPanel);
        scrollPane1.setBorder(null);
        JScrollPane scrollPane2 = new JScrollPane(ToDoPanel);
        scrollPane2.setBorder(null);

        add(scrollPane1, BorderLayout.WEST);
        add(scrollPane2, BorderLayout.CENTER);

        // ヘッダーボタン
        JButton backButton = new JButton("Back");
        JButton archiveButton = new JButton("Archive List");
        JButton addButton = new JButton("+");
        JLabel titleLabel = new JLabel("My Todo List");
        titleLabel.setFont(new Font("Arial", Font.BOLD,30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(archiveButton);
        rightButtonPanel.add(addButton);
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
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

    //Priorityを数値にマッピングする
    private int priorityToInt(String priority) {
        switch (priority) {
            case "High": return 5;
            case "Medium-High": return 4;
            case "Medium": return 3;
            case "Medium-Low": return 2;
            case "Low": return 1;
            default: return 0;
        }
    }

    //sort関数の実装
    public void sortTodosBoth() {
        Collections.sort(todos, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                int priorityComparison = Integer.compare(priorityToInt(todo2[6]), priorityToInt(todo1[6])); // compare priority
                if (priorityComparison == 0) { // if priorities are the same, compare deadline
                    return todo1[5].compareTo(todo2[5]); // compare deadline
                } else {
                    return priorityComparison;
                }
            }
        });
    }

    public void sortTodosDeadLine() {
        Collections.sort(todos, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                return todo1[5].compareTo(todo2[5]); // compare deadline
            }
        });
    }   

    public void sortTodosPriority() {
        Collections.sort(todos, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                return Integer.compare(priorityToInt(todo2[6]), priorityToInt(todo1[6])); // compare priority
        }
        });
    }

    //todoSelected(絞り込み済み)を表示する
    private void displayTodos() {
        ToDoPanel.removeAll();  // Important to remove old buttons
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
            String buttonLabel = String.format("%-15s: %-20s (%-10s) %-15s - %s", memberName, title, tag, deadline, priority);
            JButton todoButton = new JButton(buttonLabel);
            todoButton.setPreferredSize(new Dimension(500, 50));
            todoButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onTodoButtonClick(todoId);
                }
            });
            ToDoPanel.add(todoButton);
        }
        ToDoPanel.revalidate();
        ToDoPanel.repaint();
        }
    
}