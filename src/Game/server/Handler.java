package Game.server;

import java.net.DatagramPacket;

public interface Handler {
    void process(Client parent,DatagramPacket packet);
}

