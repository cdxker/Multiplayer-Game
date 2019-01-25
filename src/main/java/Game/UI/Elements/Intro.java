package Game.UI.Elements;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.scene.IntroScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Intro extends IntroScene {

    private double time;
    private ImageView publisherCredit;
    private Image cognitiveThoughMedia;
    private ImageView developerCredit;
    private Image ZOONSoft;

    @Override
    public void startIntro() {
        time = 0;
        setBackgroundColor(Color.BLACK);

        publisherCredit = new ImageView();
        cognitiveThoughMedia = FXGL.getAssetLoader().loadImage("Cognitive-Thought-Media-Logo.png");
        publisherCredit.setImage(cognitiveThoughMedia);
        publisherCredit.setX(getWidth()/2-cognitiveThoughMedia.getWidth()/2);
        publisherCredit.setY(getHeight()/2-cognitiveThoughMedia.getHeight()/2);

        developerCredit = new ImageView();
        ZOONSoft = FXGL.getAssetLoader().loadImage("ZOONSoft-Logo.png");
        developerCredit.setImage(ZOONSoft);
        developerCredit.setX(getWidth()/2-ZOONSoft.getWidth()/2);
        developerCredit.setY(getHeight()/2-ZOONSoft.getHeight()/2);
    }

    public void onUpdate(double tpf){
        time += 1;
        if(time == 1 * 60) getRoot().getChildren().addAll(publisherCredit);
        else if(time == 3 * 60) getRoot().getChildren().removeAll(publisherCredit);
        else if(time == 4 * 60) getRoot().getChildren().addAll(developerCredit);
        else if(time == 6 * 60) getRoot().getChildren().removeAll(developerCredit);
        else if(time == 7 * 60) finishIntro();
    }

}
