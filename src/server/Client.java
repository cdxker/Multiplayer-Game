package server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    public static void main(String[] args) {
        Client server = new Client(3000);
        server.add_handler((parent, packet) -> System.out.println("Handle"));
        server.add_handler(((parent, packet) -> System.out.println(new String(packet.getData()))));
        server.add_handler((parent, packet) -> parent.running = false);
        server.start();
        server.send("Hello_Dude".getBytes(), 3000);

    }

    private DatagramSocket socket;
    private int port;
    private ArrayList<Handler> handlers = new ArrayList<>();
    private static final Logger logger = Logger.getLogger("Socket");
    private boolean running = true;
    private Thread listeningThread;

    public Client(int port){
        this.port = port;

    }

    /**
     * Creates all sockets and begins listening on its port
     */
    public void start(){
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        listeningThread = new Thread(this::listen);
        listeningThread.start();
        logger.log(Level.INFO, "Client started on port "+ port);
    }

    private void listen(){
        byte[] buf = new byte[256];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        while(running) {
            try {
                socket.receive(p);

            } catch (IOException e) {
                e.printStackTrace();
            }
            process(p);
        }
    }
    public void add_handler(Handler handler){
        handlers.add(handler);
    }

    private void process(DatagramPacket packet){
        for (Handler handler : handlers){
            handler.process(this, packet);
        }
    }

    /**
     * Sends a byte[] to a location. The location is set to the local host
     * @param data The data that is to be sent.
     * @param send_port the port on the address to send the packet to
     * @return Retruns if the method completed successfully
     */
    public boolean send(byte[] data, int send_port) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return send(data, address, send_port);
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, "Local Host not found"+ port);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a byte[] to a location
     * @param data The data that is to be sent.
     * @param address The address that it will be sent to
     * @param send_port the port on the address to send the packet to
     * @return Retruns if the method completed successfully
     */
    public boolean send(byte[] data, InetAddress address, int send_port){
        DatagramPacket p = new DatagramPacket(data, data.length, address, send_port);
        try {
            socket.send(p);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger("Socket").log(Level.WARNING, "Client Failed to send"+ port);
            return false;
        }
    }
}
