package Game.UI;

import com.almasb.fxgl.ui.FXGLTextFlow;
import com.almasb.fxgl.ui.FontType;
import com.almasb.fxgl.ui.MDIWindow;
import com.almasb.fxgl.ui.UIFactory;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UIElements implements UIFactory {

    @Override
    public MDIWindow newWindow() {
        return null;
    }

    @Override
    public Font newFont(double v) {
        return null;
    }

    @Override
    public Font newFont(FontType fontType, double v) {
        return null;
    }

    @Override
    public Text newText(String s) {
        return null;
    }

    @Override
    public Text newText(String s, double v) {
        return null;
    }

    @Override
    public Text newText(String s, Color color, double v) {
        return null;
    }

    @Override
    public Text newText(StringExpression stringExpression) {
        return null;
    }

    @Override
    public Button newButton(String s) {
        return null;
    }

    @Override
    public Button newButton(StringBinding stringBinding) {
        return null;
    }

    @Override
    public <T> ChoiceBox<T> newChoiceBox(ObservableList<T> observableList) {
        return null;
    }

    @Override
    public <T> ChoiceBox<T> newChoiceBox() {
        return null;
    }

    @Override
    public CheckBox newCheckBox() {
        return null;
    }

    @Override
    public <T> Spinner<T> newSpinner(ObservableList<T> observableList) {
        return null;
    }

    @Override
    public <T> ListView<T> newListView(ObservableList<T> observableList) {
        return null;
    }

    @Override
    public <T> ListView<T> newListView() {
        return null;
    }

    @Override
    public FXGLTextFlow newTextFlow() {
        return null;
    }
}
