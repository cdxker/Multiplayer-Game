package Game.UI;

import Game.UI.Elements.BoxButton;
import Game.UI.Elements.BoxButtonSettings;
import Game.UI.Elements.RectText;
import Game.UI.Elements.RectTextSettings;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static Game.UI.MainMenu.OVERPASS_LIGHT_FACTORY;
import static Game.UI.MainMenu.OVERPASS_REGULAR_FACTORY;


public class GameMenu extends FXGLMenu {
    public GameMenu(GameApplication app) {
        super(app, MenuType.GAME_MENU);

        final double heightRatio = app.getHeight() / 600.0;
        final double widthRatio = app.getWidth() / 900.0;
        final double fontRatio = 25.0 / 6;
        final double defactoRatio = (app.getHeight() < app.getWidth()) ? heightRatio : widthRatio; // Actual ratio to be used in scaling menu elements.

        //// Super class adds unwanted nodes to root, so these instructions rids of those unwanted nodes
        //// while maintaining menuRoot and contentRoot.
        getRoot().getChildren().clear();
        getRoot().getChildren().add(menuRoot);
        getRoot().getChildren().add(getContentRoot());

        RectTextSettings headerSettings = new RectTextSettings();
        headerSettings.text = "Paused";
        headerSettings.font = OVERPASS_REGULAR_FACTORY.newFont(12 * fontRatio * defactoRatio);
        headerSettings.normTextCol = Color.WHITE;
        RectText header = new RectText(headerSettings);


        BoxButtonSettings continueSettings = new BoxButtonSettings();
        continueSettings.text = "Continue";
        continueSettings.font = OVERPASS_LIGHT_FACTORY.newFont(7 * fontRatio * defactoRatio);
        continueSettings.action = this::fireResume;
        continueSettings.normTextCol = Color.WHITE;
        continueSettings.otherTextCol = Color.rgb(41, 128, 187);
        BoxButton continueGame = new BoxButton(continueSettings);

        BoxButtonSettings returnSettings = new BoxButtonSettings();
        returnSettings.text = "Return to main menu";
        returnSettings.font = OVERPASS_LIGHT_FACTORY.newFont(7 * fontRatio * defactoRatio);
        returnSettings.action = this::fireExitToMainMenu;
        returnSettings.normTextCol = Color.WHITE;
        returnSettings.otherTextCol = Color.rgb(41, 128, 187);
        BoxButton gotoMainMenu = new BoxButton(returnSettings);

        VBox items = new VBox(header, continueGame, gotoMainMenu);
        items.setAlignment(Pos.CENTER);
        items.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.50), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox wrapper = new VBox(items);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPrefWidth(app.getWidth());
        wrapper.setPrefHeight(app.getHeight());

        menuRoot.getChildren().add(wrapper);
        menuRoot.setBackground(new Background(new BackgroundImage(FXGL.getAssetLoader().loadImage("PowerupTessellation.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    @Override
    protected void switchMenuTo(Node menuNode) {
        menuRoot.getChildren().clear();
        menuRoot.getChildren().add(menuNode);
    }

    @Override
    protected void switchMenuContentTo(Node content) {
        getContentRoot().getChildren().clear();
        getContentRoot().getChildren().add(content);
    }

    @Override
    protected Button createActionButton(String name, Runnable action) {
        Button btn = new Button(name);
        btn.addEventHandler(ActionEvent.ACTION, event -> action.run());
        return btn;
    }

    @Override
    protected Button createActionButton(StringBinding name, Runnable action) {
        return createActionButton(name.getValue(), action);
    }

    @Override
    protected Node createBackground(double width, double height) {
        Rectangle bg = new Rectangle();
        bg.setFill(Color.RED);
        return bg;
    }

    @Override
    protected Node createTitleView(String title) {
        return FXGL.getUIFactory().newText(title);
    }

    @Override
    protected Node createVersionView(String version) {
        return FXGL.getUIFactory().newText(version);
    }

    @Override
    protected Node createProfileView(String profileName) {
        return FXGL.getUIFactory().newText(profileName);
    }
}
