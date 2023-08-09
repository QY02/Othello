package com.rt.othello.server;

import java.util.ArrayList;

public class Send {

    public ArrayList<ConnectedPlayer> rs;

    public Send(ArrayList<ConnectedPlayer> rs) {
        this.rs = rs;
    }

    public void zf(String s, int id) {
        for (int i = 0; i < rs.size(); i++) {
            if (i != id) {
                this.rs.get(i).send(s);
            }
        }
    }
}
