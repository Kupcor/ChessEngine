package org.pk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.pk.chessgame.Figure;
import org.pk.chessgame.helperclasses.FigurePromoter;

import java.io.IOException;

//  Hierarchy
//  Stage
//      Scene
//          Groups
//              Panes / FXML

public class App extends Application {
    private static Scene scene;
    private static final Group mainGroup = new Group();

    private final ChessGameUI chessGameUI = new ChessGameUI(480, 480);

    @Override
    public void start(Stage mainStage) throws IOException {
        //  stage parameters
        mainStage.setTitle("Chess game");

        FigurePromoter figurePromoter = new FigurePromoter(Figure.createFigure(50, 50, "P"));
        mainGroup.getChildren().add(chessGameUI);


        // scene = new Scene(loadFXML("primary"), 500, 500); For now we do not need this
        scene = new Scene(mainGroup, Color.BLACK);
        mainStage.setScene(scene);

        mainStage.show();
    }

    //  This is two default method which I can delete, but better not touch this now in order to understand Controller
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}