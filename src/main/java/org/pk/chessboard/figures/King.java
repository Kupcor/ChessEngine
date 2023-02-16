package org.pk.chessboard.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessboard.Field;
import org.pk.chessboard.Figure;

import java.util.ArrayList;


public class King extends Figure implements Moves {

    public King(double width, double height, String figureType) {
        super(width, height, figureType);
        if (figureType.equals("K")) {
            this.figure.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/WKing.png")))));
        } else {
            this.figure.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/BKing.png")))));
        }
    }

    @Override
    public ArrayList<Field> setMoveRestrictions(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredList = new ArrayList<>();
        Field sourceField = fieldsList.get(verticalPosition).get(horizontalPosition);
        Field targetField;

        //  Standard moves
        for (int currentVerticalPosition = verticalPosition - 1; currentVerticalPosition < verticalPosition + 2; currentVerticalPosition++) {
            for (int currentHorizontalPosition = horizontalPosition - 1; currentHorizontalPosition < horizontalPosition + 2; currentHorizontalPosition++) {
                if (0 > currentVerticalPosition || currentVerticalPosition > 7 || currentHorizontalPosition > 7 || currentHorizontalPosition < 0 ) continue;
                targetField = fieldsList.get(currentVerticalPosition).get(currentHorizontalPosition);
                if (!targetField.getChildren().isEmpty() && sourceField.getFigure().getIsFigureWhite() == targetField.getFigure().getIsFigureWhite()) continue;
                filteredList.add(targetField);
            }
        }

        return filteredList;
    }
}