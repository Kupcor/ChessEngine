package org.pk.chessgame;
import javafx.scene.layout.*;
import org.pk.chessgame.figures.King;
import org.pk.chessgame.figures.Pawn;
import org.pk.chessgame.helperclasses.EnemyPiecesSelector;
import org.pk.chessgame.helperclasses.FENOfBoardStateCreator;
import org.pk.chessgame.helperclasses.FigureSwapper;

import java.util.ArrayList;

public class ChessGame extends BorderPane {
    private final ArrayList<ArrayList<Field>> currentStateFields;
    private ArrayList<ArrayList<Field>> previousStateFields;
    private ArrayList<Field> figureAllowedMoves;

    //  Figure objects to handle drag-and-drop functionalities
    private Field targetField = null;
    private Field sourceField = null;
    private Field whiteKing = null;
    private Field blackKing = null;

    private final int width;
    private final int height;

    private boolean whiteTurn = true;

    public ChessGame(int width, int height) {
        this.setPrefSize(width, height);
        this.width = width;
        this.height = height;

        ChessBoard chessBoard = new ChessBoard(width, height);
        this.setCenter(chessBoard);

        this.currentStateFields = chessBoard.getFieldsList();
        this.previousStateFields = chessBoard.getPreviousState();

        this.setFiguresPositionOnBoard(width, height, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        this.previousStateFields = cloneState(this.currentStateFields);

        //  Mechanism of moves on a chessboard
        this.setOnMouseReleased(mouseEvent -> {
            int horPos = (int)  mouseEvent.getX() / (this.width / 8);
            int verPos = (int) mouseEvent.getY() / (this.height / 8);
            this.selectFigureDestination(verPos, horPos);
        });

        this.setOnMousePressed(mouseEvent -> {
            int horPos = (int)  mouseEvent.getX() / (this.width / 8);
            int verPos = (int) mouseEvent.getY() / (this.height / 8);
            this.selectFigureToMove(verPos, horPos);
        });
    }

    private void selectFigureToMove(int verPos, int horPos) {
        Field selectedField = this.currentStateFields.get(verPos).get(horPos);
        if (selectedField.getChildren().isEmpty()) return;
        if (selectedField.getFigure().isFigureWhite != this.whiteTurn) return;
        this.sourceField = selectedField;
        this.sourceField.changeFieldColor("#cf9a65");
        this.figureAllowedMoves = this.sourceField.getFigure().getAvailableMoves(this.currentStateFields, this.previousStateFields, verPos, horPos);
        this.figureAllowedMoves.removeIf(field -> (!field.getChildren().isEmpty() && field.getFigure().isFigureWhite == this.whiteTurn));
        for (Field field : figureAllowedMoves) field.changeFieldColor("#e3cea8");
    }

    private void selectFigureDestination(int verPos, int horPos) {
        //for (Field field : figureAllowedMoves) field.setOriginFieldColor();
        if (sourceField == null) return;
        Field selectedField = currentStateFields.get(verPos).get(horPos);

        if (!isValidMove(selectedField)) return;

        this.targetField = selectedField;
        Figure sourceFigure = this.sourceField.getFigure();
        Figure targetFigure = selectedField.getFigure();
        this.previousStateFields = cloneState(this.currentStateFields);

        this.executeEnPassantMechanism(this.sourceField, this.targetField);
        this.executeCastlingMechanism(this.sourceField, this.targetField);

        sourceFigure.setDidFigureMove();
        FigureSwapper.swapFigures(this.sourceField, this.targetField);
        if (sourceFigure instanceof King) {
            this.whiteKing = whiteTurn ? targetField : this.whiteKing;
            this.blackKing = whiteTurn ? this.blackKing : targetField;
        }
        this.targetField .changeFieldColor("#ab7846");
        this.sourceField = null;

        this.executeCheckAndCheckMateMechanism();
    }

    private boolean isValidMove(Field selectedField) {
        if (!this.figureAllowedMoves.contains(selectedField)) return false;
        if (selectedField == sourceField) return false;
        return selectedField.getChildren().isEmpty() || selectedField.getFigure().isFigureWhite != sourceField.getFigure().getIsFigureWhite();
    }

    private void executeEnPassantMechanism(Field sourceField, Field targetField) {
        if (sourceField.getFigure() instanceof Pawn && (sourceField.getVerticalPosition() == 3 || sourceField.getVerticalPosition() == 4)) {
            if (targetField.getChildren().isEmpty() && targetField.getHorizontalPosition() != sourceField.getHorizontalPosition()) {
                this.currentStateFields.get(sourceField.getVerticalPosition()).get(targetField.getHorizontalPosition()).removeFigure();
            }
        }
    }

    private void executeCastlingMechanism(Field sourceField, Field targetField) {
        if (sourceField.getFigure() instanceof King && Math.abs(targetField.getHorizontalPosition() - sourceField.getHorizontalPosition()) > 1) {
            Field secondTargetField;
            Field rookField;
            if (targetField.getHorizontalPosition() - sourceField.getHorizontalPosition() > 0) {
                secondTargetField = currentStateFields.get(targetField.getVerticalPosition()).get(targetField.getHorizontalPosition() - 1);
                rookField = currentStateFields.get(targetField.getVerticalPosition()).get(7);
            } else {
                secondTargetField = currentStateFields.get(targetField.getVerticalPosition()).get(targetField.getHorizontalPosition() + 1);
                rookField = currentStateFields.get(targetField.getVerticalPosition()).get(0);
            }
            rookField.getFigure().setDidFigureMove();
            secondTargetField.setFigure(rookField.getAndRemoveFigure());
        }
    }

    private void executeCheckAndCheckMateMechanism() {
        if (this.whiteTurn && !this.whiteKing.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, true).isEmpty()) {
            System.out.println("White king still under attack");
            setFiguresPositionOnBoard(this.width, this.height, FENOfBoardStateCreator.createFENOfBoardState(this.previousStateFields));
            return;
        }
        if (!this.whiteTurn && !this.blackKing.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, false).isEmpty()) {
            System.out.println("Black king under attack");
            setFiguresPositionOnBoard(this.width, this.height, FENOfBoardStateCreator.createFENOfBoardState(this.previousStateFields));
            return;
        }
        this.checkGameStatus();
        whiteTurn = !whiteTurn;
    }

    // Check game status
    private void checkGameStatus() {
        if (this.evaluateGame(this.whiteTurn, currentStateFields, previousStateFields).isEmpty()) {
            if (this.whiteTurn) {
                if (this.blackKing.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, this.blackKing.getFigure().getIsFigureWhite()).isEmpty()) {
                    System.out.println("Draw, white get pat");
                } else {
                    System.out.println("White Won");
                }
            } else {
                if (this.whiteKing.checkIfFieldIsUnderAttack(currentStateFields, previousStateFields, this.blackKing.getFigure().getIsFigureWhite()).isEmpty()) {
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

    //  Method return list of moves that can save a king
    private ArrayList<Field> evaluateGame(boolean whiteMove, ArrayList<ArrayList<Field>> currentFields, ArrayList<ArrayList<Field>> previousState) {
        ArrayList<Field> opponentPieces = EnemyPiecesSelector.getOpponentPieces(whiteTurn, currentFields);
        ArrayList<Field> savingMoves = new ArrayList<>();
        Field kingPosition = whiteMove ? this.blackKing : this.whiteKing;

        for (Field opponentPiece : opponentPieces) {
            ArrayList<Field> availableFields = opponentPiece.getFigure().getAvailableMoves(currentFields, previousState, opponentPiece.getVerticalPosition(), opponentPiece.getHorizontalPosition());
            availableFields.removeIf(tempField -> (!tempField.getChildren().isEmpty() && tempField.getFigure().getIsFigureWhite() != whiteMove));
            Figure tempOpponentFigure = opponentPiece.getFigure();
            Figure tempFigure = null;
            for (Field availableField : availableFields) {
                if (!availableField.getChildren().isEmpty()) tempFigure = availableField.getFigure();
                FigureSwapper.swapFigures(opponentPiece, availableField);
                if (kingPosition.checkIfFieldIsUnderAttack(currentFields, previousState, !whiteMove).isEmpty()) {
                    savingMoves.add(availableField);
                }
                FigureSwapper.swapFigures(availableField, opponentPiece);
                opponentPiece.setFigure(tempOpponentFigure);
                if (tempFigure != null) {
                    availableField.setFigure(tempFigure);
                    tempFigure = null;
                }
            }
        }

        kingPosition.getFigure().getAvailableMoves(currentFields, previousState, kingPosition.getVerticalPosition(), kingPosition.getHorizontalPosition())
                .stream()
                .filter(field -> field.checkIfFieldIsUnderAttack(currentFields, previousState, !whiteMove).isEmpty())
                .forEach(savingMoves::add);
        savingMoves.removeIf(availableField -> (!availableField.getChildren().isEmpty() && availableField.getFigure().isFigureWhite == !whiteMove));

        return savingMoves;
    }

    //  We are using reference list fieldsList here
    private void setFiguresPositionOnBoard(double width, double height, String FEN) {
        //  FEN - Forsyth–Edwards Notation
        String piecesNotation = "KkQqRrBbNnPp";
        String emptySpaceNotation = "12345678";
        Character rowSeparator = '/';

        int row = 0;
        int column = 0;

        for (Character currentChar : FEN.toCharArray()) {
            if (currentChar == rowSeparator) {
                column = 0;
                row++;
                continue;
            }
            if (emptySpaceNotation.indexOf(currentChar) != -1) {
                for (int i = column; i < column + Character.getNumericValue(currentChar); i++) this.currentStateFields.get(row).get(i).removeFigure();
                column += Character.getNumericValue(currentChar);
            }
            if (piecesNotation.indexOf(currentChar) != -1) {
                this.currentStateFields.get(row).get(column).setFigure(Figure.createFigure(width, height, String.valueOf(currentChar)));
                Field previousField = this.previousStateFields.get(row).get(column);
                if (!previousField.getChildren().isEmpty() && !previousField.getFigure().getDidFigureNotMove()) this.currentStateFields.get(row).get(column).getFigure().setDidFigureMove();
                if (currentChar == 'k') this.blackKing = this.currentStateFields.get(row).get(column);
                if (currentChar == 'K') this.whiteKing = this.currentStateFields.get(row).get(column);
                column++;
            }
        }
    }

    private ArrayList<ArrayList<Field>> cloneState(ArrayList<ArrayList<Field>> currentFields) {
        ArrayList<ArrayList<Field>> previousState = new ArrayList<>();
        for (ArrayList<Field> list : currentFields) {
            ArrayList<Field> newList = new ArrayList<>();
            for (Field field : list) {
                Field newField = new Field(field);
                newList.add(newField);
            }
            previousState.add(newList);
        }
        return previousState;
    }
}
