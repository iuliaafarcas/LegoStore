package org.example.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.LegoStoreApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationUi extends Application {
    private ConfigurableApplicationContext applicationContext;
    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }
    @Override
    public void init(){
        SpringApplicationBuilder builder = new SpringApplicationBuilder(LegoStoreApplication.class);
        builder.headless(false);
        applicationContext = builder.run();
    }

    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }
    public static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage){
            super(stage);
        }
        public Stage getStage(){ return (Stage) getSource();}
    }
}
