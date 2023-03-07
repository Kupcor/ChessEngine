package org.pk;

import javafx.scene.layout.BorderPane;
import org.pk.chessgame.ChessBoard;
import org.pk.chessgame.ChessGame;

public class ChessGameUI extends BorderPane {
    private final ChessGame chessGame;

    public ChessGameUI(int width, int height) {
        this.setPrefSize(width, height);
        ChessBoard chessBoard = new ChessBoard(width, height);
        this.setCenter(chessBoard);

        this.chessGame = new ChessGame(width, height, chessBoard.getFieldsList());

        chessBoard.setOnMousePressed(mouseEvent -> {
            int horPos = (int)  mouseEvent.getX() / (width / 8);
            int verPos = (int) mouseEvent.getY() / (height / 8);
            this.chessGame.selectFigureToMove(verPos, horPos);
        });

        chessBoard.setOnMouseReleased(mouseEvent -> {
            int horPos = (int)  mouseEvent.getX() / (width / 8);
            int verPos = (int) mouseEvent.getY() / (height / 8);
            this.chessGame.selectFigureDestination(verPos, horPos);
        });

        //  For scenario tests - temporary | to remove
        chessBoard.setOnMouseClicked(mouseEvent->{
            int horPos = (int)  mouseEvent.getX() / (width / 8);
            int verPos = (int) mouseEvent.getY() / (height / 8);
            //System.out.println(chessBoard.getFieldsList().get(verPos).get(horPos).getFigure().getDidFigureNotMove());
        });
    }
}
