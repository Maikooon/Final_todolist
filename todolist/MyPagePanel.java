import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Mypage panel

public class MyPagePanel extends JPanel {
    private JLabel spaceLabel;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel accountTypeLabel;

    public MyPagePanel() {
        //lyaout 
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 60, 50, 70));

        // title abel
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        // main panel
        JPanel contentPanel = new JPanel(new GridLayout(7, 2, 5, 10));
        JLabel SpaceLabel = new JLabel("");
        spaceLabel = new JLabel();
        JLabel idTitleLabel = new JLabel("User ID:");
        idLabel = new JLabel();
        JLabel nameTitleLabel = new JLabel("Name:");
        nameLabel = new JLabel();
        JLabel emailTitleLabel = new JLabel("Email:");
        emailLabel = new JLabel();
        JLabel accountTypeTitleLabel = new JLabel("Account type:");
        accountTypeLabel = new JLabel();

        // add label & field to main panel
        contentPanel.add(SpaceLabel);
        contentPanel.add(spaceLabel);
        contentPanel.add(SpaceLabel);
        contentPanel.add(spaceLabel);
        contentPanel.add(idTitleLabel);
        contentPanel.add(idLabel);
        contentPanel.add(nameTitleLabel);
        contentPanel.add(nameLabel);
        contentPanel.add(emailTitleLabel);
        contentPanel.add(emailLabel);
        contentPanel.add(accountTypeTitleLabel);
        contentPanel.add(accountTypeLabel);

        loadUserData();

        // Back & titel button
        JButton backButton; // Back button
        backButton = new JButton("Back");
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // generate Edit button and Logout button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("Edit");
        JButton logoutButton = new JButton("Logout");
        buttonPanel.add(editButton);
        buttonPanel.add(logoutButton);

        // layout
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // button avtion
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyPagePanel.this);
            MyWindow myWindow = (MyWindow) frame;
            myWindow.reloadPanels();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "MainPanel");
        });
        editButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyPagePanel.this);
            MyWindow myWindow = (MyWindow) frame;
            myWindow.addPanelsAfterLogin();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "EditMyPagePanel");
        });
        logoutButton.addActionListener(e -> {
            int option = showConfirmationDialog("Logout Confirmation", "Are you sure you want to log out??");
            if (option == JOptionPane.YES_OPTION) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyPagePanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "LoginPanel");
            }
        });
    }

    // get userdata from member.csv
    public void loadUserData() {
        String memberId = String.valueOf(LoginPanel.user_id);
        String memberFilePath = "member.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(memberFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data[0].equals(memberId)) {
                    idLabel.setText(data[0]);
                    nameLabel.setText(data[1]);
                    emailLabel.setText(data[2]);
                    accountTypeLabel.setText(data[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ///////dialog when Logout button is pushed 
    private int showConfirmationDialog(String title, String message) {
        return JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Yes", "Cancel" }, "Yes");
    }
}
