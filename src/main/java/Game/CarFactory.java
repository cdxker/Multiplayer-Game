package Game;

import Game.components.*;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.extra.entity.effect.EffectComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.app.DSLKt.texture;

public class CarFactory implements EntityFactory {

    private Entities.EntityBuilder genericPlayer(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(0, 0));

        return  Entities.builder()
                .from(data)
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new MovementComponent(0.1,0.95,0.80,1,0.5))
                .with(new EffectComponent());

    }

    @Spawns("Player1")
    public Entity spawnPlayer1(SpawnData data) {
        Node view1 = texture("car.png", 64, 64/2);
        Node view2 = texture("car.png", 64, 64/2);

        return genericPlayer(data)
                .type(EntityType.Player1)
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .with(new HealthComponent(100))
                .with(new GunComponent("Bullet", 50))
                .build();
    }

    @Spawns("Player2")
    public Entity spawnPlayer2(SpawnData data) {
        Node view1 = texture("car.png", 64, 64/2);
        Node view2 = texture("car.png", 64, 64/2);

        return genericPlayer(data)
                .type(EntityType.Player2)
                .viewFromNodeWithBBox(view1)
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .with(new HealthComponent(100))
                .with(new GunComponent("Bullet", 50))
                .build();
    }

    @Spawns("Ball")
    public Entity newBall(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1.0f)); // makes it bounce off stuff
        Point2D velocity = data.get("velocity"); // gets the data injected from spawns() (look in game app implementation)
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(velocity));
        Node view = new Circle(20);
        return Entities.builder()
                .type(EntityType.Ball)
                .from(data)
                .viewFromNodeWithBBox(view)
                .with(physics)
                .with(new ScreenComponent(view))
                .with(new ScreenComponent2(view))
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }

    @Spawns("Bullet")
    public Entity newBullet(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        Point2D velocity = data.get("velocity");
        Entity player = data.get("player");
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(velocity));
        Node view1 = new Circle(5);
        Node view2 = new Circle(5);

        return Entities.builder()
                .type(EntityType.Bullet)
                .from(data)
                .viewFromNodeWithBBox(view1)
                .with(new CollidableComponent(true))
                .with(new DamageComponent(5))
                .with(new ScreenComponent(view1))
                .with(new ScreenComponent2(view2))
                .with(new BulletComponent(player, velocity))
                .with(physics)
                .build();
    }
}
