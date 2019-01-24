package Game.UI.Elements;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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


