package GUI;

import Communication.Client;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientWindow extends JFrame {

    private JTextField textField;
    private JTextPane chat;
    private Client client;
    private String loginMessage;
    private String name;

    public ClientWindow() {

        client = new Client(this, "");
        LogIn logIn = new LogIn(client);

        while (name == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        client.updateName(name);
        init();

    }

    private void init() {

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        setContentPane(content);
        setTitle("BlueNectar ~ IM Chatroom");

//        Creating the text area/chat
        chat = new JTextPane();
        chat.setPreferredSize(new Dimension(400, 300));
        chat.setMargin(new Insets(5, 5, 5, 5));
        chat.setEditable(false);
        JScrollPane scrollableChat = new JScrollPane(chat);
        content.add(scrollableChat);

//        Creating the send message text entry line
        JPanel textEntryPane = new JPanel();
        textEntryPane.setLayout(new BoxLayout(textEntryPane, BoxLayout.LINE_AXIS));

        textField = new JTextField();
        textEntryPane.add(textField);
        textField.getText();

        JButton enter = new JButton("ENTER");
        enter.addActionListener(new EnterActionListener());
        textEntryPane.add(enter);

        content.add(textEntryPane);

//        Disconnect button
        JButton disconnect = new JButton("DISCONNECT");
        disconnect.addActionListener(new DisconnectActionListener());
        content.add(disconnect);

        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public String getEnteredText() {
        return textField.getText();
    }

    public void updateChat(String name, String text, Color colour) {

        chat.setEditable(true);

//        COPIED FROM https://stackoverflow.com/questions/9650992/how-to-change-text-color-in-the-jtextarea
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, colour);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = chat.getDocument().getLength();
        chat.setCaretPosition(len);
        chat.setCharacterAttributes(aset, false);
        chat.replaceSelection("\n" + name + ": " + text);
//        COPIED FROM https://stackoverflow.com/questions/9650992/how-to-change-text-color-in-the-jtextarea

        chat.setEditable(false);

    }

    public void setLoginMessage(String message) {
        loginMessage = message;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    class LogIn {

        private JFrame loginFrame;
        private JTextField userField;
        private JPasswordField passField;
        private Client client;

        public LogIn(Client client) {

            this.client = client;

            loginFrame = new JFrame("Sign up & Log in");
            JPanel loginContent = new JPanel();
            loginContent.setLayout(new BoxLayout(loginContent, BoxLayout.PAGE_AXIS));
            loginFrame.setContentPane(loginContent);

//            Username section
            JPanel userContent = createHorizontalPanel();

            JLabel userLabel = new JLabel("Username: ");
            userContent.add(userLabel);

            userField = new JTextField();
            userContent.add(userField);

            loginContent.add(userContent);

//            Password section
            JPanel passContent = createHorizontalPanel();

            JLabel passLabel = new JLabel("Password: ");
            passContent.add(passLabel);

            passField = new JPasswordField();
            passContent.add(passField);

            loginContent.add(passContent);

//            Sign up button
            JButton signButton = new JButton("Log-in/Sign-up");
            signButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    if (getUsername().equals("") || getPassword() == null) {
                        JOptionPane.showMessageDialog(null, "Username or password can't be empty.");
                        return;
                    }

                    setLoginMessage("");
                    client.login(getUsername(), getPassword());

//                    Wait for the server to reply
                    while (getLoginMessage().equals("")) {

                        try {

                            Thread.sleep(1);

                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                    }

                    if (getLoginMessage().equals("INCORRECT PASSWORD")) {
                        JOptionPane.showMessageDialog(null, "Incorrect password.");
                        return;
                    }

                    name = getUsername();

                    loginFrame.dispose();

                }

            });

            loginFrame.setSize(300, 300);
            loginFrame.setVisible(true);

        }

        public String getUsername() {
            return userField.getText();
        }

//        Note - Passwords stored in char[] arrays and not Strings, as a String sticks around in memory for longer.
        public char[] getPassword() {
            return passField.getPassword();
        }


        public JPanel createHorizontalPanel() {
            JPanel sectionContent = new JPanel();
            sectionContent.setLayout(new BoxLayout(sectionContent, BoxLayout.LINE_AXIS));
            return sectionContent;
        }

        public JTextField createSection(String label, JPanel parent) {
            JPanel sectionContent = new JPanel();
            sectionContent.setLayout(new BoxLayout(sectionContent, BoxLayout.LINE_AXIS));

            JLabel sectionLabel = new JLabel(label);
            sectionContent.add(sectionLabel);

            JTextField sectionField = new JTextField();
            sectionContent.add(sectionField);

            parent.add(sectionContent);

            return sectionField;
        }

    }

    class EnterActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String text = getEnteredText();

            if (text.contains(":")) {
                JOptionPane.showMessageDialog(null, "Colon's \':\' are illegal characters");
                return;
            }

            textField.setText("");
            client.sendUpdate(text);

        }
    }

    class DisconnectActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            client.disconnect();

            dispose();
            System.exit(0);

        }
    }

}
