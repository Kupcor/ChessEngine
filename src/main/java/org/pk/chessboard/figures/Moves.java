package org.pk.chessboard.figures;

import org.pk.chessboard.Field;

import java.util.ArrayList;

public interface Moves {
    //  Function return a list of field that are available for specific figure to move. Returned list is different
    //  between specific figure, but can contain empty fields (field that has no figure on itself) and occupy fields.
    //  Occupy fields can be split to fields occupy by enemy and friendly figure (black and white figures).
    //  Occupy preventing figure for moving to field that is occupied by friendly figure has to be implemented separately, due
    //  to simplify mechanism detecting if enemy king can capture specific figure.
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition);
}
