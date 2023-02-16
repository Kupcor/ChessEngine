package org.pk.chessboard.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessboard.Field;
import org.pk.chessboard.Figure;

import java.util.ArrayList;

public class Pawn extends Figure implements Moves {

    public Pawn(double width, double height, String figureType) {
        super(width, height, figureType);
        if (figureType.equals("P")) {
            this.figure.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/WPawn.png")))));
        }
        else {
            this.figure.setGraphic(new ImageView(new Image(String.valueOf(Field.class.getResource("assets/BPawn.png")))));
        }
    }

    @Override
    public ArrayList<Field> setMoveRestrictions(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();
        Field sourceField = fieldsList.get(verticalPosition).get(horizontalPosition);
        int increment;
        if (this.figureType.equals("P")) increment = -1;
        else increment = 1;
        Field targetField;

        //  Check if Pawn can capture opponent figure
        for (int offset : new int[]{1, -1}) {
            int targetHorizontalPosition = horizontalPosition + offset;
            if (targetHorizontalPosition >= 0 && targetHorizontalPosition < 8) {
                targetField = fieldsList.get(verticalPosition + increment).get(targetHorizontalPosition);
                if (!targetField.getChildren().isEmpty() && targetField.getFigure().getIsFigureWhite() != sourceField.getFigure().getIsFigureWhite()) {
                    filteredFields.add(targetField);
                }
            }
        }

        //  Standard moves
        targetField = fieldsList.get(verticalPosition + increment).get(horizontalPosition);
        if (targetField.getChildren().isEmpty()) {
            filteredFields.add(targetField);
            //  First pawn move

            if ((this.figureType.equals("P") && verticalPosition == 6) || (this.figureType.equals("p") && verticalPosition == 1)) {
                int doubleIncrement = increment * 2;
                Field doubleMoveTargetField = fieldsList.get(verticalPosition + doubleIncrement).get(horizontalPosition);
                if (doubleMoveTargetField.getChildren().isEmpty()) filteredFields.add(doubleMoveTargetField);
            }
        }

        //  Beating in transit - should be split into private functions in future
        if ((this.figureType.equals("P") && verticalPosition == 3) || (this.figureType.equals("p") && verticalPosition == 4)) {
            if (horizontalPosition - 1 > 0) {
                Field firstField = previousBoardState.get(verticalPosition).get(horizontalPosition - 1);
                Field secondField = previousBoardState.get(verticalPosition + increment).get(horizontalPosition - 1);
                Field thirdField = fieldsList.get(verticalPosition).get(horizontalPosition - 1);
                if (firstField.getChildren().isEmpty() && secondField.getChildren().isEmpty()) {
                    if(!thirdField.getChildren().isEmpty() && "Pp".contains(thirdField.getFigure().getFigureType())) {
                        if (sourceField.getFigure().getIsFigureWhite() != thirdField.getFigure().getIsFigureWhite()) {
                            filteredFields.add(fieldsList.get(verticalPosition + increment).get(horizontalPosition - 1));
                        }
                    }
                }
            }

            if (horizontalPosition + 1 < 8) {
                Field firstField = previousBoardState.get(verticalPosition).get(horizontalPosition + 1);
                Field secondField = previousBoardState.get(verticalPosition + increment).get(horizontalPosition + 1);
                Field thirdField = fieldsList.get(verticalPosition).get(horizontalPosition + 1);
                if (firstField.getChildren().isEmpty() && secondField.getChildren().isEmpty()) {
                    if(!thirdField.getChildren().isEmpty() && "Pp".contains(thirdField.getFigure().getFigureType())) {
                        if (sourceField.getFigure().getIsFigureWhite() != thirdField.getFigure().getIsFigureWhite()) {
                            filteredFields.add(fieldsList.get(verticalPosition + increment).get(horizontalPosition + 1));
                        }
                    }
                }
            }
        }
        return filteredFields;
    }
}