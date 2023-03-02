package org.pk.chessgame;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ChessBoard extends GridPane {
    private final ArrayList<ArrayList<Field>> currentStateFields = new ArrayList<>();
    private ArrayList<ArrayList<Field>> previousStateFields = new ArrayList<>();

    public ChessBoard(double width, double height) {
        this.setPrefSize(width, height);
        this.createBoardAndFields(width, height);
    }

    private void createBoardAndFields(double width, double height) {
        for (int row = 0; row < 8; row++) {
            ArrayList<Field> tempArrayListForFields = new ArrayList<>();
            ArrayList<Field> tempArrayListForPreviousStateFields = new ArrayList<>();
            for (int column = 0; column < 8; column++) {
                Field field = new Field(width, height, row, column);
                Field previousStateField = new Field(width, height, row, column);
                tempArrayListForFields.add(field);
                tempArrayListForPreviousStateFields.add(previousStateField);

                GridPane.setConstraints(field, column, row);
                this.getChildren().add(field);
            }
            this.currentStateFields.add(tempArrayListForFields);
            this.previousStateFields.add(tempArrayListForPreviousStateFields);
        }
    }

    public ArrayList<ArrayList<Field>> getFieldsList() {
        return currentStateFields;
    }

    public ArrayList<ArrayList<Field>> getPreviousState() {
        return previousStateFields;
    }
}
