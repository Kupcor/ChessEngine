package org.pk.chessgame.figures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pk.chessgame.ChessBoard;
import org.pk.chessgame.ChessGame;
import org.pk.chessgame.Field;
import org.pk.chessgame.Figure;

import java.util.ArrayList;

public class QueenTest {
    private Queen testWhiteQueen;
    private Queen testBlackQueen;

    @Test
    void testQueenConstructor() {
        Assertions.assertTrue(testWhiteQueen.getIsFigureWhite());
        Assertions.assertFalse(testBlackQueen.getIsFigureWhite());

        Assertions.assertEquals("Q", testWhiteQueen.getFigureType());
        Assertions.assertEquals("q", testBlackQueen.getFigureType());

        Assertions.assertTrue(testWhiteQueen.getDidFigureNotMove());
        Assertions.assertTrue(testBlackQueen.getDidFigureNotMove());

        Assertions.assertEquals(1, testWhiteQueen.getChildren().size());
        Assertions.assertEquals(1, testBlackQueen.getChildren().size());

        Assertions.assertFalse(testWhiteQueen.getChildren().isEmpty());
        Assertions.assertFalse(testBlackQueen.getChildren().isEmpty());
    }


    //  To test moves
    //  Przypadki testowe
    //  Nie może wykonać ruchu
    //  Jest związana z królem? - tego nie implementujemy
    //  Może najechać na swoją figurę
    //  Może zbić czyjąś figure
    @Test
    void testIfQueenAvailableMovesAreCorrect() {
        ChessBoard chessBoard = new ChessBoard(400, 400);   //  Chess board is source of currentFieldState
        ChessGame chessGame = new ChessGame(400, 400, chessBoard.getFieldsList());

    }

    private void findQueenPosition(Figure figure, ArrayList<ArrayList<Field>> currentFieldsList) {

    }
}
