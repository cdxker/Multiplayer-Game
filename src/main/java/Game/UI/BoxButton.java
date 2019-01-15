package Game.UI;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BoxButton extends RectText {
    public BoxButton(BoxButtonSettings settings) {
        super(settings);
        Rectangle rect = this.getRect();
        Text text = this.getText();
        this.setOnMouseEntered(event -> {
            rect.setFill(settings.otherBgCol);
            text.setFill(settings.otherTextCol);
        });
        this.setOnMouseExited(event -> {
            rect.setFill(settings.normBgCol);
            text.setFill(settings.normTextCol);
        });
        this.setOnMouseClicked(event -> settings.action.run());
    }
}


