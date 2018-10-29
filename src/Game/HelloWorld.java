package Game;/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */


import Game.server.Client;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import Game.components.KeyPressComponent;

import static com.almasb.fxgl.app.DSLKt.onKey;
import static com.almasb.fxgl.app.DSLKt.spawn;

/**
 * Shows how to init a basic game object and attach it to the world
 * using fluent API.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class HelloWorld extends GameApplication {

    // 1. define types of entities in the game using Enum


    // make the field instance level
    // but do NOT init here for properly functioning save-load system

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        System.out.println("hi");
        settings.setTitle("InitSample");
        settings.setVersion("0.1");
    }

    @Override
    protected void initInput() {
        // TODO: extract this to a method
        onKey(KeyCode.A, "Left", ()->{
            getGameWorld().getEntitiesByComponent(KeyPressComponent.class).forEach((entity)->{
                KeyPressComponent component =  entity.getComponent(KeyPressComponent.class);
                component.left();
                System.out.println("h");
            });

        });
        System.out.println("h");
        onKey(KeyCode.D, "Right", ()->{
            getGameWorld().getEntitiesByComponent(KeyPressComponent.class).forEach((entity)->{
                KeyPressComponent component =  entity.getComponent(KeyPressComponent.class);
                component.right();
            });

        });

        onKey(KeyCode.W, "UP", ()->{
            getGameWorld().getEntitiesByComponent(KeyPressComponent.class).forEach((entity)->{
                KeyPressComponent component =  entity.getComponent(KeyPressComponent.class);
                component.up();
            });

        });

        onKey(KeyCode.S, "Down", ()->{
            getGameWorld().getEntitiesByComponent(KeyPressComponent.class).forEach((entity)->{
                KeyPressComponent component =  entity.getComponent(KeyPressComponent.class);
                component.down();
            });

        });

        onKey(KeyCode.SPACE , "Send", new SendData());
    }

    public class SendData implements Runnable{
        public double x, y;
        public SendData(){
            x = y = 3;
        }

        @Override
        public void run() {
            Client client = new Client();
            client.start();
            client.send(String.format("pos:%f,%f", x, y).getBytes(), 3000);
            x+= 0.5;
            y+= 0.5;
        }
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CarFactory());
        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        spawn("CAR", 30, 30);
        spawn("Enemy", 20, 20);
    }

    public static void main(String[] args){
        launch(args);
    }
}