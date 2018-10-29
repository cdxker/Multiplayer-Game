package Game.server.Handlers;

import Game.components.ServerControlComponent;
import Game.server.Client;
import Game.server.Handler;
import javafx.geometry.Point2D;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class ServerComponentHandler implements Handler {

    @Override
    public void process(Client parent, DatagramPacket packet) {
        String data = new String(packet.getData(), StandardCharsets.UTF_8);
        String[] tokens = data.split(":-");
        System.out.print(data);
        if (tokens[0].toLowerCase().equals("pos")) { // Always is the type
            PositionParser parser = new PositionParser(data);

            ServerControlComponent.characterPositions.put(parser.getCharacterId(), parser.getPosition());
        }
    }

    private class PositionParser {
        private int characterId;
        private Point2D position;


        public PositionParser(String data){
            String[] tokens = data.split(":-");
            characterId = Integer.parseInt(tokens[1]);
            String[] positions = tokens[2].split(",");

            double x = Double.parseDouble(positions[0]);
            double y = Double.parseDouble(positions[1]);
            position = new Point2D(x, y);
        }

        public int getCharacterId() {
            return characterId;
        }

        public Point2D getPosition() {
            return position;
        }
    }
}

