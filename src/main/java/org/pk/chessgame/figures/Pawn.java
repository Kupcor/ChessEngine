package org.pk.chessgame.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessgame.Field;
import org.pk.chessgame.Figure;
import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Figure implements Moves {

    public Pawn(double width, double height, String figureType) {
        super(width, height, figureType);
        String pawnImage = figureType.equals("P") ? "WPawn.png" : "BPawn.png";
        this.figureLabel.setGraphic(new ImageView(new Image(Objects.requireNonNull(Field.class.getResource("assets/" + pawnImage)).toString())));
    }


    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> currentStateFields, ArrayList<ArrayList<Field>> previousStateFields, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();
        Field sourceField = currentStateFields.get(verticalPosition).get(horizontalPosition);
        int increment;
        if (this.figureType.equals("P")) increment = -1;
        else increment = 1;
        Field targetField;

        //  Check if Pawn can capture opponent figure
        for (int offset : new int[]{1, -1}) {
            int targetHorizontalPosition = horizontalPosition + offset;
            if (targetHorizontalPosition >= 0 && targetHorizontalPosition < 8) {
                targetField = currentStateFields.get(verticalPosition + increment).get(targetHorizontalPosition);
                if (!targetField.getChildren().isEmpty()) filteredFields.add(targetField);
            }
        }

        //  Standard moves
        targetField = currentStateFields.get(verticalPosition + increment).get(horizontalPosition);
        if (targetField.getChildren().isEmpty()) {
            filteredFields.add(targetField);
            //  First pawn move

            if ((this.figureType.equals("P") && verticalPosition == 6) || (this.figureType.equals("p") && verticalPosition == 1)) {
                int doubleIncrement = increment * 2;
                Field doubleMoveTargetField = currentStateFields.get(verticalPosition + doubleIncrement).get(horizontalPosition);
                if (doubleMoveTargetField.getChildren().isEmpty()) filteredFields.add(doubleMoveTargetField);
            }
        }

        //  Beating in transit - should be split into private functions in future
        if ((this.figureType.equals("P") && verticalPosition == 3) || (this.figureType.equals("p") && verticalPosition == 4)) {
            if (horizontalPosition - 1 >= 0) {
                Field firstField = previousStateFields.get(verticalPosition).get(horizontalPosition - 1);
                Field secondField = previousStateFields.get(verticalPosition + increment).get(horizontalPosition - 1);
                Field thirdField = currentStateFields.get(verticalPosition).get(horizontalPosition - 1);
                if (firstField.getChildren().isEmpty() && secondField.getChildren().isEmpty()) {
                    if(!thirdField.getChildren().isEmpty() && "Pp".contains(thirdField.getFigure().getFigureType())) {
                        if (sourceField.getFigure().getIsFigureWhite() != thirdField.getFigure().getIsFigureWhite()) {
                            filteredFields.add(currentStateFields.get(verticalPosition + increment).get(horizontalPosition - 1));
                        }
                    }
                }
            }

            if (horizontalPosition + 1 < 8) {
                Field firstField = previousStateFields.get(verticalPosition).get(horizontalPosition + 1);
                Field secondField = previousStateFields.get(verticalPosition + increment).get(horizontalPosition + 1);
                Field thirdField = currentStateFields.get(verticalPosition).get(horizontalPosition + 1);
                if (firstField.getChildren().isEmpty() && secondField.getChildren().isEmpty()) {
                    if(!thirdField.getChildren().isEmpty() && "Pp".contains(thirdField.getFigure().getFigureType())) {
                        if (sourceField.getFigure().getIsFigureWhite() != thirdField.getFigure().getIsFigureWhite()) {
                            filteredFields.add(currentStateFields.get(verticalPosition + increment).get(horizontalPosition + 1));
                        }
                    }
                }
            }
        }
        return filteredFields;
    }
}