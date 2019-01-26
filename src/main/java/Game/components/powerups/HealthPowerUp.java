package Game.components.powerups;

import Game.components.HealthComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.extra.entity.effect.Effect;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * PowerUp that adds resources to a different component
 */
public class HealthPowerUp extends Effect {
    private static Logger logger = Logger.getLogger(HealthPowerUp.class.getName());
    private double amount;

    /**
     * Constructs a power up.
     * @param amount The ammount to heal the entity by
     */
     HealthPowerUp(double amount){
        super(Duration.millis(1));
        this.amount = amount;
    }

    @Override
    public void onStart(@NotNull Entity e) {
         logger.info("Health Power Up applied");
        HealthComponent h = e.getComponent(HealthComponent.class);
        h.add(this.amount);
    }

    @Override
    public void onEnd(@NotNull Entity entity) {

    }
}
