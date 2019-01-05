package Game.UI;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.scene.FXGLMenu;
import com.almasb.fxgl.scene.menu.MenuType;
import com.almasb.fxgl.ui.FontFactory;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/*
 * To-do list area...
 * TODO: Think about maybe implementing a way for the user to submit their own profile icon rather than using built-in icons.
 * TODO: Maybe use this code somewhere?
        Image backgroundImage = FXGL.getAssetLoader().loadImage("BulletHailPattern.png");
        mainLayout.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
 */
public class MainMenu extends FXGLMenu {
    public MainMenu(GameApplication app) {
        super(app, MenuType.MAIN_MENU);

        //// Super class adds unwanted nodes to root so these instructions rids of those unwanted nodes
        //// while maintaining menuRoot and contentRoot.
        getRoot().getChildren().clear();
        getRoot().getChildren().add(menuRoot);
        getRoot().getChildren().add(getContentRoot());

        final FontFactory OVERPASS_LIGHT_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-light.otf");
        final FontFactory OVERPASS_LIGHT_ITALIC_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-light-italic.otf");
        final FontFactory OVERPASS_REGULAR_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-regular.otf");
        final FontFactory OVERPASS_HEAVY_ITALIC_FACTORY = FXGL.getAssetLoader().loadFont("overpass/overpass-heavy-italic.otf");
        final FontFactory HACK_REGULAR_FACTORY = FXGL.getAssetLoader().loadFont("hack/Hack-Regular.ttf");
        final double heightRatio = app.getHeight() / 600.0;
        final double widthRatio = app.getWidth() / 900.0;
        final double fontRatio = 25.0 / 6;
        final double defactoRatio = (app.getHeight() < app.getWidth()) ? heightRatio : widthRatio; // Actual ratio to be used in scaling menu elements.

        //// Adjusting the overall layout of the main menu
        double marginSize = 42 * defactoRatio;
        GridPane mainLayout = new GridPane();
        RowConstraints row = new RowConstraints(app.getHeight() / 2.0 - marginSize);
        ColumnConstraints col = new ColumnConstraints(app.getWidth() / 2.0 - marginSize);
        mainLayout.getRowConstraints().addAll(row, row);
        mainLayout.getColumnConstraints().addAll(col, col);
        mainLayout.setPadding(new Insets(marginSize));
        //mainLayout.setBackground(new Background(new BackgroundFill(Color.rgb(230, 224, 211),CornerRadii.EMPTY, Insets.EMPTY)));



        //// Make header and add it to main layout
        Text title = new Text(app.getSettings().getTitle());
        title.setFont(OVERPASS_HEAVY_ITALIC_FACTORY.newFont(12 * fontRatio * defactoRatio));

        Text version = new Text(app.getSettings().getVersion());
        version.setFont(OVERPASS_LIGHT_ITALIC_FACTORY.newFont(4 * fontRatio * defactoRatio));
        version.setFill(Color.rgb(120, 120, 120));

        VBox header = new VBox(title, version);
        header.setSpacing(-9 * defactoRatio);

        mainLayout.add(header, 0, 0);


        //// Make TextButtons on left side of menu and add it to main layout
        VBox textButtons = new VBox();
        textButtons.setSpacing(18 * defactoRatio);
        textButtons.setAlignment(Pos.BOTTOM_LEFT);

        Font overpassReg = OVERPASS_REGULAR_FACTORY.newFont(12 * fontRatio * defactoRatio);
        Color orange = Color.rgb(255, 179, 71);
        TextButton play = new TextButton("Play", overpassReg, orange, this::fireNewGame);
        TextButton modifyCars = new TextButton("Modify Cars", overpassReg, orange, this::doNothing);

        Font overpassLight = OVERPASS_LIGHT_FACTORY.newFont(10 * fontRatio * defactoRatio);
        Color blue = Color.rgb(41, 128, 187);
        TextButton settings = new TextButton("Settings", overpassLight, blue, this::doNothing);
        TextButton changeProfile = new TextButton("Change profile", overpassLight, blue, this::fireLogout);
        TextButton exitGame = new TextButton("Exit Game", overpassLight, blue, this::fireExit);
        textButtons.getChildren().addAll(play, modifyCars, settings, changeProfile, exitGame);

        GridPane.setValignment(textButtons, VPos.BOTTOM);
        mainLayout.add(textButtons, 0, 1);


        //// Make profile area that is in the top right area of the main menu
        HBox profileArea = new HBox();
        profileArea.setSpacing(11 * defactoRatio);
        profileArea.setAlignment(Pos.TOP_RIGHT);

        Text profileName = new Text();
        profileName.setFont(HACK_REGULAR_FACTORY.newFont(5 * fontRatio * defactoRatio));
        profileName.setTextAlignment(TextAlignment.RIGHT);
        profileName.setTranslateY(7 * defactoRatio);
        listener.profileNameProperty().addListener((o, oldValue, newValue) -> profileName.setText(newValue));

        Rectangle iconBox = new Rectangle(37 * defactoRatio, 37 * defactoRatio, null);
        iconBox.setStroke(Color.BLACK);
        Image profilePicture = FXGL.getAssetLoader().loadImage("Profile pictures/tire.png");
        iconBox.setFill(new ImagePattern(profilePicture));

        profileArea.getChildren().addAll(profileName, iconBox);

        GridPane.setHalignment(profileArea, HPos.RIGHT);
        mainLayout.add(profileArea, 1, 0);


        //// Make the "What's New?" button on the bottom right of the main menu
        BoxButtonSettings whatsNewConfig = new BoxButtonSettings();
        whatsNewConfig.text = "What's new?";
        whatsNewConfig.font = OVERPASS_LIGHT_FACTORY.newFont(4 * fontRatio * defactoRatio);
        whatsNewConfig.setBackgroundAndTextColors(Color.hsb(180, 1, 0.5));
        whatsNewConfig.hMargin = 13 * defactoRatio;
        whatsNewConfig.vMargin = 4 * defactoRatio;
        StackPane whatsNewPane = new BoxButton(whatsNewConfig);

        whatsNewPane.setAlignment(Pos.BOTTOM_RIGHT);
        GridPane.setHalignment(whatsNewPane, HPos.RIGHT);
        GridPane.setValignment(whatsNewPane, VPos.BOTTOM);
        mainLayout.add(whatsNewPane, 1, 1);

        menuRoot.getChildren().add(mainLayout);

    }

