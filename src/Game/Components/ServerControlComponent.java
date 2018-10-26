package Game.Components;

import Game.server.Client;
import Game.server.Handler;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import javafx.geometry.Point2D;

import java.net.DatagramPacket;

public class ServerControlComponent extends Component {
    private PositionComponent position;
    private Client server;
    private ComponentHandler posHandler;

    public ServerControlComponent(int port){
        posHandler = new ComponentHandler();
        server = new Client();
        server.add_handler(posHandler);
        server.start();
    }

    @Override
    public void onUpdate(double tpf){
        position.setValue(posHandler.getPos());
    }

    private class ComponentHandler implements Handler{

        public Point2D position;

        public Point2D getPos(){
            return position;
        }

        @Override
        public void process(Client parent, DatagramPacket packet){
        }
    }
}
