package server;


import java.net.DatagramPacket;

public interface Handler {

    /**
     * Method to be called when a requests needs to be handled
     * @param parent The parent is the Client that called the method, This
     *               can be used as a trace back to call methods from that class
     * @param packet The packet is the data that it received.
     */
    void process(Client parent, DatagramPacket packet);
}

