package Game.components;

import com.almasb.fxgl.entity.component.Component;

public class DamageComponent extends Component {

    double damage;

    public DamageComponent(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }
}
