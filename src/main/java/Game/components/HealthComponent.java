package Game.components;


import Game.GameApp;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This component contains an entities health
 * and max health
 */
public class HealthComponent extends Component {
    private final double maxHealth;
    private double health;

    Rectangle hbar1;
    Rectangle hbar2;
    Rectangle fillBar2;
    Rectangle fillBar1;
    /**
     * Sets the maximum health to the entities starting health value
     *
     * @param startingHealth The starting health of the entity
     */
    public HealthComponent(double startingHealth) {
        this(startingHealth, startingHealth);
    }

    /**
     * @param maxHealth      The maximum health that an entity can have
     * @param startingHealth The starting health of the entity
     */
    public HealthComponent(double maxHealth, double startingHealth) {
        this.maxHealth = maxHealth;
        this.health = startingHealth;
    }

    private void killEntity() {
        Entity entity = getEntity();
        entity.removeFromWorld();
    }

    @Override
    public void onAdded() {
        Point2D pos = getEntity().getPosition();
        Point2D size = new Point2D(getEntity().getWidth(), getEntity().getHeight());

        hbar1 = new Rectangle(0, -5, size.getX(), size.getY()/ 5); hbar1.setStroke(Color.BLACK);
        hbar2 = new Rectangle(0, -5, size.getX(), size.getY()/ 5); hbar2.setStroke(Color.BLACK);
        hbar1.setFill(Color.TRANSPARENT);
        hbar2.setFill(Color.TRANSPARENT);

        fillBar2 = new Rectangle(0, -5, size.getX()* health/maxHealth, size.getY()/ 5);
        fillBar1 = new Rectangle(0, -5, size.getX()* health/maxHealth, size.getY()/ 5);

        fillBar1.setFill(Color.rgb(0, 255, 0));
        fillBar2.setFill(Color.rgb(0, 255, 0));

        entity.getComponent(ScreenComponent.class).getView().addNode(hbar1);
        entity.getComponent(ScreenComponent.class).getView().addNode(fillBar1);

        entity.getComponent(ScreenComponent2.class).getView().addNode(hbar2);
        entity.getComponent(ScreenComponent2.class).getView().addNode(fillBar2);

    }

    private void updateHealth(){
        fillBar1.setWidth(health * getEntity().getWidth() /maxHealth);
        fillBar2.setWidth(health * getEntity().getWidth() /maxHealth);
    }

    public double getHealth() {
        return health;
    }

    /**
     * Increments the health only up to the entities maximum health
     *
     * @param amount The amount to increase the health by
     */
    public void add(double amount) {
        health += amount;
        if (health > maxHealth){
            health = maxHealth;
        }
        System.out.println(amount);
        if (health <= 0) {
            health = 0;
            killEntity();
        }
        updateHealth();
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public HealthComponent add(HealthComponent amount){
        HealthComponent higher;
        if(amount.getHealth() > this.getHealth()) higher = amount;
        else higher = this;
        return new HealthComponent(amount.getHealth() + this.getHealth(), higher.getHealth());
    }


}
