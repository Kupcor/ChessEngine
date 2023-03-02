package org.pk.chessgame.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessgame.Field;
import org.pk.chessgame.Figure;

import java.util.ArrayList;
import java.util.Objects;

public class King extends Figure implements Moves {

    public King(double width, double height, String figureType) {
        super(width, height, figureType);
        String pawnImage = figureType.equals("K") ? "WKing.png" : "BKing.png";
        this.figureLabel.setGraphic(new ImageView(new Image(Objects.requireNonNull(Field.class.getResource("assets/" + pawnImage)).toString())));
    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> currentStateFields, ArrayList<ArrayList<Field>> previousStateFields, int verticalPosition, int horizontalPosition) {
        boolean isWhiteKing = this.figureType.equals("K");
        Field sourceField = currentStateFields.get(verticalPosition).get(horizontalPosition);
        if (horizontalPosition != 4) this.setDidFigureMove();

        ArrayList<Field> availableFields = new ArrayList<>(this.getFieldsAroundKing(currentStateFields, verticalPosition, horizontalPosition));
        //  Temp unavailable - to be tested
        availableFields.removeIf(field -> !field.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, isWhiteKing).isEmpty());

        if (this.didFigureNotMove && sourceField.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, sourceField.getFigure().getIsFigureWhite()).isEmpty()) {
            //  Short castling
            Field firstRightField = currentStateFields.get(verticalPosition).get(horizontalPosition+1);
            Field secondRightField = currentStateFields.get(verticalPosition).get(horizontalPosition+2);
            Field rookField = currentStateFields.get(verticalPosition).get(horizontalPosition+3);
            if (firstRightField.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, this.getIsFigureWhite()).isEmpty() && firstRightField.getChildren().isEmpty()) {
                if (secondRightField.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, this.getIsFigureWhite()).isEmpty() && secondRightField.getChildren().isEmpty()) {
                    if (!rookField.getChildren().isEmpty() && rookField.getFigure() instanceof Rook &&rookField.getFigure().getDidFigureNotMove()) {
                        availableFields.add(secondRightField);
                    }
                }
            }

            //  Long castling
            Field firstLeftField = currentStateFields.get(verticalPosition).get(horizontalPosition-1);
            Field secondLeftField = currentStateFields.get(verticalPosition).get(horizontalPosition-2);
            Field thirdLeftField = currentStateFields.get(verticalPosition).get(horizontalPosition-3);
            rookField = currentStateFields.get(verticalPosition).get(horizontalPosition-4);
            if (firstLeftField.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, this.getIsFigureWhite()).isEmpty() && firstLeftField.getChildren().isEmpty()) {
                if (secondLeftField.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, this.getIsFigureWhite()).isEmpty() && secondLeftField.getChildren().isEmpty()) {
                    if (thirdLeftField.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, this.getIsFigureWhite()).isEmpty() && thirdLeftField.getChildren().isEmpty()) {
                        if (!rookField.getChildren().isEmpty() && rookField.getFigure() instanceof Rook &&rookField.getFigure().getDidFigureNotMove()) {
                            availableFields.add(secondLeftField);
                        }
                    }
                }
            }
         }
        return availableFields;
    }

    public ArrayList<Field> getFieldsAroundKing(ArrayList<ArrayList<Field>> fieldsList, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> fieldsAroundKing = new ArrayList<>();
        Field targetField;

        //  Standard moves
        for (int currentVerticalPosition = verticalPosition - 1; currentVerticalPosition < verticalPosition + 2; currentVerticalPosition++) {
            for (int currentHorizontalPosition = horizontalPosition - 1; currentHorizontalPosition < horizontalPosition + 2; currentHorizontalPosition++) {
                if (0 > currentVerticalPosition || currentVerticalPosition > 7 || currentHorizontalPosition > 7 || currentHorizontalPosition < 0 ) continue;
                if (currentHorizontalPosition == horizontalPosition && currentVerticalPosition == verticalPosition) continue;
                targetField = fieldsList.get(currentVerticalPosition).get(currentHorizontalPosition);
                fieldsAroundKing.add(targetField);;
            }
        }

        return fieldsAroundKing;
    }
}