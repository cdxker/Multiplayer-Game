package Game.UI;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RectText extends StackPane {
    private Rectangle rect;
    private Text text;

    public RectText(RectTextSettings settings) {
        this.text = new Text(settings.text);
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

        this.rect = new Rectangle(width, height, settings.normBgCol);

        this.getChildren().addAll(rect, textBox);
        this.setPrefSize(width, height);
        this.setMaxSize(width, height);
    }

    public Rectangle getRect() {
        return rect;
    }

    public Text getText() {
        return text;
    }
}



