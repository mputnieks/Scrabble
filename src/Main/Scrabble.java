package Main;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.VisualsManager;

public class Scrabble extends Application {
    
    public final static Pane root = new StackPane();
    public Controller controller;
    private final static int window_x = 1200;
    private final static int window_y = 700;
    
    private Parent createContent(){
        root.setPrefSize(window_x,window_y);
        
        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
        return root;
    }
    
    private void onUpdate(){
        controller.update();
    }
    
    @Override
    public void start(Stage primaryStage) {
    	controller = new Controller();
    	root.setBackground(VisualsManager.getGameBackground());
        primaryStage.setTitle("Scrabble");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	controller.exit();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}