package Game;

import Game.components.KeyPressComponent;
import Game.components.ServerControlComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.extra.entity.components.KeepOnScreenComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.shape.Rectangle;


public class CarFactory implements EntityFactory {

    @Spawns("CAR")
    public Entity newCar(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return Entities.builder()
                .type(EntityType.CAR)
                .from(data)
                .viewFromNode(new Rectangle(30, 30))
                .with(new CollidableComponent(true))
                .with(new KeyPressComponent())
                .with(new KeepOnScreenComponent(true, true))
                .build();
    }

    @Spawns("Enemy")
    public Entity newEnemy(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return Entities.builder()
                .type(EntityType.ENEMY)
                .from(data)
                .viewFromNode(new Rectangle(30, 30))
                .with(new CollidableComponent(true))
                .with(new ServerControlComponent(3000))
                .with(new KeepOnScreenComponent(true, true))
                .with(new KeyPressComponent())
                .build();
    }
}
