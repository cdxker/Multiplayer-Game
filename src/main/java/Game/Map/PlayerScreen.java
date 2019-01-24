package Game.Map;

import Game.EntityType;
import Game.components.ScreenComponent;
import Game.components.ScreenComponent2;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.app.FXGL.*;


/**
 * Class that
 */
public class PlayerScreen extends Pane {
    private static int instances = 0;
    private Rectangle rect;
    private Entity target;
    private int id;

    public PlayerScreen(Rectangle rect, Entity target){
        super();
        id = instances;
        instances ++;
        this.rect = rect; // rectangle should initially be placed where the entity begins.
        this.target = target;
        setMinSize(rect.getWidth(), rect.getHeight());
        setMaxSize(rect.getWidth(), rect.getHeight());
        setTranslateX(rect.getX());
        setTranslateY(rect.getY());
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
        Rectangle bg = new Rectangle(0, 0, getAppWidth(), getAppHeight());
        bg.setFill(Color.WHITE);
        getChildren().add(0, bg);

        // Draws entities on game world
        getGameWorld().getEntitiesByType(EntityType.Tile, EntityType.Wall, EntityType.PowerUp, EntityType.FINISHLINE).forEach(this::add);
        getGameWorld().getEntitiesByType(EntityType.Player1, EntityType.Player2,EntityType.Bullet).forEach(this::add);

        //setTranslateY(-target.getPosition().getY() + rect.getHeight() / 2); // Changing the value makes the rendering different
        //setTranslateX(-target.getPosition().getX() + rect.getWidth() / 2);  // Changing the value makes the rendering different
        rect.setX(target.getPosition().getX() - rect.getWidth() / 2);
        rect.setY(target.getPosition().getY() - rect.getHeight() / 2);
    }

    private void add(Entity e) {
        if(!rect.contains(e.getPosition())){
            return;
        }
        //System.out.println(e.getPosition().multiply(1.0/64.0));
        Node view;
        if(id == 0) {
            view = e.getComponent(ScreenComponent.class).getView();
        }else{
            view = e.getComponent(ScreenComponent2.class).getView();
        }
        System.out.println(e.getPosition().multiply(1/64.0));
        view.translateXProperty().bind(e.xProperty().subtract(rect.getX()));
        view.translateYProperty().bind(e.yProperty().subtract(rect.getY()));
        getChildren().add(view);
    }

    public void add(Node view){
        view.setTranslateX(view.getTranslateX() - rect.getX());
        view.setTranslateX(view.getTranslateX() - rect.getY());
        getChildren().add(view);
    }


    public void addAll(Node... views){
        for (Node view: views) {
            add(view);
        }
    }


}
