package VIEWS;

import MODELS.InfoField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;
import org.mariuszgromada.math.mxparser.*;

public class FXMLBasicFormController implements Initializable {

    @FXML
    private AnchorPane ancr;
    InfoField infoField;
    private int cellWidth = 75;
    private int cellHeight = 20;
    private TextField[][] textFields;
    private String[][] textContains;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoField = new InfoField(51, 27);
        textFields  = new TextField[infoField.getCountRow()][infoField.getCountColumn()];
        textContains = new String[infoField.getCountRow()][infoField.getCountColumn()];
        FillAncorPane();
        TextFieldHeader();
    }


    private void FillAncorPane() {
        for(int i = 0; i < infoField.getCountRow(); i++) {
            for (int j = 0; j < infoField.getCountColumn(); j++) {
                textFields[i][j] = new TextField();

                if(j == 0) {
                    textFields[i][0].setPrefSize(cellWidth / 3 + 10, cellHeight);
                    textFields[i][j].setLayoutX(j * cellWidth);
                    textFields[i][j].setAlignment(Pos.BASELINE_LEFT);
                }
                else {
                    textFields[i][j].setPrefSize(cellWidth, cellHeight);
                    textFields[i][j].setLayoutX(j * cellWidth - 2 * cellWidth / 3 + 5);
                    textFields[i][j].setAlignment(Pos.CENTER);
                }

                textFields[i][j].setLayoutY(i * cellHeight);
                final int rowIndex = i;
                final int columnIndex = j;
                textFields[i][j].setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if(event.getCode().equals(KeyCode.ENTER)) {
                            textContains[rowIndex][columnIndex] = textFields[rowIndex][columnIndex].getText();

                            StartOperation();

                            NewText();
                        }
                    }
                });

                ancr.getChildren().add(textFields[i][j]);
            }
        }
    }

    private void TextFieldHeader() {
        textFields[0][0].setEditable(false);

        for(int i = 1; i < infoField.getCountRow(); ++i) {
            textFields[i][0].setText(String.valueOf(i));
            textFields[i][0].setEditable(false);
        }

        for(int i = 1; i < infoField.getCountColumn(); ++i) {
            textFields[0][i].setText(String.valueOf((char)(i + 64)));
            textFields[0][i].setEditable(false);
        }
    }

    private void StartOperation() {
        for(int i = 1; i <infoField.getCountRow(); ++i) {
            for (int j = 1; j < infoField.getCountColumn(); j++) {

                if(textContains[i][j] != null) {
                    if(textContains[i][j].toCharArray()[0] != '=') {
                        textContains[i][j] = "#InputError";
                        continue;
                    }

                    String temp = textContains[i][j].substring(1);
                    if(temp.toCharArray()[0] == '\'') {//якщо текст
                        temp = temp.substring(1);
                        textContains[i][j] = temp;
                    }
                    else {
                        temp = ParseString(temp);
                    }

                }
            }
        }
    }

    private void NewText() {
        for(int i = 0; i < infoField.getCountRow(); i++) {
            for(int j = 0; j < infoField.getCountColumn(); j++) {
                textFields[i][j].setText(textContains[i][j]);
            }
        }

        TextFieldHeader();
    }

    private String ParseString(String t) {
        t = t.replaceAll("\\s+", "");
//        String[] spliting = t.split("\\+|\\-|\\*|\\/");
        Expression e = new Expression(t);
        if(e.checkSyntax())
            System.out.println(e.calculate());
        else
            System.out.println("ERROR");
        return "";
    }
}
