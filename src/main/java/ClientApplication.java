import GUI.ClientWindow;

import javax.swing.*;

public class ClientApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new ClientWindow());

    }

}
