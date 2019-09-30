import Communication.Server;

import javax.swing.*;

public class ServerApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Server());

    }

}
