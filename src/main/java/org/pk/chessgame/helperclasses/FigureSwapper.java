package org.pk.chessgame.helperclasses;

import org.pk.chessgame.Field;
import org.pk.chessgame.Figure;

public class FigureSwapper {

    public static void swapFigures(Field sourceField, Field targetField) {
        Figure tempFigure = sourceField.getAndRemoveFigure();
        targetField.setFigure(tempFigure);
    }
}
