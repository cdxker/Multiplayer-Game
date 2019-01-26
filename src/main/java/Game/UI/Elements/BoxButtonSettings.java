package Game.UI.Elements;

import javafx.scene.paint.Color;

public class BoxButtonSettings extends RectTextSettings {
    public Color otherTextCol;
    public Color otherBgCol;
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

