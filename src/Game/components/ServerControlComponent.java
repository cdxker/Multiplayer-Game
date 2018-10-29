package Game.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import javafx.geometry.Point2D;

import java.util.HashMap;

public class ServerControlComponent extends Component {

        public static HashMap<Integer, Point2D> characterPositions;
        private int id;
        private PositionComponent position;

        public ServerControlComponent(int id){
            this.id = id;
        }

        public void onUpdate(){
            Point2D pos = characterPositions.get(id);
            position.setValue(pos);
        }
}

