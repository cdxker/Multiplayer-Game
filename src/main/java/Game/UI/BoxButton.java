package Game.UI;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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


