package org.pk.chessboard.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessboard.Field;
import org.pk.chessboard.Figure;

import java.util.ArrayList;

public class Rook extends Figure implements Moves {

    public Rook(double width, double height, String figureType) {
        super(width, height, figureType);
        if (figureType.equals("R")) this.figure.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/WTower.png")))));
        else this.figure.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/BTower.png")))));

    }

    @Override
    public ArrayList<Field> setMoveRestrictions(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();
        Field sourceField = fieldsList.get(verticalPosition).get(horizontalPosition);
        for (int verticalDirection : new int[] {-1, 1, 0}) {
            for (int horizontalDirection : new int[] {0, -1, 1}) {
                if (verticalDirection != 0 && horizontalDirection != 0) {
                    continue;
                }

                int currentRow = verticalPosition + verticalDirection;
                int currentCol = horizontalPosition + horizontalDirection;

                while (0 <= currentRow && currentRow < 8 && 0 <= currentCol && currentCol < 8) {
                    Field targetField = fieldsList.get(currentRow).get(currentCol);

                    if (targetField.getChildren().isEmpty()) {
                        filteredFields.add(targetField);
                    } else if (targetField.getFigure().getIsFigureWhite() != sourceField.getFigure().getIsFigureWhite()) {
                        filteredFields.add(targetField);
                        break;
                    } else {
                        break;
                    }

                    currentRow += verticalDirection;
                    currentCol += horizontalDirection;
                }
            }
        }
        return filteredFields;
    }
}
