import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

// MyPage edit panel
// 画面の生成部分：isakile MyPagePanel
// データの保存部分：isalike SignUpPanel

public class EditMyPagePanel extends JPanel {
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel accountTypeLabel;
    private JTextField nameTextField;
    private JRadioButton publicRadioButton;
    private JRadioButton privateRadioButton;

    public EditMyPagePanel() {
        // layout 
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 40, 40));

        // main
        JPanel contentPanel = new JPanel(new GridLayout(7, 3, 5, 10));

        JLabel idTitleLabel = new JLabel("User ID:");
        idLabel = new JLabel();
        JLabel nameTitleLabel = new JLabel("Display Name:");
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        JLabel emailTitleLabel = new JLabel("Email:");
        emailLabel = new JLabel();
        passwordLabel = new JLabel();
        JLabel accountTypeTitleLabel = new JLabel("Account Type:");
        accountTypeLabel = new JLabel();
        JLabel accountTypeLabel = new JLabel("Account Type:");
        publicRadioButton = new JRadioButton("Public");
        privateRadioButton = new JRadioButton("Private");
        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(publicRadioButton);
        accountTypeGroup.add(privateRadioButton);

        // add label an field to mina panel 
        contentPanel.add(idTitleLabel);
        contentPanel.add(idLabel);
        contentPanel.add(new JLabel()); // adding 
        contentPanel.add(nameTitleLabel);
        contentPanel.add(nameLabel);
        contentPanel.add(nameTextField);
        contentPanel.add(emailTitleLabel);
        contentPanel.add(emailLabel);
        contentPanel.add(new JLabel());
        contentPanel.add(accountTypeTitleLabel);
        contentPanel.add(accountTypeLabel);
        contentPanel.add(publicRadioButton);
        contentPanel.add(new JLabel());
        contentPanel.add(new JLabel());
        contentPanel.add(privateRadioButton);

        // Back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        headerPanel.add(backButton, BorderLayout.WEST);

        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(saveButton);

        // layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserData();

        // button action
        saveButton.addActionListener(new SaveButtonListener());
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(EditMyPagePanel.this);
            MyWindow myWindow = (MyWindow) frame;
            myWindow.addPanelsAfterLogin();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "MainPanel");
        });
    }

    // if puushed Save button 
    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        
            int id = Integer.valueOf(idLabel.getText());
            String name = nameTextField.getText();
            name = (name == null || name.isEmpty()) ? nameLabel.getText() : name;
            String email = emailLabel.getText();
            String password = passwordLabel.getText();
            String accountType = publicRadioButton.isSelected() ? "Public" : "Private";
            accountType = (accountType == null || accountType.isEmpty()) ? accountTypeLabel.getText() : accountType;

            // validation ckeck
            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(EditMyPagePanel.this, "Invalid name. Name should contain only alphabets and have a length between 3 and 20 characters.");
                return;
            }

            // delete from member.csv once 
            try {
                File memberFile = new File("member.csv");
                File tempFile = new File("temp.csv");

                BufferedReader br = new BufferedReader(new FileReader(memberFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

                String line;
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        bw.write(line);
                        bw.newLine();
                        isFirstLine = false;
                    } else {
                        String[] data = line.split(",");
                        if (!data[0].equals(String.valueOf(id))) {
                            bw.write(line);
                            bw.newLine();
                        }
                    }
                }
                br.close();
                bw.close();

                memberFile.delete(); //overwritten by nre date in  member.csv
                if (!tempFile.renameTo(new File("member.csv"))) {
                    throw new IOException("Failed to rename temp.csv to member.csv");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // save member.csv
            try {
                FileWriter writer = new FileWriter("member.csv", true);
                writer.write(toCSV(id, name, email, password, accountType));
                writer.write("\n");
                writer.close();
                // clear field
                nameTextField.setText("");
                publicRadioButton.setSelected(false);
                privateRadioButton.setSelected(false);
                JOptionPane.showMessageDialog(null, "Saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(EditMyPagePanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.addPanelsAfterLogin();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "MyPagePanel");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Name 3-20words
        private boolean isValidName(String name) {
            return name.length() >= 3 && name.length() <= 20;
        }
    }

    // change data iorder to save CSV
    private String toCSV(int id, String name, String email, String password, String accountType) {
        return String.format("%d,%s,%s,%s,%s", id, name, email, password, accountType);
    }

    // get user data from member.csv
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
                    passwordLabel.setText(data[3]);
                    accountTypeLabel.setText(data[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}