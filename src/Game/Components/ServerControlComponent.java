package Game.Components;

import Game.server.Client;
import Game.server.Handler;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import javafx.geometry.Point2D;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

public class ServerControlComponent extends Component {
    private PositionComponent position;
    private Client server;
    private ComponentHandler posHandler;

    public ServerControlComponent(int port){
        posHandler = new ComponentHandler();
        server = new Client(port);
        server.add_handler(posHandler);
        server.start();
    }

    @Override
    public void onUpdate(double tpf){
        if (posHandler.getPos() != null){
            position.setValue(posHandler.getPos());
        }
    }

    private class ComponentHandler implements Handler{

        public Point2D position;

        public Point2D getPos(){
            return position;
        }

        @Override
        public void process(Client parent, DatagramPacket packet){
            String data = null;
            try {
                data = new String(packet.getData(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String[] tokens = data.split(":");
            System.out.print(data);
            if (tokens[0].toLowerCase().equals("pos")){ // Always is the id
                String[] positions = tokens[1].split(",");
                double x = Double.parseDouble(positions[0]);
                double y = Double.parseDouble(positions[1]);
                position = new Point2D(x, y);
            }
        }
    }
}
