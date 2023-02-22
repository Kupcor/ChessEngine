package org.pk.chessboard;
import javafx.scene.layout.*;
import org.pk.chessboard.figures.King;
import org.pk.chessboard.figures.Pawn;
import java.util.ArrayList;

public class ChessGame extends BorderPane {
    private ArrayList<ArrayList<Field>> fieldsList = new ArrayList<>();
    private ArrayList<ArrayList<Field>> previousState;
    private ArrayList<Field> figureAllowedMoves = new ArrayList<>();

    private final BorderPane gameArea = new BorderPane();
    private final GridPane mainChessBoard = new GridPane();
    private final Figure figure;

    //  Figure objects to handle drag-and-drop functionalities
    private Field targetField = null;
    private Field sourceField = null;
    private Field whiteKing;
    private Field blackKing;

    private int width;
    private int height;

    private boolean whiteTurn = true;

    public ChessGame(int width, int height) {
        this.setPrefSize(width, height);
        this.width = width;
        this.height = height;
        this.figure = new Figure(width, height, "Figure creator");

        //this.createBoard(width, height);
        //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
        this.setFiguresPositionOnBoard(width, height, "QRK6/8/8/8/8/8/8/7k");
        this.setCenter(this.gameArea);

        //  Moving figures logic / mechanism
        this.setOnMouseReleased(mouseEvent -> {
            int horizontalField = (int) mouseEvent.getX() / (width / 8);
            int verticalField = (int) mouseEvent.getY() / (height / 8);
            for (Field field : figureAllowedMoves) field.setOriginFieldColor();

            if (sourceField == null) return;

            this.targetField = fieldsList.get(verticalField).get(horizontalField);
            this.createPreviousGameState();
            if (!figureAllowedMoves.contains(targetField) || targetField == sourceField) {
                this.targetField = null;
                return;
            }

            Figure sourceFigure = sourceField.getFigure();
            Figure targetFigure = targetField.getFigure();

            //  En passant
            if (sourceFigure instanceof Pawn && (sourceField.getVerticalPosition() == 3 || sourceField.getVerticalPosition() == 4)) {
                if (targetField.getChildren().isEmpty() && targetField.getHorizontalPosition() != sourceField.getHorizontalPosition()) {
                    fieldsList.get(sourceField.getVerticalPosition()).get(targetField.getHorizontalPosition()).getAndRemoveFigure();
                }
            }

            //  Castling
            if (sourceFigure instanceof King && Math.abs(targetField.getHorizontalPosition() - sourceField.getHorizontalPosition()) > 1) {
                Field secondTargetField;
                Field rookField;
                if (targetField.getHorizontalPosition() - sourceField.getHorizontalPosition() > 0) {
                    secondTargetField = fieldsList.get(targetField.getVerticalPosition()).get(targetField.getHorizontalPosition() - 1);
                    rookField = fieldsList.get(targetField.getVerticalPosition()).get(7);
                } else {
                    secondTargetField = fieldsList.get(targetField.getVerticalPosition()).get(targetField.getHorizontalPosition() + 1);
                    rookField = fieldsList.get(targetField.getVerticalPosition()).get(0);
                }
                rookField.getFigure().setDidFigureMove();
                secondTargetField.setFigure(rookField.getAndRemoveFigure());
            }

            if (targetField.getChildren().isEmpty() || sourceFigure.getIsFigureWhite() != targetFigure.getIsFigureWhite()) {
                sourceFigure.setDidFigureMove();
                targetField.setFigure(sourceField.getAndRemoveFigure());
                if (sourceFigure instanceof King) {
                    if (whiteTurn) this.whiteKing = targetField;
                    else this.blackKing = targetField;
                }
                targetField.changeFieldColor("#ab7846");
                sourceField = null;

                if (this.whiteTurn && !this.whiteKing.checkIfFieldIsUnderAttack(fieldsList, previousState, true).isEmpty()) {
                    System.out.println("White king still under attack");
                    this.setFiguresPositionOnBoard(this.width, this.height, this.createFENOfActualState(this.previousState));
                    return;
                }
                if (!this.whiteTurn && !this.blackKing.checkIfFieldIsUnderAttack(fieldsList, previousState, false).isEmpty()) {
                    System.out.println("Black king under attack");
                    this.setFiguresPositionOnBoard(this.width, this.height, this.createFENOfActualState(this.previousState));
                    return;
                }
                this.checkGameStatus();
                whiteTurn = !whiteTurn;
            }
        });

        this.setOnMousePressed(mouseEvent -> {
            this.selectFigureToMove(mouseEvent.getY(), mouseEvent.getX());
        });

    }

    //  Move actions
    private void selectFigureToMove(double verticalPosition, double horizontalPosition) {
        int horPos = (int) horizontalPosition / (this.width / 8);
        int verPos = (int) verticalPosition / (this.height / 8);
        if (fieldsList.get(verPos).get(horPos).getChildren().isEmpty()) return;
        if (fieldsList.get(verPos).get(horPos).getFigure().isFigureWhite != this.whiteTurn) return;
        this.sourceField = fieldsList.get(verPos).get(horPos);
        this.sourceField.setOriginFieldColor();
        this.sourceField.changeFieldColor("#cf9a65");
        this.figureAllowedMoves = this.sourceField.getFigure().getAvailableMoves(this.fieldsList, this.previousState, verPos, horPos);
        this.figureAllowedMoves.removeIf(field -> (!field.getChildren().isEmpty() && field.getFigure().isFigureWhite == this.whiteTurn));
        for (Field field : figureAllowedMoves) {
            field.changeFieldColor("ffffff");
        }
    }

