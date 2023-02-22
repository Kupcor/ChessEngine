package org.pk.chessboard.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessboard.Field;
import org.pk.chessboard.Figure;

import java.util.ArrayList;

public class Knight extends Figure implements Moves {

    public Knight(double width, double height, String figureType) {
        super(width, height, figureType);
        if (figureType.equals("N")) {
            this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/WKnight.png")))));
        } else {
            this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/BKnight.png")))));
        }
    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();

        int[][] directions = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] direction : directions) {
            int verticalDirection = verticalPosition + direction[0];
            int horizontalDirection = horizontalPosition + direction[1];

            if (verticalDirection < 0 || verticalDirection > 7 || horizontalDirection < 0 || horizontalDirection > 7) {
                continue;
            }

            Field targetField = fieldsList.get(verticalDirection).get(horizontalDirection);
            filteredFields.add(targetField);

        }

        return filteredFields;
    }
}