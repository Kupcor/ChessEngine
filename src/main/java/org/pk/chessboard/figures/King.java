package org.pk.chessboard.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessboard.Field;
import org.pk.chessboard.Figure;

import java.util.ArrayList;

public class King extends Figure implements Moves {

    public King(double width, double height, String figureType) {
        super(width, height, figureType);
        if (this.figureType.equals("K")) {
            this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/WKing.png")))));
        } else {
            this.figureLabel.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/BKing.png")))));
        }
    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        boolean isWhiteKing = this.figureType.equals("K");
        Field sourceField = fieldsList.get(verticalPosition).get(horizontalPosition);
        if (horizontalPosition != 4) this.setDidFigureMove();

        ArrayList<Field> availableFields = new ArrayList<>(this.getFieldsAroundKing(fieldsList, verticalPosition, horizontalPosition));
        //  Temp unavailable - to be tested
        availableFields.removeIf(field -> !field.checkIfFieldIsUnderAttack(fieldsList, previousBoardState, isWhiteKing).isEmpty());

        if (this.didFigureNotMove && sourceField.checkIfFieldIsUnderAttack(fieldsList, previousBoardState, sourceField.getFigure().getIsFigureWhite()).isEmpty()) {
            //  Short castling
            Field firstRightField = fieldsList.get(verticalPosition).get(horizontalPosition+1);
            Field secondRightField = fieldsList.get(verticalPosition).get(horizontalPosition+2);
            Field rookField = fieldsList.get(verticalPosition).get(horizontalPosition+3);
            if (firstRightField.checkIfFieldIsUnderAttack(fieldsList, previousBoardState, this.getIsFigureWhite()).isEmpty() && firstRightField.getChildren().isEmpty()) {
                if (secondRightField.checkIfFieldIsUnderAttack(fieldsList, previousBoardState, this.getIsFigureWhite()).isEmpty() && secondRightField.getChildren().isEmpty()) {
                    if (!rookField.getChildren().isEmpty() && rookField.getFigure() instanceof Rook &&rookField.getFigure().getDidFigureNotMove()) {
                        availableFields.add(secondRightField);
                    }
                }
            }

            //  Long castling
            Field firstLeftField = fieldsList.get(verticalPosition).get(horizontalPosition-1);
            Field secondLeftField = fieldsList.get(verticalPosition).get(horizontalPosition-2);
            Field thirdLeftField = fieldsList.get(verticalPosition).get(horizontalPosition-3);
            rookField = fieldsList.get(verticalPosition).get(horizontalPosition-4);
            if (firstLeftField.checkIfFieldIsUnderAttack(fieldsList, previousBoardState, this.getIsFigureWhite()).isEmpty() && firstLeftField.getChildren().isEmpty()) {
                if (secondLeftField.checkIfFieldIsUnderAttack(fieldsList, previousBoardState, this.getIsFigureWhite()).isEmpty() && secondLeftField.getChildren().isEmpty()) {
                    if (thirdLeftField.checkIfFieldIsUnderAttack(fieldsList, previousBoardState, this.getIsFigureWhite()).isEmpty() && thirdLeftField.getChildren().isEmpty()) {
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