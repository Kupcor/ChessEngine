package org.pk.chessboard;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.pk.chessboard.figures.Queen;


public class Field extends AnchorPane {
    private Figure currentFigure;
    private final String color;
    private final int verticalPosition;
    private final int horizontalPosition;
    private final double width;
    private final double height;

    public Field(double width, double height, String color, int verticalPosition, int horizontalPosition) {
        this.color = color;
        this.verticalPosition = verticalPosition;
        this.horizontalPosition = horizontalPosition;
        this.width = width;
        this.height = height;
        this.setPrefSize(width / 8, height / 8);
        this.setBackground(new Background(new BackgroundFill(Color.valueOf(this.color), new CornerRadii(0), new Insets(0))));
    }

    //  Change figure mechanism contains two public functions that are call from ChessBoard class events
    //  Function set figure change current figure on the field
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

    //  Typical getters
    public String getColor() {return this.color;}

    public Figure getFigure() {return this.currentFigure;}

    public int getVerticalPosition() {return this.verticalPosition;}

    public int getHorizontalPosition() {return this.horizontalPosition;}
}
