package Game;

import Game.components.MovementComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.app.DSLKt.texture;

public class CarFactory implements EntityFactory {

    @Spawns("Car")
    public Entity newCar(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return Entities.builder()
                .type(EntityType.Car)
                .from(data)
                .viewFromNodeWithBBox(texture("arrow.png", 40, 20))
                .with(new CollidableComponent(true))
                .with(new MovementComponent())
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }

    @Spawns("Bullet")
    public Entity newEnemy(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return Entities.builder()
                .type(EntityType.Bullet)
                .from(data)
                .viewFromNode(new Rectangle(30, 30))
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }
}
