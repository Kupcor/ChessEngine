package org.pk;

import javafx.scene.layout.BorderPane;
import org.pk.chessgame.ChessBoard;
import org.pk.chessgame.ChessGame;

public class ChessGameUI extends BorderPane {


    private final ChessBoard chessBoard;
    private final ChessGame chessGame;

    public ChessGameUI(int width, int height) {
        this.setPrefSize(width, height);

        this.chessGame = new ChessGame(width, height);

        this.chessBoard = new ChessBoard(width, height);
        this.setCenter(this.chessBoard);

        this.chessBoard.setOnMousePressed(event -> {

        });

        this.chessBoard.setOnMouseReleased(event -> {

        });
    }
}
