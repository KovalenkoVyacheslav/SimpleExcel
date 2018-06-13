package VIEWS;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLBasicFormController implements Initializable {

    @FXML
    private AnchorPane ancr;

    private int rows = 51;
    private int column = 27;
    private int cellWidth = 75;
    private int cellHeight = 20;
    private TextField[][] textFields = new TextField[rows][column];
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FillAncorPane();
        TextFieldHeader();
    }


    private void FillAncorPane() {
        ancr.setPrefSize(rows * cellWidth - 10,column * cellHeight + 10);
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                textFields[i][j] = new TextField();

                if(j == 0) {
                    textFields[i][0].setPrefSize(cellWidth / 3 + 10, cellHeight);
                    textFields[i][j].setLayoutX(j * cellWidth);
                    textFields[i][j].setAlignment(Pos.CENTER_LEFT);
                }
                else {
                    textFields[i][j].setPrefSize(cellWidth, cellHeight);
                    textFields[i][j].setLayoutX(j * cellWidth - 2 * cellWidth / 3);
                    textFields[i][j].setAlignment(Pos.CENTER);
                }

                textFields[i][j].setLayoutY(i * cellHeight);



                ancr.getChildren().add(textFields[i][j]);
            }
        }
    }

    private void TextFieldHeader() {
        textFields[0][0].setEditable(false);

        for(int i = 1; i < rows; ++i) {
            textFields[i][0].setText(String.valueOf(i));
            textFields[i][0].setEditable(false);
        }

        for(int i = 1; i < column; ++i) {
            textFields[0][i].setText(String.valueOf((char)(i + 64)));
            textFields[0][i].setEditable(false);
        }
    }
}
