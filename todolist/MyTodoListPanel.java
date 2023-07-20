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
    private DetailTodoPanel detailPanel; // show detail panel
    private JPanel ToDoPanel = new JPanel();
    private JPanel LeftSelectPanel = new JPanel();
    private List<String[]> todosTagSelected = new ArrayList<>();
    private List<TagCheckBox> tagCheckBoxes = new ArrayList<>();

    // define tag class
    class TagCheckBox extends JCheckBox {
        private String tag;

        public TagCheckBox(String tag) {
            super(tag);
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    public MyTodoListPanel() {
        todos = readCSV("todos.csv");
        members = readCSV("member.csv");

        // overall layout
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 70, 20, 70));

        // left panel: LeftSelectPanel.
        // light panel :ToDoPanel
        LeftSelectPanel.setLayout(new BoxLayout(LeftSelectPanel, BoxLayout.Y_AXIS));
        ToDoPanel.setLayout(new GridLayout(100, 1));

        // generate tag button
        LeftSelectPanel.add(new JLabel(" "));
        JLabel tagTitle = new JLabel("TAG");
        LeftSelectPanel.add(tagTitle);
        TagCheckBox PersonalCheckBox = new TagCheckBox("Personal");
        PersonalCheckBox.setSelected(true);
        LeftSelectPanel.add(PersonalCheckBox);
        tagCheckBoxes.add(PersonalCheckBox);
        TagCheckBox WorkCheckBox = new TagCheckBox("Work");
        WorkCheckBox.setSelected(true);
        LeftSelectPanel.add(WorkCheckBox);
        tagCheckBoxes.add(WorkCheckBox);
        TagCheckBox EducationCheckBox = new TagCheckBox("Education");
        EducationCheckBox.setSelected(true);
        LeftSelectPanel.add(EducationCheckBox);
        tagCheckBoxes.add(EducationCheckBox);
        TagCheckBox HealthCheckBox = new TagCheckBox("Health");
        HealthCheckBox.setSelected(true);
        LeftSelectPanel.add(HealthCheckBox);
        tagCheckBoxes.add(HealthCheckBox);
        TagCheckBox FinanceCheckBox = new TagCheckBox("Finance");
        FinanceCheckBox.setSelected(true);
        LeftSelectPanel.add(FinanceCheckBox);
        tagCheckBoxes.add(FinanceCheckBox);
        TagCheckBox HomeCheckBox = new TagCheckBox("Home");
        HomeCheckBox.setSelected(true);
        LeftSelectPanel.add(HomeCheckBox);
        tagCheckBoxes.add(HomeCheckBox);
        TagCheckBox SocialCheckBox = new TagCheckBox("Social");
        SocialCheckBox.setSelected(true);
        LeftSelectPanel.add(SocialCheckBox);
        tagCheckBoxes.add(SocialCheckBox);
        TagCheckBox OtherCheckBox = new TagCheckBox("Other");
        OtherCheckBox.setSelected(true);
        LeftSelectPanel.add(OtherCheckBox);
        tagCheckBoxes.add(OtherCheckBox);

        // generate button for sort: DeadLine & Priority button
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

        // OK button
        LeftSelectPanel.add(new JLabel(" "));//
        LeftSelectPanel.add(new JLabel(" "));//
        JButton SelectOKButton = new JButton("OK");
        SelectOKButton.setPreferredSize(new Dimension(200, 200));
        LeftSelectPanel.add(SelectOKButton);
        filterTagTodos();
        displayTodos();

        // generate OK button when pushed todo
        SelectOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterTagTodos();
                if (DeadLineCheckBox.isSelected() && PriorityCheckBox.isSelected()) {
                    sortTodosBoth();// both Priority,DeadLine
                } else if (DeadLineCheckBox.isSelected()) {
                    sortTodosDeadLine();// only DeadLine
                } else if (PriorityCheckBox.isSelected()) {
                    sortTodosPriority();// only Priority
                }
                displayTodos();// show result
            }
        });

        // can scroll
        JScrollPane scrollPane1 = new JScrollPane(LeftSelectPanel);
        scrollPane1.setBorder(null);
        JScrollPane scrollPane2 = new JScrollPane(ToDoPanel);
        scrollPane2.setBorder(null);

        add(scrollPane1, BorderLayout.WEST);
        add(scrollPane2, BorderLayout.CENTER);

        // header button
        JButton backButton = new JButton("Back");
        JButton archiveButton = new JButton("Archive List");
        JButton addButton = new JButton("+");
        JLabel titleLabel = new JLabel("My Todo List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
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

        // button action
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

    // push button - generate DetailTodoPanel - move
    private void onTodoButtonClick(String todoId) {
        detailPanel.loadTodoDetails(todoId);
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyTodoListPanel.this);
        CardLayout cardLayout = (CardLayout) frame.getLayout();
        cardLayout.show(frame, "DetailTodoPanel");
    }

    // call MyWindow
    public void setDetailPanel(DetailTodoPanel detailPanel) {
        this.detailPanel = detailPanel;
    }

    // read CSV file
    private List<String[]> readCSV(String fileName) {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // skip header
                }
                String[] data = line.split(",");
                records.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    // search id for name
    private String findMemberName(String memberId) {
        for (String[] member : members) {
            if (member[0].equals(memberId)) {
                return member[1];
            }
        }
        return "Unknown";
    }

    // whether mine or not
    private boolean isMyTodo(String memberId) {
        String user_id = String.valueOf(LoginPanel.user_id);
        if (user_id.equals(memberId)) {
            return true;
        }
        return false;
    }

    // sort by Tag
    private void filterTagTodos() {
        todosTagSelected.clear();
        for (TagCheckBox tagCheckBox : tagCheckBoxes) {
            if (tagCheckBox.isSelected()) {
                for (String[] todo : todos) {
                    if (todo[4].equals(tagCheckBox.tag)) {
                        todosTagSelected.add(todo);
                    }
                }
            }
        }
    }

    // Mapping Priority
    private int priorityToInt(String priority) {
        switch (priority) {
            case "High":
                return 5;
            case "Medium-High":
                return 4;
            case "Medium":
                return 3;
            case "Medium-Low":
                return 2;
            case "Low":
                return 1;
            default:
                return 0;
        }
    }

    // sort-func
    public void sortTodosBoth() {
        Collections.sort(todosTagSelected, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                int priorityComparison = Integer.compare(priorityToInt(todo2[6]), priorityToInt(todo1[6])); // compare
                                                                                                            // priority
                if (priorityComparison == 0) { // if priorities are the same, compare deadline
                    return todo1[5].compareTo(todo2[5]); // compare deadline
                } else {
                    return priorityComparison;
                }
            }
        });
    }

    public void sortTodosDeadLine() {
        Collections.sort(todosTagSelected, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                return todo1[5].compareTo(todo2[5]); // compare deadline
            }
        });
    }

    public void sortTodosPriority() {
        Collections.sort(todosTagSelected, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                return Integer.compare(priorityToInt(todo2[6]), priorityToInt(todo1[6])); // compare priority
            }
        });
    }

    // show todosTagSelected
    private void displayTodos() {
        ToDoPanel.removeAll(); // Important to remove old buttons
        for (String[] todo : todosTagSelected) {
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
            String buttonLabel = String.format("%-15s : %-18s |    #%-8s    |    *%-10s    |    %s", memberName, title,
                    tag, deadline, priority);
            JButton todoButton = new JButton(buttonLabel);
            todoButton.setPreferredSize(new Dimension(600, 50));
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