package Game;/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import Game.components.MovementComponent;

import static com.almasb.fxgl.app.DSLKt.onKey;
import static com.almasb.fxgl.app.DSLKt.spawn;

/**
 * Shows how to init a basic game object and attach it to the world
 * using fluent API.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class Game extends GameApplication {

    // 1. define types of entities in the game using Enum


    // make the field instance level
    // but do NOT init here for properly functioning save-load system

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        System.out.println("hi");
        settings.setTitle("InitSample");
        settings.setVersion("0.1");
    }

    @Override
    protected void initInput() {
        // TODO: extract this to a method
        onKey(KeyCode.A, "Left", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.left();
            });

        });
        System.out.println("h");
        onKey(KeyCode.D, "Right", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.right();
            });

        });

        onKey(KeyCode.W, "Speed up", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.speedUp();
            });

        });

        onKey(KeyCode.S, "Down", ()->{
            getGameWorld().getEntitiesByComponent(MovementComponent.class).forEach((entity)->{
                MovementComponent component =  entity.getComponent(MovementComponent.class);
                component.slowDown();
            });

        });
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        spawn("CAR", 30, 30);
    }

    public static void main(String[] args){
        launch(args);
    }
}