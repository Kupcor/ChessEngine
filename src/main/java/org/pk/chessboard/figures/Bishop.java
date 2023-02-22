package org.pk.chessboard.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessboard.Field;
import org.pk.chessboard.Figure;

import java.util.ArrayList;

public class Bishop extends Figure implements Moves {

    public Bishop(double width, double height, String figureType) {
        super(width, height, figureType);
        if (figureType.equals("B")) {
            this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/WBishop.png")))));
        } else {
            this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/BBishop.png")))));
        }
    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();
        for (int verticalDirection : new int[]{-1, 1}) {
            for (int horizontalDirection : new int[]{-1, 1}) {

                int currentRow = verticalPosition + verticalDirection;
                int currentCol = horizontalPosition + horizontalDirection;

                //  First meet figure with the same color on selected direction is added to list of possible fields which on that
                //  selected figure can move. It's implement "cover" functionality, which prevent covered piece from capture
                //  by opponent king. Mechanism preventing from capturing their own pieces is implemented in eventHandlers
                while (0 <= currentRow && currentRow < 8 && 0 <= currentCol && currentCol < 8) {
                    Field targetField = fieldsList.get(currentRow).get(currentCol);

                    if (targetField.getChildren().isEmpty() || (targetField.getFigure() instanceof King && (this.isFigureWhite != targetField.getFigure().getIsFigureWhite()))) {
                        filteredFields.add(targetField);
                    } else {
                        filteredFields.add(targetField);
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