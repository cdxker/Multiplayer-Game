package Game.components.powerups;

import Game.components.MovementComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.extra.entity.effect.Effect;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

/**
 * Effects the    speed of an Entity with a movement component
 */
class SpeedEffect extends Effect {

    private double strength;

    SpeedEffect(double strength, Duration length){
        super(length);
        this.strength = strength;
    }

    @Override
    public void onStart(@NotNull Entity entity) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        movement.incrBaseAccelerationDrag(strength);
    }

    @Override
    public void onEnd(@NotNull Entity entity) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        movement.incrBaseAccelerationDrag(-strength);
    }

}
