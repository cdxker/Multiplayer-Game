package Game.components.powerups;


import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.extra.entity.effect.Effect;
import com.almasb.fxgl.extra.entity.effect.EffectComponent;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

/**
 * Utility that provides all power up components
 */
public class PowerUps {

    @NotNull
    public static Component HealthPowerUp(double strength){
        return new PowerUpComponent(new HealthPowerUp(strength));
    }

    @NotNull
    public static Component SpeedPowerUp(Duration time){
        return new PowerUpComponent(new SpeedEffect(0.1, time));
    }

    @NotNull
    public static Component SlowPowerUp(Duration time){
        return new PowerUpComponent(new SpeedEffect(-0.1, time));
    }


}
