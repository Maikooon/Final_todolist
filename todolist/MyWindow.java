import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyWindow extends Frame {
    private TodoListPanel todoListPanel;
    private MyTodoListPanel myTodoListPanel;
    private DetailTodoPanel detailTodoPanel;
    private EditTodoPanel editTodoPanel;
    private MyPagePanel myPagePanel;

    public static void main(String[] args) {
        MyWindow mw = new MyWindow();
        mw.setVisible(true);
    }

    MyWindow() {
        setTitle("Todo List");
        setSize(1000, 750);
        setLocationRelativeTo(null); // 画面中央に表示

        CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(new InitialPanel(), "InitialPanel");
        add(new SignUpPanel(), "SignUpPanel");
        add(new LoginPanel(), "LoginPanel");
        add(new MainPanel(), "MainPanel");
        add(new CreateTodoPanel(), "CreateTodoPanel");

        addWindowListener(new WinListener());
    }

    // ログイン成功時にパネルを追加する
    public void addPanelsAfterLogin() {
        // 値の受け渡しをする画面同士の制御
        myPagePanel = new MyPagePanel();

        add(myPagePanel, "MyPagePanel");
    }

    public void reloadPanels() {
        // 値の受け渡しをする画面同士の制御
        todoListPanel = new TodoListPanel();
        myTodoListPanel = new MyTodoListPanel();
        detailTodoPanel = new DetailTodoPanel();
        editTodoPanel = new EditTodoPanel();
        todoListPanel.setDetailPanel(detailTodoPanel);
        myTodoListPanel.setDetailPanel(detailTodoPanel);
        detailTodoPanel.setEditlPanel(editTodoPanel);

        add(todoListPanel, "TodoListPanel");
        add(myTodoListPanel, "MyTodoListPanel");
        add(detailTodoPanel, "DetailTodoPanel");
        add(editTodoPanel, "EditTodoPanel");
    }

    // 画面を閉じたらプログラム終了
    class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}