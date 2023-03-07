package org.pk.chessgame;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.pk.chessgame.figures.*;

import java.util.ArrayList;

public class Figure extends AnchorPane implements Moves, Cloneable {
    protected final String figureType;
    protected boolean didFigureNotMove;
    protected final boolean isFigureWhite;
    protected Label figureLabel = new Label();

    public Figure(double width, double height, String figureType) {
        this.setPrefSize(width / 8, height / 8);
        this.setStyle("-fx-background-color: transparent");

        this.figureType = figureType;
        this.isFigureWhite = Character.isUpperCase(this.figureType.charAt(0));
        this.didFigureNotMove = true;

        this.figureLabel.setPrefSize(width / 8, height / 8);
        this.getChildren().add(this.figureLabel);
    }

    public static Figure createFigure(double width, double height, String figureType) {
        if ("Kk".contains(figureType)) return new King(width, height, figureType);
        if ("Qq".contains(figureType)) return new Queen(width, height, figureType);
        if ("Nn".contains(figureType)) return new Knight(width, height, figureType);
        if ("Bb".contains(figureType)) return new Bishop(width, height, figureType);
        if ("Rr".contains(figureType)) return new Rook(width, height, figureType);
        return new Pawn(width, height, figureType);
    }

    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> currentStateFields, ArrayList<ArrayList<Field>> previousStateFields, int verticalPosition, int horizontalPosition) {
        return null;
    }

    public void setDidFigureMove() {
        //  If a chess piece has been moved, the variable didFigureNotMove is set permanently to false
        this.didFigureNotMove = false;
    }

    public boolean getDidFigureNotMove() {
        return this.didFigureNotMove;
    }

    public boolean getIsFigureWhite() {
        return this.isFigureWhite;
    }

    public String getFigureType() {
        return this.figureType;
    }

}

