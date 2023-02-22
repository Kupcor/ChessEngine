package org.pk.chessboard;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.pk.chessboard.figures.King;
import org.pk.chessboard.figures.Pawn;
import org.pk.chessboard.figures.Queen;

import java.util.ArrayList;

public class Field extends AnchorPane {
    private Figure currentFigure = null;
    private final String color;
    private final int verticalPosition;
    private final int horizontalPosition;
    private final double width;
    private final double height;

    public Field(double width, double height, int verticalPosition, int horizontalPosition) {
        this.verticalPosition = verticalPosition;
        this.horizontalPosition = horizontalPosition;
        this.width = width;
        this.height = height;

        if ((horizontalPosition + verticalPosition) % 2 != 0) this.color = "#613d2e";
        else this.color = "#a89187";

        this.setPrefSize(width / 8, height / 8);
        this.setBackground(new Background(new BackgroundFill(Color.valueOf(this.color), new CornerRadii(0), new Insets(0))));
    }

    //  Change figure mechanism contains two public functions that are call from ChessBoard class events
    //  Function set figure change current figure on the field
    //  Here can be added code to filter that figure can't beat the other figure with same color - it probably little increase calculation
    public void setFigure(Figure figure) {
        this.getChildren().removeAll(this.getChildren());
        this.currentFigure = chooseNewFigure(figure);
        this.getChildren().add(this.currentFigure);
    }

    //  Function get figure return current figure in the field and remove it from the field
    public Figure getAndRemoveFigure() {
        this.getChildren().remove(this.currentFigure);
        Figure tempFigure = this.currentFigure;
        this.currentFigure = null;
        return tempFigure;
    }

    //  Promote pawn to queen - temp solution
    private Figure chooseNewFigure(Figure figure) {
        if ((this.verticalPosition == 0 || this.verticalPosition == 7) && "Pp".contains(figure.getFigureType())) {
            return new Queen(this.width, this.height, figure.getIsFigureWhite() ? "Q" : "q");
        }
        return figure;
    }

    //  Visualization functions - functions that change visual field appearance
    //  They are also called from the ChessBoard class events
    public void changeFieldColor(String color) {
        this.setBackground(new Background(new BackgroundFill(Color.valueOf(color), new CornerRadii(0), new Insets(0))));
    }

    public void setOriginFieldColor() {
        this.setBackground(new Background(new BackgroundFill(Color.valueOf(this.color), new CornerRadii(0), new Insets(0))));
    }

    public ArrayList<Field> checkIfFieldIsUnderAttack(ArrayList<ArrayList<Field>> fieldList, ArrayList<ArrayList<Field>> previousState , boolean whiteMove) {
        ArrayList<Field> allFieldsThatAttackMainField = new ArrayList<>();

        ArrayList<Field> opponentPieces = new ArrayList<>();
        for (ArrayList<Field> list : fieldList) {
            for (Field field : list) {
                if (!field.getChildren().isEmpty() && field.getFigure().getIsFigureWhite() != whiteMove) {
                    opponentPieces.add(field);
                }
            }
        }
        //  So yea there is only two exceptions - king and pawn.
        for (Field field : opponentPieces) {
            ArrayList<Field> figureAvailableMoves = new ArrayList<>();
            //  King exception
            if (field.getFigure() instanceof King) {
                figureAvailableMoves.addAll(((King) field.getFigure()).getFieldsAroundKing(fieldList, field.getVerticalPosition(), field.getHorizontalPosition()));
            }
            //  Pawn exception
            else if (field.getFigure() instanceof Pawn) {
                int increment;
                if (field.getFigure().getIsFigureWhite()) increment = -1;
                else increment = 1;
                if (field.getHorizontalPosition() + 1 < 8) figureAvailableMoves.add(fieldList.get(field.getVerticalPosition() + increment).get(field.getHorizontalPosition() + 1));
                if (field.getHorizontalPosition() - 1 > -1) figureAvailableMoves.add(fieldList.get(field.getVerticalPosition() + increment).get(field.getHorizontalPosition() - 1));
            } else figureAvailableMoves = field.getFigure().getAvailableMoves(fieldList, previousState, field.getVerticalPosition(), field.getHorizontalPosition());

            if (figureAvailableMoves.contains(this)) {
                allFieldsThatAttackMainField.add(field);
            }
        }
        return allFieldsThatAttackMainField;
    }

    //  Getters
    public String getColor() {return this.color;}

    public Figure getFigure() {return this.currentFigure;}

    public int getVerticalPosition() {return this.verticalPosition;}

    public int getHorizontalPosition() {return this.horizontalPosition;}
}
