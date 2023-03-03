package org.pk.chessgame.helperclasses;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.pk.chessgame.Field;
import org.pk.chessgame.Figure;

import java.util.Objects;

//  TODO
public class FigurePromoter extends GridPane {

    public FigurePromoter(Figure figure) {
        this.setPrefSize(25, 200);
        String assetsPrefix = Character.isUpperCase(figure.getFigureType().charAt(0)) ? "W" : "B";
        this.createSelectionBoard(assetsPrefix);
    }

    private void createSelectionBoard(String assetsPrefix) {
        String[] imagesPaths = {"Queen.png", "Rook.png", "Bishop.png", "Knight.png"};
        int row = 0;
        for (String imagePath : imagesPaths) {
            Label label = new Label();
            label.setPrefSize(25, 50);
            label.setGraphic(new ImageView(new Image(Objects.requireNonNull(Field.class.getResource("assets/" + assetsPrefix + imagePath)).toString())));
            GridPane.setConstraints(label, 0, row);
            row++;
            this.getChildren().add(label);
        }
    }
}
