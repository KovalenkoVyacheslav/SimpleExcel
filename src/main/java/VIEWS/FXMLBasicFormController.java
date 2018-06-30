package VIEWS;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.mariuszgromada.math.mxparser.*;

public class FXMLBasicFormController implements Initializable {

    @FXML

    private AnchorPane ancr;

    private final int CELL_WIDTH = 75;
    private final int CELL_HEIGHT = 20;

    private final int ROW_COUNT = 51;
    private final int COLUMN_COUNT = 27;

    private TextField[][] textFields;
    private String[][] textFormula;
    private String[][] textResult;
    private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    @Override

    public void initialize(URL location, ResourceBundle resources) {

        textFields  = new TextField[ROW_COUNT][COLUMN_COUNT];
        textFormula = new String[ROW_COUNT][COLUMN_COUNT];
        textResult = new String[ROW_COUNT][COLUMN_COUNT];

        FillAncorPane();
        TextFieldHeader();
    }

    //заповняє нашу форму і додає ф-цію, яка буде викликати рекурсію
    private void FillAncorPane() {
        for(int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                textFields[i][j] = new TextField();

                if(j == 0) {
                    textFields[i][0].setPrefSize(CELL_WIDTH / 3 + 10, CELL_HEIGHT);
                    textFields[i][j].setLayoutX(j * CELL_WIDTH);
                    textFields[i][j].setAlignment(Pos.BASELINE_LEFT);
                }
                else {
                    textFields[i][j].setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                    textFields[i][j].setLayoutX(j * CELL_WIDTH - 2 * CELL_WIDTH/ 3 + 5);
                    textFields[i][j].setAlignment(Pos.CENTER);
                }

                textFields[i][j].setLayoutY(i * CELL_HEIGHT);
                final int rowIndex = i;
                final int columnIndex = j;
                textFields[i][j].setOnKeyPressed(event -> {
                    if(event.getCode().equals(KeyCode.ENTER)) {
                        textFormula[rowIndex][columnIndex] = textFields[rowIndex][columnIndex].getText();

                        StartOperation();

                        NewText();
                    }
                });
                ancr.getChildren().add(textFields[i][j]);
            }
        }
    }

    //додає заголовок до першого рядка та числа до першого стовпця
    private void TextFieldHeader() {
        textFields[0][0].setEditable(false);

        for(int i = 1; i < ROW_COUNT; ++i) {
            textFields[i][0].setText(String.valueOf(i));
            textFields[i][0].setEditable(false);
        }

        for(int i = 1; i < COLUMN_COUNT; ++i) {
            textFields[0][i].setText(String.valueOf((char)(i + 64)));
            textFields[0][i].setEditable(false);
        }
    }

    //ф-ція зчитування даних
    private void StartOperation() {
        for(int i = 1; i < ROW_COUNT; ++i) {
            for (int j = 1; j < COLUMN_COUNT; j++) {

                if(textFormula[i][j] != null) {
                    if(textFormula[i][j].startsWith("'")) {
                        textResult[i][j] = textFormula[i][j].substring(1);
                    }
                    else if(textFormula[i][j].startsWith("=")) {
                        textResult[i][j] = ParseString(textFormula[i][j].substring(1));
                    }
                    else if(tryParseDouble(textFormula[i][j])) {
                        textResult[i][j] = String.valueOf(Double.parseDouble(textFormula[i][j]));
                    }
                    else {
                        textResult[i][j] = "#InputError";
                    }
                }
            }
        }
    }

    //ф-ція передачі результату на форму
    private void NewText() {
        for(int i = 0; i < ROW_COUNT; i++) {
            for(int j = 0; j < COLUMN_COUNT; j++) {
                textFields[i][j].setText(textResult[i][j]);
            }
        }

        TextFieldHeader();
    }

    private String ParseString(String t) {
        t = t.replaceAll("\\s+", "");// видалили пробіли

        Expression e = new Expression(t);// створюємо вираз
        if(e.checkSyntax()) {
            return String.valueOf(e.calculate()); // якщо можливо порахувати, повертаємо результат
        }
        else
            return GetResultFromFormula(t);
    }

    private boolean tryParseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String GetResultFromFormula(String t) {

        if(t.startsWith("="))
            t = t.substring(1);

        ArrayList<String> formulaRef = new ArrayList<>();
        ArrayList<String> formulaSign = new ArrayList<>();
        String local = "";
        for (char x:t.toCharArray()) {
            if(x == '+' || x == '-' || x == '*' || x == '/') {
                formulaSign.add(String.valueOf(x));
                continue;
            }
            else if(x >= (int)'A' && x <= (int)'Z')
                local = local + x;
            else if(x >= (int)'0' && x <= (int)'9')
                local = local + x;
            if(local.length() >= 2 || tryParseDouble(local)) {
                formulaRef.add(local);
                local = "";
            }
        }
        if(formulaRef.size() == 1)
            return startRecursion(formulaRef.get(0));
        else {
            for(int i = 0; i < formulaRef.size(); i++)
                formulaRef.set(i, startRecursion(formulaRef.get(i)));

            String temp = "";
            for(int i = 0; i < formulaSign.size(); i++) {
                if(formulaRef.get(i).startsWith("-")) {
                    formulaRef.set(i, "(" + formulaRef.get(i)  + ")");
                }
                temp += formulaRef.get(i) + formulaSign.get(i);
            }

            temp += formulaRef.get(formulaSign.size());
            return String.valueOf(new Expression(temp).calculate());
        }
    }

    private boolean CheckNextStep(String vl)
    {
        Expression e = new Expression(vl);
        return e.checkSyntax();
    }

    private String startRecursion(String r) {
        if(tryParseDouble(r))
            return r;
        int i = Integer.valueOf(r.substring(1));
        int j = alphabet.indexOf(r.toCharArray()[0]) + 1;
        String prepare = textFormula[i][j];

        if(prepare == null)
            return  "0";

        if(prepare.startsWith("="))
            prepare = prepare.substring(1);

        if(CheckNextStep(prepare)) {
            textResult[i][j] = String.valueOf(new Expression(prepare).calculate());
            return textResult[i][j];
        }
        else
            return textResult[i][j] = String.valueOf(startRecursion(GetResultFromFormula(prepare)));
    }
}