package org.pk.chessboard;
import javafx.scene.layout.*;
import org.pk.chessboard.figures.King;
import org.pk.chessboard.figures.Pawn;

import java.util.ArrayList;

public class ChessGame extends BorderPane {
    private final ArrayList<ArrayList<Field>> fieldsList;
    private ArrayList<ArrayList<Field>> previousState;
    private ArrayList<Field> figureAllowedMoves = new ArrayList<>();

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

        this.fieldsList = chessBoard.getFieldsList();
        this.previousState = chessBoard.getPreviousState();

        this.setFiguresPositionOnBoard(width, height, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        this.previousState = savePreviousState(this.fieldsList);

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
        Field selectedField = this.fieldsList.get(verPos).get(horPos);
        if (selectedField.getChildren().isEmpty()) return;
        if (selectedField.getFigure().isFigureWhite != this.whiteTurn) return;
        this.sourceField = selectedField;
        this.sourceField.changeFieldColor("#cf9a65");
        this.figureAllowedMoves = this.sourceField.getFigure().getAvailableMoves(this.fieldsList, this.previousState, verPos, horPos);
        this.figureAllowedMoves.removeIf(field -> (!field.getChildren().isEmpty() && field.getFigure().isFigureWhite == this.whiteTurn));
        for (Field field : figureAllowedMoves) field.changeFieldColor("#e3cea8");
    }

    private void selectFigureDestination(int verPos, int horPos) {
        //for (Field field : figureAllowedMoves) field.setOriginFieldColor();
        if (sourceField == null) return;
        Field selectedField = fieldsList.get(verPos).get(horPos);

        if (!isValidMove(selectedField)) return;

        this.targetField = selectedField;
        Figure sourceFigure = this.sourceField.getFigure();
        Figure targetFigure = selectedField.getFigure();
        this.previousState = savePreviousState(this.fieldsList);

        this.executeEnPassantMechanism(this.sourceField, this.targetField);
        this.executeCastlingMechanism(this.sourceField, this.targetField);

        sourceFigure.setDidFigureMove();
        targetField.setFigure(sourceField.getAndRemoveFigure());
        if (sourceFigure instanceof King) {
            if (whiteTurn) this.whiteKing = targetField;
            else this.blackKing = targetField;
        }
        this.targetField.changeFieldColor("#ab7846");
        this.sourceField = null;

        this.executeCheckAndCheckMateMechanism();
    }

    private boolean isValidMove(Field selectedField) {
        if (!this.figureAllowedMoves.contains(selectedField)) return false;
        if (selectedField == sourceField) return false;
        return selectedField.getChildren().isEmpty() || selectedField.getFigure().isFigureWhite != sourceField.getFigure().getIsFigureWhite();
    }

    private void executeEnPassantMechanism(Field sourceField, Field targetField) {
        Figure sourceFigure = sourceField.getFigure();
        if (sourceFigure instanceof Pawn && (sourceField.getVerticalPosition() == 3 || sourceField.getVerticalPosition() == 4)) {
            if (targetField.getChildren().isEmpty() && targetField.getHorizontalPosition() != sourceField.getHorizontalPosition()) {
                this.fieldsList.get(sourceField.getVerticalPosition()).get(targetField.getHorizontalPosition()).removeFigure();
            }
        }
    }

    private void executeCastlingMechanism(Field sourceField, Field targetField) {
        Figure sourceFigure = sourceField.getFigure();
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
    }

    private void executeCheckAndCheckMateMechanism() {
        if (this.whiteTurn && !this.whiteKing.checkIfFieldIsUnderAttack(fieldsList, previousState, true).isEmpty()) {
            System.out.println("White king still under attack");
            setFiguresPositionOnBoard(this.width, this.height, this.createFENOfBoardState(this.previousState));
            return;
        }
        if (!this.whiteTurn && !this.blackKing.checkIfFieldIsUnderAttack(fieldsList, previousState, false).isEmpty()) {
            System.out.println("Black king under attack");
            setFiguresPositionOnBoard(this.width, this.height, this.createFENOfBoardState(this.previousState));
            return;
        }
        this.checkGameStatus();
        whiteTurn = !whiteTurn;
    }

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

