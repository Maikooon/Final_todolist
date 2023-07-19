import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.ImageIcon;

// panel control
// panel
//1. app is up
//2. login
//3. move panel to panel 

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
        // layout
        setTitle("Todo List");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);

        // icon
        URL imageIconUrl = getClass().getClassLoader().getResource("iconimage.png");
        if (imageIconUrl != null) {
            ImageIcon icon = new ImageIcon(imageIconUrl);
            setIconImage(icon.getImage());
        }

        // generate panel when wakeup app
        add(new InitialPanel(), "InitialPanel");
        add(new SignUpPanel(), "SignUpPanel");
        add(new LoginPanel(), "LoginPanel");
        add(new MainPanel(), "MainPanel");
        add(new CreateTodoPanel(), "CreateTodoPanel");

        addWindowListener(new WinListener());
    }

    // add panel when login is successful
    public void addPanelsAfterLogin() {
        myPagePanel = new MyPagePanel();
        editMyPagePanel = new EditMyPagePanel();
        add(myPagePanel, "MyPagePanel");
        add(editMyPagePanel, "EditMyPagePanel");
    }

    // add panel - every time you open 
    public void reloadPanels() {
        //move value
        // lst --detail & detail -edit 
        todoListPanel = new TodoListPanel();
        // myTodoListPanel = new MyTodoListPanel();
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

        // add(todoListPanel, "TodoListPanel");
        // add(myTodoListPanel, "MyTodoListPanel");
        add(todoListPanel, "TodoListPanel");
        add(myTodoListPanel, "MyTodoListPanel");
        add(archiveTodoListPanel, "ArchiveTodoListPanel");
        add(detailTodoPanel, "DetailTodoPanel");
        add(archiveDetailTodoPanel, "ArchiveDetailTodoPanel");
        add(editTodoPanel, "EditTodoPanel");
    }

    // exit func
    class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}