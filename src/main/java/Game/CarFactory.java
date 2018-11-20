package Game;

import Game.components.DamageComponent;
import Game.components.HealthComponent;
import Game.components.MovementComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.app.DSLKt.texture;

public class CarFactory implements EntityFactory {

    @Spawns("Car")
    public Entity newCar(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(0, 5));

        return Entities.builder()
                .type(EntityType.Car)
                .from(data)
                .viewFromNodeWithBBox(texture("arrow.png", 40, 20))
                .with(new CollidableComponent(true))
                .with(new MovementComponent())
                .with(new HealthComponent(100))
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }

    @Spawns("Bouncy Bullet")
    public Entity newBullet(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        Point2D velocity = data.get("velocity");
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(velocity));

        return Entities.builder()
                .type(EntityType.Bullet)
                .from(data)
                .viewFromNodeWithBBox(new Circle(20))
                .with(new CollidableComponent(true))
                .with(new DamageComponent(5))
                .with(physics)
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }
}
