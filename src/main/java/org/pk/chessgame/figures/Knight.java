package org.pk.chessgame.figures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pk.chessgame.Field;
import org.pk.chessgame.Figure;

import java.util.ArrayList;
import java.util.Objects;

public class Knight extends Figure implements Moves {

    public Knight(double width, double height, String figureType) {
        super(width, height, figureType);
        String pawnImage = figureType.equals("N") ? "WKnight.png" : "BKnight.png";
        this.figureLabel.setGraphic(new ImageView(new Image(Objects.requireNonNull(Field.class.getResource("assets/" + pawnImage)).toString())));
    }

    @Override
    public ArrayList<Field> getAvailableMoves(ArrayList<ArrayList<Field>> currentStateFields, ArrayList<ArrayList<Field>> previousStateFields, int verticalPosition, int horizontalPosition) {
        ArrayList<Field> filteredFields = new ArrayList<>();

        int[][] directions = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] direction : directions) {
            int verticalDirection = verticalPosition + direction[0];
            int horizontalDirection = horizontalPosition + direction[1];

            if (verticalDirection < 0 || verticalDirection > 7 || horizontalDirection < 0 || horizontalDirection > 7) {
                continue;
            }

            Field targetField = currentStateFields.get(verticalDirection).get(horizontalDirection);
            filteredFields.add(targetField);

        }

        return filteredFields;
    }
}