    //  Method return list of moves that can save a king
    private ArrayList<Field> evaluateGame(boolean whiteMove, ArrayList<ArrayList<Field>> currentFields, ArrayList<ArrayList<Field>> previousState) {
        ArrayList<Field> opponentPieces = this.getOpponentPieces(whiteMove, currentFields);
        ArrayList<Field> savingMoves = new ArrayList<>();
        Field kingPosition = whiteMove ? this.blackKing : this.whiteKing;

        for (Field opponentPiece : opponentPieces) {
            ArrayList<Field> availableFields = opponentPiece.getFigure().getAvailableMoves(currentFields, previousState, opponentPiece.getVerticalPosition(), opponentPiece.getHorizontalPosition());
            availableFields.removeIf(tempField -> (!tempField.getChildren().isEmpty() && tempField.getFigure().getIsFigureWhite() != whiteMove));

            Figure tempFigure = null;
            for (Field availableField : availableFields) {
                if (!availableField.getChildren().isEmpty()) tempFigure = availableField.getFigure();
                this.swapFigures(opponentPiece, availableField);
                if (kingPosition.checkIfFieldIsUnderAttack(currentFields, previousState, !whiteMove).isEmpty()) {
                    savingMoves.add(availableField);
                }
                this.swapFigures(availableField, opponentPiece);
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

    //  Helper method for evaluateGame
    private ArrayList<Field> getOpponentPieces(boolean isWhiteTurn, ArrayList<ArrayList<Field>> fieldList) {
        ArrayList<Field> opponentPieces = new ArrayList<>();
        for (ArrayList<Field> list : fieldList) {
            for (Field field : list) {
                if (!field.getChildren().isEmpty() && field.getFigure().getIsFigureWhite() != isWhiteTurn) {
                    opponentPieces.add(field);
                }
            }
        }
        return opponentPieces;
    }

    private void swapFigures(Field sourceField, Field targetField) {
        Figure tempFigure = sourceField.getAndRemoveFigure();
        targetField.setFigure(tempFigure);
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
                for (int i = column; i < column + Character.getNumericValue(currentChar); i++) this.fieldsList.get(row).get(i).removeFigure();
                column += Character.getNumericValue(currentChar);
            }
            if (piecesNotation.indexOf(currentChar) != -1) {
                this.fieldsList.get(row).get(column).setFigure(Figure.createFigure(width, height, String.valueOf(currentChar)));
                Field previousField = this.previousState.get(row).get(column);
                if (!previousField.getChildren().isEmpty() && !previousField.getFigure().getDidFigureNotMove()) this.fieldsList.get(row).get(column).getFigure().setDidFigureMove();
                if (currentChar == 'k') this.blackKing = this.fieldsList.get(row).get(column);
                if (currentChar == 'K') this.whiteKing = this.fieldsList.get(row).get(column);
                column++;
            }
        }
    }

    private String createFENOfBoardState(ArrayList<ArrayList<Field>> currentFields) {
        StringBuilder FEN = new StringBuilder();
        for (ArrayList<Field> list : currentFields) {
            int numberOfFreeFields = 0;
            for (Field currentField : list) {
                if (currentField.getChildren().isEmpty()) numberOfFreeFields++;
                else {
                    this.appendFields(FEN, numberOfFreeFields, currentField.getFigure().getFigureType());
                    numberOfFreeFields = 0;
                }
            }
            this.appendFields(FEN, numberOfFreeFields);
            if (list != currentFields.get(currentFields.size() - 1)) FEN.append("/");
        }
        return FEN.toString();
    }

    //  Helper method for createFenOfBoardState
    private void appendFields(StringBuilder FEN, int numberOfFreeFields, String figureType) {
        if (numberOfFreeFields > 0) {
            FEN.append(numberOfFreeFields);
        }
        FEN.append(figureType);
    }

    //  Overloaded helper method for createFenOfBoardState
    private void appendFields(StringBuilder FEN, int numberOfFreeFields) {
        appendFields(FEN, numberOfFreeFields, "");
    }

    private ArrayList<ArrayList<Field>> savePreviousState(ArrayList<ArrayList<Field>> currentFields) {
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
