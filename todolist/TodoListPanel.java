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

// All Todo List
// check box - have name and id

public class TodoListPanel extends JPanel {
    private List<String[]> todos;
    private List<String[]> members;
    private DetailTodoPanel detailPanel; // detail infromation panel
    private List<MemberCheckBox> memberCheckBoxes = new ArrayList<>();
    private List<TagCheckBox> tagCheckBoxes = new ArrayList<>();
    private List<String[]> todosMemberSelected = new ArrayList<>();
    private List<String[]> todosTagSelected = new ArrayList<>();
    private JPanel ToDoPanel = new JPanel();
    private JPanel LeftSelectPanel = new JPanel();

    // define memeber tag class 
    class MemberCheckBox extends JCheckBox {
        private String memberId;

        public MemberCheckBox(String memberId, String memberName) {
            super(memberName);
            this.memberId = memberId;
        }

        public String getMemberId() {
            return memberId;
        }
    }

    // define check tag
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

    public TodoListPanel() {
        todos = readCSV("todos.csv");
        members = readCSV("member.csv");

        // layout
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 70, 20, 70));

        // separete 2 panel
        LeftSelectPanel.setLayout(new BoxLayout(LeftSelectPanel, BoxLayout.Y_AXIS));
        ToDoPanel.setLayout(new GridLayout(15, 1));

        // generate buttons for each LeftScrollPane

        // member button
        LeftSelectPanel.add(new JLabel(" "));
        JLabel memberTitle = new JLabel("MEMBER");
        LeftSelectPanel.add(memberTitle);
        String user_id = String.valueOf(LoginPanel.user_id);
        for (String[] member : members) {
            if (member[0].equals(user_id)) {
                MemberCheckBox memberCheckBox = new MemberCheckBox(member[0], member[1]);
                memberCheckBox.setSelected(true);
                LeftSelectPanel.add(memberCheckBox);
                memberCheckBoxes.add(memberCheckBox);
            } else if (member[4].equals("Public")) {
                MemberCheckBox memberCheckBox = new MemberCheckBox(member[0], member[1]);
                memberCheckBox.setSelected(true);
                LeftSelectPanel.add(memberCheckBox);
                memberCheckBoxes.add(memberCheckBox);
            } else {
                continue;
            }
        }

        // generate tag
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

        // generate sort button
        LeftSelectPanel.add(new JLabel(" "));
        JLabel sortTitle = new JLabel("SORT");
        LeftSelectPanel.add(sortTitle);
        JCheckBox DeadLineCheckBox = new JCheckBox("DeadLine");
        DeadLineCheckBox.setSelected(false);
        LeftSelectPanel.add(DeadLineCheckBox);
        JCheckBox PriorityCheckBox = new JCheckBox("Priority");
        PriorityCheckBox.setSelected(false);
        LeftSelectPanel.add(PriorityCheckBox);

        // generate OK button
        LeftSelectPanel.add(new JLabel(" "));
        JButton SelectOKButton = new JButton("OK");
        SelectOKButton.setPreferredSize(new Dimension(300, 200));
        LeftSelectPanel.add(SelectOKButton);
        filterMemberTodos();
        filterTagTodos();
        displayTodos();

        // generate button for each todo
        SelectOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterMemberTodos();
                filterTagTodos();
                if (DeadLineCheckBox.isSelected() && PriorityCheckBox.isSelected()) {
                    sortTodosBoth();
                } else if (DeadLineCheckBox.isSelected()) {
                    sortTodosDeadLine();
                } else if (PriorityCheckBox.isSelected()) {
                    sortTodosPriority();
                }
                displayTodos();
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
        JLabel titleLabel = new JLabel("All Todo List");
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
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(TodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "MainPanel");
            }
        });
        archiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(TodoListPanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "ArchiveTodoListPanel");
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

    // add DetailTodoPanel when button pushed
    private void onTodoButtonClick(String todoId) {
        detailPanel.loadTodoDetails(todoId);
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(TodoListPanel.this);
        CardLayout cardLayout = (CardLayout) frame.getLayout();
        cardLayout.show(frame, "DetailTodoPanel");
    }

    // for MyWindow
    public void setDetailPanel(DetailTodoPanel detailPanel) {
        this.detailPanel = detailPanel;
    }

    // read CSV files
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

    // whether my task or public task or not
    private boolean isOpen(String memberId) {
        String user_id = String.valueOf(LoginPanel.user_id);
        for (String[] member : members) {
            if (user_id.equals(memberId)) {
                return true;
            }
            if (member[0].equals(memberId)) {
                if (member[4].equals("Public")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    // if check is in the box to todosMemberSelected
    // sort by Member
    private void filterMemberTodos() {
        todosMemberSelected.clear();
        for (MemberCheckBox memberCheckBox : memberCheckBoxes) {
            if (memberCheckBox.isSelected()) {
                String memberId = memberCheckBox.getMemberId();
                for (String[] todo : todos) {
                    if (todo[1].equals(memberId)) {
                        todosMemberSelected.add(todo);
                    }
                }
            }
        }
    }

    // sort by Tag
    private void filterTagTodos() {
        todosTagSelected.clear();
        for (TagCheckBox tagCheckBox : tagCheckBoxes) {
            if (tagCheckBox.isSelected()) {
                for (String[] todo : todosMemberSelected) {
                    if (todo[4].equals(tagCheckBox.tag)) {
                        todosTagSelected.add(todo);
                    }
                }
            }
        }
    }

    // mapping Priority
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

    // sort-function
    public void sortTodosBoth() {// Priority、DeadLine is both checked
        Collections.sort(todosTagSelected, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                int priorityComparison = Integer.compare(priorityToInt(todo2[6]), priorityToInt(todo1[6]));
                if (priorityComparison == 0) {
                    return todo1[5].compareTo(todo2[5]);
                } else {
                    return priorityComparison;
                }
            }
        });
    }

    public void sortTodosDeadLine() {// only DeadLine is checked
        Collections.sort(todosTagSelected, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                return todo1[5].compareTo(todo2[5]);
            }
        });
    }

    public void sortTodosPriority() {// only Priority is checked
        Collections.sort(todosTagSelected, new Comparator<String[]>() {
            @Override
            public int compare(String[] todo1, String[] todo2) {
                return Integer.compare(priorityToInt(todo2[6]), priorityToInt(todo1[6]));
            }
        });
    }

    // display todoTagSelected
    private void displayTodos() {
        ToDoPanel.removeAll();
        for (String[] todo : todosTagSelected) {
            String todoId = todo[0];
            String memberId = todo[1];
            String memberName = findMemberName(memberId);
            String title = todo[2];
            String tag = todo[4];
            String deadline = todo[5];
            String priority = todo[6];
            // if not mine or pubic
            if (!isOpen(memberId)) {
                continue;
            }
            String buttonLabel = String.format("%-15s : %-18s |    #%-8s    |    *%-10s    |    %s", memberName, title,
                    tag, deadline, priority);
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
