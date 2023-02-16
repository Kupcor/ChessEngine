package org.pk.chessboard.figures;

import org.pk.chessboard.Field;

import java.util.ArrayList;

public interface Moves {
    public ArrayList<Field> setMoveRestrictions(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition);
}
