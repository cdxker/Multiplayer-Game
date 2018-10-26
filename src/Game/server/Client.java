package Game.server;

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
    public Client(){}
    /**
     * Creates all sockets and begins listening on its port
     */
    public void start(){
        try {
            if(port != 0){
                socket = new DatagramSocket(port);
            }else{
                socket = new DatagramSocket();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

        listeningThread = new Thread(this::listen);
        listeningThread.setDaemon(true);
        listeningThread.start();
        logger.log(Level.INFO, "Client started on port "+ port);
    }

    /**
     * The listening thread that will process data received on the thread
     */
    private void listen(){
        byte[] buf = new byte[1024];
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

    /**
     * Adds a handler to the server, Its method will be invoked when
     * the server receives a Datagram packet
     * @param handler A handler interface
     */
    public void add_handler(Handler handler){
        handlers.add(handler);
    }

    /**
     * Private method to process data
     * @param packet The packet that will be processed by the handlers
     */
    private void process(DatagramPacket packet){
        for (Handler handler : handlers){
            handler.process(this, packet);
        }
    }

    /**
     * Sends a byte[] to a location. The location is set to the local host
     * @param data The data that is to be sent.
     * @param send_port the port on the address to send the packet to
     * @return Returns if the method completed successfully
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
     * Sends a byte[] to a location specified by the address and port
     * @param data The data that is to be sent.
     * @param address The address that it will be sent to
     * @param send_port the port on the address to send the packet to
     * @return Returns if the method completed successfully
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

    @Override
    public String toString() {
        return String.format("<%s>: running: %b on port %d", getClass().getName(),this.running, this.port);
    }
}
