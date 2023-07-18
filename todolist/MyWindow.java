import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// 画面を制御する
// 起動時に生成 or ログイン時に生成 or 遷移するたびに生成

public class MyWindow extends Frame {
    private TodoListPanel todoListPanel;
    private MyTodoListPanel myTodoListPanel;
    private ArchiveTodoListPanel archiveTodoListPanel;
    private DetailTodoPanel detailTodoPanel;
    private ArchiveDetailTodoPanel archiveDetailTodoPanel;
    private EditTodoPanel editTodoPanel;
    private MyPagePanel myPagePanel;
    private EditMyPagePanel editMyPagePanel;

    public static void main(String[] args) {
        MyWindow mw = new MyWindow();
        mw.setVisible(true);
    }

    MyWindow() {
        // レイアウト
        setTitle("Todo List");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);

        // 起動時に生成して大丈夫なパネル
        add(new InitialPanel(), "InitialPanel");
        add(new SignUpPanel(), "SignUpPanel");
        add(new LoginPanel(), "LoginPanel");
        add(new MainPanel(), "MainPanel");
        add(new CreateTodoPanel(), "CreateTodoPanel");

        addWindowListener(new WinListener());
    }

    // ログイン成功時にパネルを追加する
    public void addPanelsAfterLogin() {
        myPagePanel = new MyPagePanel();
        editMyPagePanel = new EditMyPagePanel();
        add(myPagePanel, "MyPagePanel");
        add(editMyPagePanel, "EditMyPagePanel");
    }

    // 開くたびにパネルを追加する
    public void reloadPanels() {
        // 値の受け渡しをする画面同士の制御
        // 一覧⇄詳細と詳細⇄編集はここに追加
        todoListPanel = new TodoListPanel();
        //myTodoListPanel = new MyTodoListPanel();
        archiveTodoListPanel = new ArchiveTodoListPanel();
        detailTodoPanel = new DetailTodoPanel();
        archiveDetailTodoPanel = new ArchiveDetailTodoPanel();
        editTodoPanel = new EditTodoPanel();
        myTodoListPanel = new MyTodoListPanel();

        todoListPanel.setDetailPanel(detailTodoPanel);
        myTodoListPanel.setDetailPanel(detailTodoPanel);
        archiveTodoListPanel.setDetailPanel(archiveDetailTodoPanel);
        detailTodoPanel.setEditlPanel(editTodoPanel);
        todoListPanel.setDetailPanel(detailTodoPanel);
        myTodoListPanel.setDetailPanel(detailTodoPanel);

        //add(todoListPanel, "TodoListPanel");
        //add(myTodoListPanel, "MyTodoListPanel");
        add(todoListPanel, "TodoListPanel");
        add(myTodoListPanel,"MyTodoListPanel");
        add(archiveTodoListPanel, "ArchiveTodoListPanel");
        add(detailTodoPanel, "DetailTodoPanel");
        add(archiveDetailTodoPanel, "ArchiveDetailTodoPanel");
        add(editTodoPanel, "EditTodoPanel");
    }

    // 画面を閉じたらプログラム終了
    class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}