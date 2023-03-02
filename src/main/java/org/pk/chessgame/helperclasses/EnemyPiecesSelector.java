package org.pk.chessgame.helperclasses;

import org.pk.chessgame.Field;

import java.util.ArrayList;

public class EnemyPiecesSelector {

    public static ArrayList<Field> getOpponentPieces (boolean isWhiteTurn, ArrayList<ArrayList<Field>> currentFieldList) {
        ArrayList<Field> opponentPieces = new ArrayList<>();
        for (ArrayList<Field> list : currentFieldList) {
            for (Field field : list) {
                if (!field.getChildren().isEmpty() && field.getFigure().getIsFigureWhite() != isWhiteTurn) {
                    opponentPieces.add(field);
                }
            }
        }
        return opponentPieces;
    }
}
