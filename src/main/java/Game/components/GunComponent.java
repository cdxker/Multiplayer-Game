package Game.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import static com.almasb.fxgl.app.DSLKt.spawn;
import static com.almasb.fxgl.app.DSLKt.texture;

public class GunComponent extends Component {

    private PhysicsComponent physics;
    private String bulletName;

    @Override
    public void onAdded() {
        Node gunTexture1 = texture("gun.png", getEntity().getWidth() * 18 /32, getEntity().getWidth()*10 / 16);
        Node gunTexture2 = texture("gun.png", getEntity().getWidth() * 18 /32, getEntity().getWidth()*10 / 16);

        gunTexture1.setTranslateX(getEntity().getWidth()*16/32);
        gunTexture1.setTranslateY(getEntity().getHeight()*3/16);

        gunTexture2.setTranslateX(getEntity().getWidth()*16/32);
        gunTexture2.setTranslateY(getEntity().getHeight()*3/16);

        getEntity().getView().addNode(gunTexture1);
        getEntity().getComponent(ScreenComponent.class).getView().addNode(gunTexture1);
        getEntity().getComponent(ScreenComponent2.class).getView().addNode(gunTexture2);
    }


    public GunComponent(String bulletName){
        this.bulletName = bulletName;
    }

    public void shootBullet(){
        Entity player = getEntity();
        double magnitude = 2000;
        float angle = physics.getBody().getAngle(); // angle is measured in radians
        Point2D pos = player.getCenter().add(Math.cos(angle) * player.getWidth(), -Math.sin(angle) * player.getHeight());
        Point2D velocity = new Point2D(Math.cos(angle)* magnitude,-Math.sin(angle)* magnitude);



        // create a bullet
        spawn(bulletName, new SpawnData(pos.getX(), pos.getY()).put("velocity", velocity).put("player",player));
        MovementComponent playerMovement = player.getComponent(MovementComponent.class);
        playerMovement.accelerates(velocity.multiply(-0.001));
        // apply velocity towards the angle of direction of the car

    }

}
