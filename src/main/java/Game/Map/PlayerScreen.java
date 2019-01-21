package Game.Map;

import Game.EntityType;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.app.FXGL.*;


/**
 * Class that
 */
public class PlayerScreen extends Pane {
    private Rectangle rect;
    private Entity target;
    
    public PlayerScreen(Rectangle rect, Entity target){
        super();
        this.rect = rect; // rectangle should initially be placed where the entity begins.
        setTranslateX(rect.getTranslateX());
        setTranslateY(rect.getTranslateY());
        this.target = target;
    }

    @Override
    public boolean contains(double v, double v1) {
        return rect.contains(v, v1);
    }

    @Override
    public boolean contains(Point2D point2D) {
        return rect.contains(point2D);
    }

    public void onUpdate() {
        getChildren().remove(0, getChildren().size());
        for(Entity e : getGameWorld().getEntitiesByType(EntityType.Tile, EntityType.Wall, EntityType.PowerUp)){
            getChildren().add(e.getView());
        }
        getGameWorld().getEntitiesByType(EntityType.Car, EntityType.Bullet).forEach((e) ->getChildren().add(e.getView()));
        setTranslateY(-target.getPosition().getY() + rect.getHeight() / 2); // Changing the value makes the rendering different
        setTranslateX(-target.getPosition().getX() + rect.getWidth() / 2);  // Changing the value makes the rendering different

        rect.setX(target.getPosition().getX() - rect.getWidth() / 2);
        rect.setY(target.getPosition().getY() - rect.getHeight() / 2);

        rect.setX(target.getPosition().getX() - rect.getWidth() / 2);
        rect.setY(target.getPosition().getY() - rect.getHeight() / 2);
    }



}
