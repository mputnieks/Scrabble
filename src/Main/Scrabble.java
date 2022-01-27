package Main;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
    	controller.initGraphics();
        primaryStage.setTitle("Scrabble");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}