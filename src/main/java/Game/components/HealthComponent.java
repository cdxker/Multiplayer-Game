package Game.components;


import Game.GameApp;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

/**
 * This component contains an entities health
 * and max health
 */
public class HealthComponent extends Component {
    private final double maxHealth;
    private double health;

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
        // TODO : add dying animation
        FXGL.<GameApp>getAppCast().gameOver();
    }

    public double getHealth() {
        return health;
    }

    /**
     * Increments the health only up to the entities maximum health
     *
     * @param amount The amount to increase the health by
     */
    public void increment(double amount) {
        health += amount;
        health %= maxHealth;
        if (health <= 0) {
            health = 0;
            killEntity();
        }
    }
}
