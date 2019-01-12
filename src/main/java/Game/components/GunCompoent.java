package Game.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.app.DSLKt.spawn;

public class GunCompoent extends Component {

    private PhysicsComponent physics;
    private String bulletName;
    private Entity myEntity;

    @Override
    public void onAdded() {
        myEntity = getEntity();
    }


    public GunCompoent(String bulletName){
        this.bulletName = bulletName;
    }

    public void shootBullet(){
        Entity e = getEntity();
        double magnitude = 200;
        float angle = physics.getBody().getAngle(); // angle is measured in radians
        Point2D pos = e.getCenter().add(Math.cos(angle) * e.getHeight(), 0);
        Point2D velocity = new Point2D(Math.cos(angle)* magnitude,-Math.sin(angle)* magnitude);



        // create a bullet
        Entity bullet = spawn(bulletName, new SpawnData(pos.getX(), pos.getY()).put("velocity", velocity));
        // apply velocity towards the angle of direction of the car
    }

}
