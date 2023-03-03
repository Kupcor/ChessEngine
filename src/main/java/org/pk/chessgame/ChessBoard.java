package org.pk.chessgame;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ChessBoard extends GridPane {
    private final ArrayList<ArrayList<Field>> currentStateFields = new ArrayList<>();

    public ChessBoard(double width, double height) {
        this.setPrefSize(width, height);
        this.createBoardAndFields(width, height);
    }

    private void createBoardAndFields(double width, double height) {
        for (int row = 0; row < 8; row++) {
            ArrayList<Field> tempArrayListForFields = new ArrayList<>();
            for (int column = 0; column < 8; column++) {
                Field field = new Field(width, height, row, column);
                tempArrayListForFields.add(field);

                GridPane.setConstraints(field, column, row);
                this.getChildren().add(field);
            }
            this.currentStateFields.add(tempArrayListForFields);
        }
    }

    public ArrayList<ArrayList<Field>> getFieldsList() {
        return currentStateFields;
    }
}
