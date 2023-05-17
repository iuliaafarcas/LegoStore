package org.example;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.example.controller.LegoStoreController;
import org.example.ui.ApplicationUi;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<ApplicationUi.StageReadyEvent> {
    public StageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    private final FxWeaver fxWeaver;

    @Override
    public void onApplicationEvent(ApplicationUi.StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.setScene(new Scene(fxWeaver.loadView(LegoStoreController.class), 800, 600));
        stage.setTitle("Lego store project");
        stage.show();
    }

}
