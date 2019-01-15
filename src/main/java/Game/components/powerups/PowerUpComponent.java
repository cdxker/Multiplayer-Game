package Game.components.powerups;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.extra.entity.effect.Effect;

/**
 * A Component that holds effects
 * for a power up
 */
public class PowerUpComponent extends Component {

    private Effect effect;

    PowerUpComponent(Effect effect){
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }
}
