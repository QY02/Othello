package view.onlineMode;

import javax.swing.*;

public class ServerLabel extends JLabel {

    private int id;
    private String address;

    public ServerLabel(int id, String address) {
        this.id = id;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
