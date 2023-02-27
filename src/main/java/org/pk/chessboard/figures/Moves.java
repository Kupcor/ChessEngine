package org.pk.chessboard.figures;

import org.pk.chessboard.Field;
import java.util.ArrayList;

public interface Moves {
    //  Function return a list of field that are available for specific figure to move. Returned list is different
    //  between specific figure, but can contain empty fields (field that has no figure on itself) and occupy fields.
    //  Occupy fields can be split to fields occupy by enemy and friendly figure (black and white figures).
    //  To prevent a figure from moving to a field occupied by a friendly figure, an occupying mechanism must be implemented separately.
    //  This is necessary to simplify the process of detecting whether an enemy king can capture a specific figure.
    ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> fieldsList, ArrayList<ArrayList<Field>> previousBoardState, int verticalPosition, int horizontalPosition);
}
