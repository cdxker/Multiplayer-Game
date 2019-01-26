package Game.components;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.RotationComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

import static java.lang.Math.PI;

public class MovementComponent extends Component {

    private double refreshRate;

    /*
     * The 3 fields below describe the vehicle's non-moving states
     */

    private PhysicsComponent physicsComponent;

    // The angle at which the vehicle is facing
    private RotationComponent orientation;

    /*
     * The 5 fields below describe the vehicle's linear motion
     */

    // The displacement of the vehicle each frame
    private Point2D velocity;

    // How much the velocity increases each frame
    private double acceleration;

    // How much the acceleration increases each frame while pressing forward
    private double enginePower = 0.05;
    private double enginePowerStore = enginePower;

    // How much slower the vehicle is each successive frame without acceleration
    private double velocityDrag = 0.95;

    // How much slower the acceleration is each successive frame
    // Places an upper limit on the horizontal velocity of the vehicle
    private double startingAccelerationDrag; // the "base" acceleration of the car
    private double accelerationDrag; // the drag the car is experiencing due to friction

    /*
     * The 3 fields below describe the vehicle's angular motion
     */

    // How much the vehicle is rotating each frame
    private double angularVelocity = 0;

    // How much the vehicle's angular velocity changes each frame
    private double steering = 1;

    // How much slower the angular velocity is each successive frame
    // Places an upper limit on the angular velocity of the vehicle
    private double angularDrag = 0.5;

    // Default constructor if no parameter is specified
    public MovementComponent(){
        this(0.05,0.95,0.90,1,0.5);
    }

    /**
     * Constructor in which the parameters are constants that define how the vehicle will react to user inputs
     *
     * @param enginePower The amount by which the vehicle's acceleration increases each frame while pressing forward.
     *
     * @param velocityDrag The fraction of the velocity left in the next frame without acceleration.
     *                      Values closer to 0 correspond to greater drag. Values closer to 1 corresponds to less drag.
     *
     * @param accelerationDrag The fraction of the acceleration left in the next frame without acceleration.
     *                          Values closer to 0 correspond to greater drag. Values closer to 1 corresponds to less drag.
     *
     * @param steering The amount by which the vehicle's angular velocity changes each frame.
     *
     * @param angularDrag The fraction of the angular velocity left in the next frame without steering.
     *                     Values closer to 0 correspond to greater drag. Values closer to 1 corresponds to less drag.
     */

    public MovementComponent(double enginePower, double velocityDrag, double accelerationDrag, double steering, double angularDrag){
        velocity = new Point2D(0,0);
        this.enginePower = enginePower;
        this.velocityDrag = velocityDrag;
        this.accelerationDrag = this.startingAccelerationDrag = accelerationDrag;
        this.steering = steering;
        this.angularDrag = angularDrag;
    }

    @Override
    /*
     * This method is run every 60th of a second
     * It updates the position, velocity, and acceleration of the vehicle based on
     * the engine power, drag constants, and player inputs
     */
    public void onUpdate(double tpf) {
        refreshRate = tpf * 60;
        /* Changes velocity by acceleration
         * Since velocity is a vector, and acceleration is represented by
         * a one-dimensional double value, each component is calculated using trigonometry
         *
         * The vehicle's previous orientation is used instead of newly changed orientation
         * to reflect the conservation of the vehicle's linear momentum
         * This has the effect of drifting/skidding when the vehicle turns at a sharp angle
         * or at high speed
         */

        physicsComponent.setBodyLinearVelocity(new Vec2(getVelocity().getX(),
                -getVelocity().getY()));

        setVelocity(getVelocity().add(Math.cos(orientation.getValue() / 180 * PI) * acceleration,
                Math.sin(orientation.getValue() / 180 * PI) * acceleration));

        // Dampening of the vehicle's linear motion due to drag
        setVelocity(getVelocity().multiply(velocityDrag));
        acceleration *= accelerationDrag;

        // Dampening of the vehicle's rotation due to drag
        angularVelocity *= angularDrag;

        physicsComponent.setAngularVelocity(angularVelocity);
    }

    /*
     * This method is a shortcut to get the magnitude of the velocity vector
     */
    public double getSpeed(){
        return getVelocity().magnitude();
    }

    /*
     * This method is called when pressing the forward button (w or up arrow)
     * It accelerates the vehicle in the same direction as its orientation
     */
    public void speedUp(){
        acceleration += enginePower;
    }

    /*
     * This method is called when pressing the backward button (s or down arrow)
     * It accelerates the vehicle in the opposite direction as its orientation
     */
    public void slowDown(){
        acceleration -= enginePower / 5;        /* Backward-moving speed is slower than forward-moving speed*/
    }

    /*
     * This method is called when pressing the right button (d or right arrow)
     * It rotates the vehicle clockwise
     */
    public void steerRight(){
        if(getSpeed() > 1 / angularDrag) {
            // Above a certain speed, the vehicle can rotate at its maximum steering rate
            angularVelocity += steering;
        } else {
            // Below a certain speed, the vehicle's ability to rotate slows down
            angularVelocity += getSpeed() * steering * angularDrag;
        }
    }

    /*
     * This method is called when pressing the left button (a or left arrow)
     * It rotates the vehicle counterclockwise
     */
    public void steerLeft(){
        if(getSpeed() > 1 / angularDrag) {
            // Above a certain speed, the vehicle can rotate at its maximum steering rate
            angularVelocity -= steering;
        }else {
            // Below a certain speed, the vehicle's ability to rotate slows down
            angularVelocity -= getSpeed() * steering * angularDrag;
        }
    }

    public void accelerates(Point2D force){
        setVelocity(getVelocity().add(force));
    }

    /**
     * Increases the starting acceleration as to not infer with
     * tile components changing instantaneous acceleration drag.
     *
     * @param value The value to increase it by
     */
    public void increaseBaseAccelerationDrag(double value){
        double deltaDrag = this.accelerationDrag - this.startingAccelerationDrag; // find the difference between drag(drag applied by TileFriction)

        this.startingAccelerationDrag += value; // Change value of starting drag

        this.accelerationDrag = startingAccelerationDrag - deltaDrag; // Set the next frames drag0
        if(accelerationDrag > 1){
            accelerationDrag = 1;
        }else if(accelerationDrag < 0){
            accelerationDrag = 0;
        }
    }

    public void increaseAccelerationDrag(double drag) {
        accelerationDrag = startingAccelerationDrag + drag;
        if(accelerationDrag > 1){
            accelerationDrag = 1;
        }else if(accelerationDrag < 0){
            accelerationDrag = 0;
        }
    }

    public void setAccelerationDrag(double drag) {
        accelerationDrag = startingAccelerationDrag + drag;
        if(accelerationDrag > 1){
            accelerationDrag = 1;
        }else if(accelerationDrag < 0){
            accelerationDrag = 0;
        }
    }

    public double getAccelerationDrag() {
        return accelerationDrag;
    }

    public void stops(){
        enginePower = 0;
    }

    public void resumeMoving(){
        enginePower = enginePowerStore;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
}
