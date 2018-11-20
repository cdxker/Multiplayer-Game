package Game.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.components.RotationComponent;
import com.almasb.fxgl.physics.PhysicsComponent;

import static java.lang.Math.*;

public class MovementComponent extends Component {

    private final double MAX_SPEED;
    private RotationComponent rotation;
    private PositionComponent position;
    private double speed = 5;
    private double angle = 0;
    private double refreshRate = 0;
    private PhysicsComponent physics;

    public MovementComponent() {
        this(50);
    }

    public MovementComponent(double max_speed) {
        MAX_SPEED = max_speed;
    }

    @Override
    public void onUpdate(double tpf) {
        refreshRate = tpf * 60;
    }

    @Override
    public void onAdded() {
        updateVelocity();
    }

    private void updateVelocity() {
        physics.setLinearVelocity(speed * refreshRate * cos(toRadians(angle)), speed * refreshRate * sin(toRadians(angle)));
    }

    public void speedUp() {
        speed += 1;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        updateVelocity();
    }

    public void slowDown() {
        speed -= 1;
        if (speed < -MAX_SPEED / 2) {
            speed = -MAX_SPEED / 2;
        }
        updateVelocity();
    }

    public void left() {
        angle -= 5;
        rotation.rotateBy(-5);
        updateVelocity();
    }

    public void right() {
        angle += 5;
        rotation.rotateBy(5);
        updateVelocity();
    }
}