    private void doNothing() {
        // Do nothing
    }

    public class TextButton extends Text {
        public TextButton(String text, Font font, Color normCol, Color otherCol, Runnable action) {
            super(text);
            this.setFont(font);
            this.setFill(normCol);
            this.setOnMouseEntered(event -> this.setFill(otherCol));
            this.setOnMouseExited(event -> this.setFill(normCol));
            this.setOnMouseClicked(event -> action.run());
        }

        public TextButton(String text, Font font, Color otherCol, Runnable action) {
            this(text, font, Color.BLACK, otherCol, action);
        }
    }

    public class BoxButton extends StackPane {
        public BoxButton(BoxButtonSettings settings) {
            Text text = new Text(settings.text);
            text.setFont(settings.font);
            text.setFill(settings.normTextCol);
            text.setTranslateY(-settings.vMargin);
            text.setTranslateX(-settings.hMargin);

            double textWidth = text.getBoundsInLocal().getWidth();
            double textHeight = text.getBoundsInLocal().getHeight();
            Rectangle rect = new Rectangle(textWidth + 2 * settings.hMargin, textHeight + 2 * settings.vMargin, settings.normBgCol);

            this.getChildren().addAll(rect, text);
            this.setOnMouseEntered(event -> {
                rect.setFill(settings.otherBgCol);
                text.setFill(settings.otherTextCol);
            });
            this.setOnMouseExited(event -> {
                rect.setFill(settings.normBgCol);
                text.setFill(settings.normTextCol);
            });
            this.setOnMouseClicked(event -> settings.action.run());
            this.setMaxSize(rect.getWidth(), rect.getHeight());
        }
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

    public class BoxButtonSettings {
        public String text;
        public Font font;
        public Color normTextCol;
        public Color otherTextCol;
        public Color normBgCol;
        public Color otherBgCol;
        public double vMargin;
        public double hMargin;
        public Runnable action = () -> {
        };

        /**
         * Given the normTextCol, the otherTextCol is set as the complimentary color of normTextCol; The class'
         * normTextCol is set as the Color object passed as the parameter.
         *
         * @param normTextCol Color object to set normTextCol and its complimentary color to set otherTextCol.
         */
        public void setComplimentTextColors(Color normTextCol) {
            this.normTextCol = normTextCol;
            this.otherTextCol = normTextCol.deriveColor(180, 1, 1, 1);
        }

        /**
         * Given the normBgCol, the otherBgCol is set as the complimentary color of normBgCol; The class'
         * normBgCol is set as the Color object passed as the parameter.
         *
         * @param normBgCol Color object to set normBgCol and its complimentary color to set otherBgCol.
         */
        public void setComplimentBgColors(Color normBgCol) {
            this.normBgCol = normBgCol;
            this.otherBgCol = normBgCol.deriveColor(180, 1, 1, 1);
        }

        /**
         * Given the normBgCol, otherBgCol is set as the complimentary color of normBgCol. The normTextCol and
         * otherTextCol are set as white if their respective background colors have a brightness less than 0.5, otherwise
         * they are set as black.
         *
         * @param normBgCol Color object to set normBgCol and its complimentary color to set otherBgCol. Both are to be
         *                  used in determining whether normTextCol and otherTextCol are white or black, each.
         */
        public void setBackgroundAndTextColors(Color normBgCol) {
            setComplimentBgColors(normBgCol);
            this.normTextCol = (normBgCol.getHue() < 0.5) ? Color.WHITE : Color.BLACK;
            this.otherTextCol = (otherBgCol.getHue() < 0.5) ? Color.WHITE : Color.BLACK;
        }
    }
}