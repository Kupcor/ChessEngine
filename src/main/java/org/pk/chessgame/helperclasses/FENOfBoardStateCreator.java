package org.pk.chessgame.helperclasses;

import org.pk.chessgame.Field;

import java.util.ArrayList;

public class FENOfBoardStateCreator {

    public static String createFENOfBoardState(ArrayList<ArrayList<Field>> currentFields) {
        StringBuilder FEN = new StringBuilder();
        for (ArrayList<Field> list : currentFields) {
            int numberOfFreeFields = 0;
            for (Field currentField : list) {
                if (currentField.getChildren().isEmpty()) numberOfFreeFields++;
                else {
                    appendFields(FEN, numberOfFreeFields, currentField.getFigure().getFigureType());
                    numberOfFreeFields = 0;
                }
            }
            appendFields(FEN, numberOfFreeFields);
            if (list != currentFields.get(currentFields.size() - 1)) FEN.append("/");
        }
        return FEN.toString();
    }

    //  Helper method for createFenOfBoardState
    private static void appendFields(StringBuilder FEN, int numberOfFreeFields, String figureType) {
        if (numberOfFreeFields > 0) {
            FEN.append(numberOfFreeFields);
        }
        FEN.append(figureType);
    }

    //  Overloaded helper method for createFenOfBoardState
    private static void appendFields(StringBuilder FEN, int numberOfFreeFields) {
        appendFields(FEN, numberOfFreeFields, "");
    }

}
