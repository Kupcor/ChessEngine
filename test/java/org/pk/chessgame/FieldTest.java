package org.pk.chessgame;

import javafx.application.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FieldTest {

    @BeforeAll
    static  void setUp() {
        //  Need to initialize JFX environment in order to test Objects/Classes that use JFX components
        //  In this case figure objects use JFX Label class
        Platform.startup(() -> {});
    }

    @Test
    void testFieldConstructor() {
        Field testWhiteField = new Field(48, 48,  0, 0);
        Field testBlackField = new Field(48,48,7, 0);

        Assertions.assertEquals("#a89187", testWhiteField.getBackgroundColor());
        Assertions.assertNotEquals(testWhiteField.getBackgroundColor(), "#613d2e");

        Assertions.assertEquals("#613d2e", testBlackField.getBackgroundColor());
        Assertions.assertNotEquals("#a89187", testBlackField.getBackgroundColor());

        Assertions.assertEquals(7, testBlackField.getVerticalPosition());
        Assertions.assertEquals(0, testBlackField.getHorizontalPosition());

        Assertions.assertEquals(0, testWhiteField.getVerticalPosition());
        Assertions.assertEquals(0, testWhiteField.getHorizontalPosition());

        //  Field do not contain any figures when it is initialized
        Assertions.assertTrue(testBlackField.getChildren().isEmpty());
        Assertions.assertTrue(testWhiteField.getChildren().isEmpty());

        Assertions.assertNull(testWhiteField.getFigure());
        Assertions.assertNull(testBlackField.getFigure());
    }

    @Test
    void testSetFigure() {

    }
}
