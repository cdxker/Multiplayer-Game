package Game.components;

import com.almasb.fxgl.entity.component.Component;

/**
 * A component applies friction to objects
 */
public class FrictionComponent extends Component {
    private double drag;

    public FrictionComponent(double force_value) {
        drag = force_value;
    }

    public double getDrag() {
        return drag;
    }

    public void setDrag(double drag) {
        this.drag = drag;
    }
}
