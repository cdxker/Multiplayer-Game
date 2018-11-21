package Game.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.components.RotationComponent;
import javafx.geometry.Point2D;

import static java.lang.Math.*;
public class MovementComponent extends Component {

    private double refreshRate = 0;
    private RotationComponent rotation;
    private PositionComponent position;
    private double direction = 0;

    private Point2D velocity = new Point2D(0,0);
    private double enginePower = 0.1;
    private double linearDrag = 0.93;
    private double acceleration = 0;

    private double angularVelocity = 0;
    private double steering = 1;
    private double angularDrag = 0.5;



    public MovementComponent(double positionX, double positionY, double engineStrength){

    }

    @Override
    public void onUpdate(double tpf) {
        refreshRate = tpf * 60;

        position.translate(velocity);
        setVelocity(getVelocity().add(Math.cos(direction/180*PI) * acceleration,
                Math.sin(direction/180*PI) * acceleration));
        setVelocity(getVelocity().multiply(linearDrag));
        acceleration *= linearDrag;
        direction += angularVelocity;
        rotation.setValue(direction);
        angularVelocity *= angularDrag;
    }

    public void speedUp(){
        acceleration += enginePower;
    }

    public void slowDown(){
        if(velocity.magnitude() > 0.1){
            velocity.multiply(0.8);
        } else {
            velocity.subtract(velocity);
        }

    }

    public void steerRight(){
        angularVelocity += steering;
    }

    public void steerLeft(){
        angularVelocity -= steering;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
}
