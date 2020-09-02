package io.starskyoio.asr;

import io.starskyoio.asr.ui.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        launch(Application.class, MainView.class, args);
    }
}