    //  Release action


    // Check game status
    private void checkGameStatus() {
        if (this.evaluateGame(this.whiteTurn, fieldsList, previousState).isEmpty()) {
            if (this.whiteTurn) {
                if (this.blackKing.checkIfFieldIsUnderAttack(fieldsList, previousState, this.blackKing.getFigure().getIsFigureWhite()).isEmpty()) {
                    System.out.println("Draw, white get pat");
                } else {
                    System.out.println("White Won");
                }
            } else {
                if (this.whiteKing.checkIfFieldIsUnderAttack(fieldsList, previousState, this.blackKing.getFigure().getIsFigureWhite()).isEmpty()) {
                    System.out.println("Draw, black get pat");
                } else {
                    System.out.println("Black Won");
                }
            }
        }

        //  Material Check


        //  Repeated position


        //  50 moves rules
    }

    //  Still sth is wrong
    //
    //  This checking only moves to cover king, it does not check if king can move
    private ArrayList<Field> evaluateGame(boolean whiteTurn, ArrayList<ArrayList<Field>> fieldList, ArrayList<ArrayList<Field>> previousState) {
        ArrayList<Field> opponentPieces = new ArrayList<>();
        ArrayList<Field> savingMoves = new ArrayList<>();
        Field kingPosition;

        if (whiteTurn) kingPosition = this.blackKing;
        else kingPosition = this.whiteKing;

        for (ArrayList<Field> list : fieldList) {
            for (Field field : list) {
                if (!field.getChildren().isEmpty() && field.getFigure().getIsFigureWhite() != whiteTurn) {
                    opponentPieces.add(field);
                }
            }
        }
        for (Field field : opponentPieces) {
            ArrayList<Field> availableFields = field.getFigure().getAvailableMoves(fieldList, previousState, field.getVerticalPosition(), field.getHorizontalPosition());
            availableFields.removeIf(tempField -> (!tempField.getChildren().isEmpty() && tempField.getFigure().getIsFigureWhite() != whiteTurn));

            Figure tempFigure = null;
            for (Field availableField : availableFields) {
                if (!availableField.getChildren().isEmpty()) tempFigure = availableField.getFigure();
                availableField.setFigure(field.getAndRemoveFigure());
                if (kingPosition.checkIfFieldIsUnderAttack(fieldList, previousState, !whiteTurn).isEmpty()) {
                    savingMoves.add(availableField);
                }
                field.setFigure(availableField.getAndRemoveFigure());
                if (tempFigure != null) {
                    availableField.setFigure(tempFigure);
                    tempFigure = null;
                }
            }
        }
        for (Field field : kingPosition.getFigure().getAvailableMoves(fieldsList, previousState, kingPosition.getVerticalPosition(), kingPosition.getHorizontalPosition())) {
            if (field.checkIfFieldIsUnderAttack(fieldList, previousState, !whiteTurn).isEmpty()) savingMoves.add(field);
        }
        return savingMoves;
    }

    //  To refactore
    private void createBoard(double width, double height) {
        this.mainChessBoard.getChildren().removeAll(this.mainChessBoard.getChildren());
        this.fieldsList.clear();
        this.mainChessBoard.setPrefSize(width, height);
        for (int verticalField = 0; verticalField < 8; verticalField++) {
            ArrayList<Field> tempArrayList = new ArrayList<>();
            for (int horizontalField = 0; horizontalField < 8; horizontalField++) {
                Field field = new Field(width, height, verticalField, horizontalField);
                tempArrayList.add(field);

                GridPane.setConstraints(field, horizontalField, verticalField);
                this.mainChessBoard.getChildren().add(field);
            }
            this.fieldsList.add(tempArrayList);
        }
        this.gameArea.setCenter(this.mainChessBoard);
    }

    //  To refactore
    private void setFiguresPositionOnBoard(double width, double height, String FEN) {
        //  FEN - Forsyth–Edwards Notation
        String numbers = "12345678";
        String figures = "KkQqRrBbNnPp";
        this.createBoard(width, height);

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
                this.fieldsList.get(row).get(column).setFigure(this.figure.createFigure(width, height, currentCharacter));
                if (currentCharacter.equals("k")) this.blackKing = this.fieldsList.get(row).get(column);
                if (currentCharacter.equals("K")) this.whiteKing = this.fieldsList.get(row).get(column);
                column++;
            }
        }
        this.createPreviousGameState();
    }

    //  It's good -> to test
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

    //  It's good - to test
    private void createPreviousGameState() {
        this.previousState = new ArrayList<>();
        ArrayList<ArrayList<Field>> tempArray = new ArrayList<>(this.fieldsList);
        for (ArrayList<Field> list : tempArray) {
            ArrayList<Field> newList = new ArrayList<>();
            for (Field field : list) {
                Field newField = new Field(field.getWidth(), field.getHeight(), field.getVerticalPosition(), field.getHorizontalPosition());
                if (field.getFigure() != null) newField.setFigure(new Figure(field.getWidth(), field.getHeight(), field.getFigure().getFigureType()));
                newList.add(newField);
            }
            this.previousState.add(newList);
        }
    }
}
