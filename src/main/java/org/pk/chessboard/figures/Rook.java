package org.pk.chessboard.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessboard.Field;
import org.pk.chessboard.Figure;

import java.util.ArrayList;

public class Rook extends Figure implements Moves {

    public Rook(double width, double height, String figureType) {
        super(width, height, figureType);
        if (figureType.equals("R")) this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/WTower.png")))));
        else this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/BTower.png")))));

    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();
        for (int verticalDirection : new int[] {-1, 1, 0}) {
            for (int horizontalDirection : new int[] {0, -1, 1}) {
                if (verticalDirection != 0 && horizontalDirection != 0) {
                    continue;
                }

                int currentRow = verticalPosition + verticalDirection;
                int currentCol = horizontalPosition + horizontalDirection;

                while (0 <= currentRow && currentRow < 8 && 0 <= currentCol && currentCol < 8) {
                    Field targetField = fieldsList.get(currentRow).get(currentCol);

                    //  First meet figure with the same color on selected direction is added to list of possible fields which on that
                    //  selected figure can move. It's implement "cover" functionality, which prevent covered piece from capture
                    //  by opponent king. Mechanism preventing from capturing their own pieces is implemented in eventHandlers
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
        filteredFields.remove(fieldsList.get(verticalPosition).get(horizontalPosition));
        return filteredFields;
    }
}
