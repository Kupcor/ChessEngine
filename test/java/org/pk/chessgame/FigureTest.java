package org.pk.chessgame;

import javafx.application.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pk.chessgame.figures.*;

import java.util.ArrayList;

public class FigureTest {

    @BeforeAll
    static  void setUp() {
        //  Need to initialize JFX environment in order to test Objects/Classes that use JFX components
        //  In this case figure objects use JFX Label component
        Platform.startup(() -> {});
    }

    @Test
    void testFigureConstructor() {
        Figure whiteTestFigure = new Figure(48, 48, "Q");
        Figure blackTestFigure = new Figure(48, 48, "k");
        Assertions.assertTrue(whiteTestFigure.getIsFigureWhite());
        Assertions.assertFalse(blackTestFigure.getIsFigureWhite());

        Assertions.assertEquals("Q", whiteTestFigure.getFigureType());
        Assertions.assertEquals("k", blackTestFigure.getFigureType());

        Assertions.assertTrue(whiteTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(blackTestFigure.getDidFigureNotMove());

        Assertions.assertEquals(1, whiteTestFigure.getChildren().size());
        Assertions.assertEquals(1, blackTestFigure.getChildren().size());

        Assertions.assertFalse(whiteTestFigure.getChildren().isEmpty());
        Assertions.assertFalse(blackTestFigure.getChildren().isEmpty());
    }

    @Test
    void testCreateFigure() {
        Assertions.assertTrue(Figure.createFigure(48,48, "K") instanceof King);
        Assertions.assertTrue(Figure.createFigure(48,48, "k") instanceof King);
        Assertions.assertTrue(Figure.createFigure(48,48, "Q") instanceof Queen);
        Assertions.assertTrue(Figure.createFigure(48,48, "q") instanceof Queen);
        Assertions.assertTrue(Figure.createFigure(48,48, "B") instanceof Bishop);
        Assertions.assertTrue(Figure.createFigure(48,48, "b") instanceof Bishop);
        Assertions.assertTrue(Figure.createFigure(48,48, "R") instanceof Rook);
        Assertions.assertTrue(Figure.createFigure(48,48, "r") instanceof Rook);
        Assertions.assertTrue(Figure.createFigure(48,48, "N") instanceof Knight);
        Assertions.assertTrue(Figure.createFigure(48,48, "n") instanceof Knight);
        Assertions.assertTrue(Figure.createFigure(48,48, "P") instanceof Pawn);
        Assertions.assertTrue(Figure.createFigure(48,48, "p") instanceof Pawn);
    }

    @Test
    void testSetDidFigureMove() {
        Figure testFigure = Figure.createFigure(48,48,"Q");
        Assertions.assertTrue(testFigure.getDidFigureNotMove());
        testFigure.setDidFigureMove();
        Assertions.assertFalse(testFigure.getDidFigureNotMove());
    }

    @Test
    void testSetDidFigureMoveFunctionOnChessBoard() {
        ChessBoard chessBoard = new ChessBoard(400, 400);   //  Chess board is source of currentFieldState
        ArrayList<ArrayList<Field>> currentFieldsList = chessBoard.getFieldsList();
        ChessGame chessGame = new ChessGame(400, 400, currentFieldsList);
        Figure firstTestFigure = currentFieldsList.get(1).get(4).getFigure();   //  Black pawn
        Figure secondTestFigure = currentFieldsList.get(7).get(4).getFigure();  //  White king
        Figure thirdTestFigure = currentFieldsList.get(7).get(7).getFigure();   //  White, right rook

        Assertions.assertTrue(firstTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(secondTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(thirdTestFigure.getDidFigureNotMove());

        //  first white move
        chessGame.selectFigureToMove(6, 4);
        chessGame.selectFigureDestination(4, 4);
        Assertions.assertTrue(firstTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(secondTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(thirdTestFigure.getDidFigureNotMove());

        //  first black move - firstTestFigure move
        chessGame.selectFigureToMove(1, 4);
        chessGame.selectFigureDestination(3, 4);
        Assertions.assertFalse(firstTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(secondTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(thirdTestFigure.getDidFigureNotMove());

        //  castling
        //  second white move - white field bishop
        chessGame.selectFigureToMove(7, 5);
        chessGame.selectFigureDestination(6, 4);
        //  second black move - any move
        chessGame.selectFigureToMove(1, 1);
        chessGame.selectFigureDestination(3, 1);
        //  third white move - right knight
        chessGame.selectFigureToMove(7, 6);
        chessGame.selectFigureDestination(5, 5);
        //  third black move - any move
        chessGame.selectFigureToMove(1, 2);
        chessGame.selectFigureDestination(3, 2);
        Assertions.assertFalse(firstTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(secondTestFigure.getDidFigureNotMove());
        Assertions.assertTrue(thirdTestFigure.getDidFigureNotMove());
        //  Now castling
        chessGame.selectFigureToMove(7, 4);
        chessGame.selectFigureDestination(7, 6);
        Assertions.assertFalse(firstTestFigure.getDidFigureNotMove());
        Assertions.assertFalse(secondTestFigure.getDidFigureNotMove());
        Assertions.assertFalse(thirdTestFigure.getDidFigureNotMove());
    }

    @Test
    public void testGetFigureType() {
        Figure pawn = new Figure(800, 800, "P");
        Figure queen = new Figure(800, 800, "Q");
        Assertions.assertEquals("P", pawn.getFigureType(), "The figure should be a pawn");
        Assertions.assertEquals("Q", queen.getFigureType(), "The figure should be a queen");
    }

    @Test
    public void testGetIsFigureWhite() {
        Figure whiteFigure = new Figure(800, 800, "P");
        Figure blackFigure = new Figure(800, 800, "p");
        Assertions.assertTrue(whiteFigure.getIsFigureWhite(), "The figure should be white");
        Assertions.assertFalse(blackFigure.getIsFigureWhite(), "The figure should be black");
    }

    @Test
    public void testGetAvailableMoves() {
        Figure king = new Figure(800, 800, "K");
        ArrayList<ArrayList<Field>> currentStateFields = new ArrayList<>();
        ArrayList<ArrayList<Field>> previousStateFields = new ArrayList<>();
        int verticalPosition = 3;
        int horizontalPosition = 3;
        ArrayList<Field> availableMoves = king.getAvailableMoves(currentStateFields, previousStateFields, verticalPosition, horizontalPosition);
        Assertions.assertNull(availableMoves);
    }
}
