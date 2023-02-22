package org.pk.chessboard;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.pk.chessboard.figures.*;

import java.util.ArrayList;


public class Figure extends AnchorPane implements Moves {
    protected String figureType;
    protected boolean DidFigureNotMove;
    protected boolean isFigureWhite;

    protected Label figureLabel = new Label();

    public Figure(double width, double height, String figureType) {
        this.setPrefSize(width / 8, height / 8);
        this.setStyle("-fx-background-color: transparent");

        this.figureType = figureType;
        this.isFigureWhite = Character.isUpperCase(this.figureType.charAt(0));
        this.DidFigureNotMove = true;

        this.figureLabel.setPrefSize(width / 8, height / 8);
        this.getChildren().add(this.figureLabel);
    }

    public final Figure createFigure(double width, double height, String figureType) {
        if ("Kk".contains(figureType)) return new King(width, height, figureType);
        if ("Qq".contains(figureType)) return new Queen(width, height, figureType);
        if ("Nn".contains(figureType)) return new Knight(width, height, figureType);
        if ("Bb".contains(figureType)) return new Bishop(width, height, figureType);
        if ("Rr".contains(figureType)) return new Rook(width, height, figureType);
        return new Pawn(width, height, figureType);
    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition) {
        return null;
    }

    public boolean getDidFigureNotMove() {
        return this.DidFigureNotMove;
    }

    public void setDidFigureMove() {
        this.DidFigureNotMove = false;
    }

    public boolean getIsFigureWhite() {
        return this.isFigureWhite;
    }

    public String getFigureType() {
        return this.figureType;
    }
}

