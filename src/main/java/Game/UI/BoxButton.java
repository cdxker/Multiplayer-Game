package Game.UI;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BoxButton extends StackPane {
    public BoxButton(BoxButtonSettings settings) {
        Text text = new Text(settings.text);
        text.setFont(settings.font);
        text.setFill(settings.normTextCol);

        double textWidth = text.getBoundsInLocal().getWidth();
        double textHeight = text.getBoundsInLocal().getHeight();
        double width = textWidth + 2 * settings.hMargin;
        double height = textHeight + 2 * settings.vMargin;

        HBox textBox = new HBox(text);
        textBox.setPadding(new Insets(settings.vMargin, settings.hMargin, settings.vMargin, settings.hMargin));
        textBox.setPrefSize(width, height);
        textBox.setMaxSize(width, height);

        Rectangle rect = new Rectangle(width, height, settings.normBgCol);

        this.getChildren().addAll(rect, textBox);
        this.setOnMouseEntered(event -> {
            rect.setFill(settings.otherBgCol);
            text.setFill(settings.otherTextCol);
        });
        this.setOnMouseExited(event -> {
            rect.setFill(settings.normBgCol);
            text.setFill(settings.normTextCol);
        });
        this.setOnMouseClicked(event -> settings.action.run());
        this.setPrefSize(width, height);
        this.setMaxSize(width, height);
    }
}


