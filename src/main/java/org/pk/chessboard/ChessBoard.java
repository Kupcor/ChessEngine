package org.pk.chessboard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class ChessBoard extends BorderPane {
    private ArrayList<ArrayList<Field>> fieldsList = new ArrayList<>();
    private ArrayList<ArrayList<Field>> previousState;
    private ArrayList<Field> figureAllowedMoves = new ArrayList<>();

    private final BorderPane gameArea = new BorderPane();
    private final GridPane mainChessBoard = new GridPane();
    private final Figure figure;

    //  Figure objects to handle drag-and-drop functionalities
    private Field targetField;
    private Field sourceField;

    //  Functionality
    private boolean whiteTurn = true;

    public ChessBoard(int width, int height) {
        this.setPrefSize(width, height);
        this.figure = new Figure(width, height, "Figure creator");

        this.createBoard(width, height);
        this.setFiguresPositionOnBoard(width, height, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        this.setCenter(this.gameArea);

        //  Moving figures logic / mechanism
        this.setOnMouseReleased(mouseEvent -> {
            int horizontalField = (int) mouseEvent.getX() / (width / 8);
            int verticalField = (int) mouseEvent.getY() / (height / 8);

            for (Field field : figureAllowedMoves) {
                field.setOriginFieldColor();
            }

            if (sourceField != null) {
                targetField = fieldsList.get(verticalField).get(horizontalField);
                if (figureAllowedMoves.contains(targetField)) {
                    if (!sourceField.getChildren().isEmpty() && targetField != sourceField) {
                        this.setPreviousState();                         //  Update previous state
                        targetField.setFigure(sourceField.getAndRemoveFigure());
                        targetField.changeFieldColor("#ab7846");
                        this.whiteTurn = !this.whiteTurn;
                    }
                }
            }
        });

        this.setOnMousePressed(mouseEvent -> {
            int horizontalField = (int) mouseEvent.getX() / (width / 8);
            int verticalField = (int) mouseEvent.getY() / (height / 8);

            if (sourceField != null) sourceField.setOriginFieldColor();
            if (targetField != null) targetField.setOriginFieldColor();

            if (!fieldsList.get(verticalField).get(horizontalField).getChildren().isEmpty()) {
                if (fieldsList.get(verticalField).get(horizontalField).getFigure().isFigureWhite == this.whiteTurn) {
                    sourceField = fieldsList.get(verticalField).get(horizontalField);
                    sourceField.changeFieldColor("#cf9a65");
                    figureAllowedMoves = sourceField.getFigure().setMoveRestrictions(fieldsList, previousState, verticalField, horizontalField);
                    for (Field field : figureAllowedMoves) {
                        field.changeFieldColor("ffffff");
                    }
                }
            }
        });

        this.simpleSimulation();
        //this.setOnMouseClicked(mouseEvent -> {
            //System.out.println(this.createFENOfActualState(this.fieldsList));
        //});
    }

    //  To delete - simple total random simulation
    public void simpleSimulation() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    System.out.println("H");
                    ArrayList<Field> fields = new ArrayList<>();
                    for (ArrayList<Field> list : fieldsList) {
                        for (Field field : list) {
                            if (field.getChildren().isEmpty()) continue;
                            if (field.getFigure().getIsFigureWhite() != whiteTurn) continue;
                            fields.add(field);
                        }
                    }
                    Random random = new Random();
                    Field sourceField = fields.get(random.nextInt(fields.size()));
                    ArrayList<Field> allowedMoves = sourceField.getFigure().setMoveRestrictions(fieldsList, previousState, sourceField.getVerticalPosition(), sourceField.getHorizontalPosition());
                    while (allowedMoves.size() < 1) {
                        sourceField = fields.get(random.nextInt(fields.size()));
                        allowedMoves = sourceField.getFigure().setMoveRestrictions(fieldsList, previousState, sourceField.getVerticalPosition(), sourceField.getHorizontalPosition());
                    }
                    int sizeAllowedMoves = allowedMoves.size();
                    Field targetField = allowedMoves.get(random.nextInt(sizeAllowedMoves));
                    targetField.setFigure(sourceField.getAndRemoveFigure());
                    whiteTurn = !whiteTurn;
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createBoard(double width, double height) {
        this.mainChessBoard.setPrefSize(width, height);
        for (int verticalField = 0; verticalField < 8; verticalField++) {
            ArrayList<Field> tempArrayList = new ArrayList<>();
            for (int horizontalField = 0; horizontalField < 8; horizontalField++) {
                String color;
                if ((horizontalField + verticalField) % 2 != 0) color = "#613d2e";
                else color = "#a89187";

                Field field = new Field(width, height, color, verticalField, horizontalField);
                tempArrayList.add(field);

                GridPane.setConstraints(field, horizontalField, verticalField);
                this.mainChessBoard.getChildren().add(field);
            }
            this.fieldsList.add(tempArrayList);
        }
        this.gameArea.setCenter(this.mainChessBoard);
    }

    private void setFiguresPositionOnBoard(double width, double height, String FEN) {
        //  FEN - Forsyth–Edwards Notation
        String numbers = "12345678";
        String figures = "KkQqRrBbNnPp";

        int row = 0;
        int column = 0;
        String currentCharacter;

        for (int index = 0; index < FEN.length(); index++) {
            currentCharacter = String.valueOf(FEN.charAt(index));
            if (currentCharacter.equals("/")) {
                column = 0;
                row++;
                continue;
            }
            if (numbers.contains(currentCharacter)) column += Integer.parseInt(currentCharacter);
            if (figures.contains(currentCharacter)) {
                this.fieldsList.get(row).get(column).setFigure(this.figure.specifyFigure(width, height, currentCharacter));
                column++;
            }
        }
        this.setPreviousState();
    }

    private String createFENOfActualState(ArrayList<ArrayList<Field>> currentFieldList) {
        StringBuilder FEN = new StringBuilder();
        for (int verticalPosition = 0; verticalPosition < 8; verticalPosition++) {
            int freeFields = 0;
            for (int horizontalPosition = 0; horizontalPosition < 8; horizontalPosition++) {
                Field currentField = currentFieldList.get(verticalPosition).get(horizontalPosition);
                if (currentField.getChildren().isEmpty()) {
                    freeFields++;
                } else {
                    if (freeFields > 0) FEN.append(freeFields);
                    FEN.append(currentField.getFigure().getFigureType());
                    freeFields = 0;
                }
                if (freeFields == 8) FEN.append(freeFields);
            }
            FEN.append("/");
        }
        return FEN.toString();
    }

    private void setPreviousState() {
        this.previousState = new ArrayList<>();
        ArrayList<ArrayList<Field>> tempArray = new ArrayList<>(this.fieldsList);
        for (ArrayList<Field> list : tempArray) {
            ArrayList<Field> newList = new ArrayList<>();
            for (Field field : list) {
                Field newField = new Field(field.getWidth(), field.getHeight(), field.getColor(), field.getVerticalPosition(), field.getHorizontalPosition());
                if (field.getFigure() != null) newField.setFigure(new Figure(field.getWidth(), field.getHeight(), field.getFigure().getFigureType()));
                newList.add(newField);
            }
            this.previousState.add(newList);
        }
    }
}
