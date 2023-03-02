package org.pk.chessgame.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessgame.Field;
import org.pk.chessgame.Figure;

import java.util.ArrayList;
import java.util.Objects;

public class Queen extends Figure implements Moves {

    public Queen(double width, double height, String figureType) {
        super(width, height, figureType);
        String pawnImage = figureType.equals("Q") ? "WQueen.png" : "BQueen.png";
        this.figureLabel.setGraphic(new ImageView(new Image(Objects.requireNonNull(Field.class.getResource("assets/" + pawnImage)).toString())));
    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> currentStateFields, ArrayList<ArrayList<Field>> previousStateFields, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();
        for (int verticalDirection : new int[] {-1, 1, 0}) {
            for (int horizontalDirection : new int[] {0, -1, 1}) {

                int currentRow = verticalPosition + verticalDirection;
                int currentCol = horizontalPosition + horizontalDirection;

                //  First meet figure with the same color on selected direction is added to list of possible fields which on that
                //  selected figure can move. It's implement "cover" functionality, which prevent covered piece from capture
                //  by opponent king. Mechanism preventing from capturing their own pieces is implemented in eventHandlers
                //  It should stop on your king
                while (0 <= currentRow && currentRow < 8 && 0 <= currentCol && currentCol < 8) {
                    Field targetField = currentStateFields.get(currentRow).get(currentCol);

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
        filteredFields.remove(currentStateFields.get(verticalPosition).get(horizontalPosition));
        return filteredFields;
    }
}