package org.pk.chessgame;

import javafx.application.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

        Assertions.assertEquals(1, whiteTestFigure.getChildren().sorted().size());
        Assertions.assertEquals(1, blackTestFigure.getChildren().sorted().size());
    }

    @Test
    void testCreateFigure() {
        Figure whiteTestFigure = new Figure(48, 48, "figureConstructor");
    }

    @Test
    void testSetDidFigureMove() {

    }

    @Test
    void testGetAvailableMoves() {

    }
}
