import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyWindow extends Frame {
    private TodoListPanel todoListPanel;
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
        add(new ArchivePanel(), "ArchivePanel");

        addWindowListener(new WinListener());
    }

    // ログイン成功時にパネルを追加する
    public void addPanelsAfterLogin() {
        // 値の受け渡しをする画面同士の制御
        todoListPanel = new TodoListPanel();
        detailTodoPanel = new DetailTodoPanel();
        editTodoPanel = new EditTodoPanel();
        myPagePanel = new MyPagePanel();
        todoListPanel.setDetailPanel(detailTodoPanel);
        detailTodoPanel.setEditlPanel(editTodoPanel);

        add(todoListPanel, "TodoListPanel");
        add(detailTodoPanel, "DetailTodoPanel");
        add(editTodoPanel, "EditTodoPanel");
        add(myPagePanel, "MyPagePanel");

        // パネルの初期表示はログイン画面に設定
        CardLayout cardLayout = (CardLayout) getLayout();
        cardLayout.show(this, "LoginPanel");
    }

    // 画面を閉じたらプログラム終了
    class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
