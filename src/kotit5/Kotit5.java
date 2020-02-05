package kotit5;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

/**Author: Veli Pekka Ulvinen
 * The homework number 5 for the Object-Oriented Programming course of the University of Vaasa and VAMK (Vaasa University of Applied Sciences)
 */

public class Kotit5 extends Application {

    private static double rate;
    private static int amount;
    private static int years;
    private static ObservableList<LoanYear> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        FlowPane topBar = new FlowPane();
        TableView<LoanYear> table = new TableView<>();

        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(5, 5, 5, 5));
        topBar.setHgap(10);
        Button calcButton = new Button("Calculate");
        Button saveButton = new Button("Save");
        Text amountText = new Text("Loan amount (€)");
        TextField amountField = new TextField();
        Text rateText = new Text("Interest rate (%)");
        TextField rateField = new TextField();
        Text yearText = new Text("Period (years)");
        TextField yearField = new TextField();
        topBar.getChildren().add(amountText);
        topBar.getChildren().add(amountField);
        topBar.getChildren().add(rateText);
        topBar.getChildren().add(rateField);
        topBar.getChildren().add(yearText);
        topBar.getChildren().add(yearField);
        topBar.getChildren().add(calcButton);
        topBar.getChildren().add(saveButton);
        root.setTop(topBar);

        table.setEditable(true);
        TableColumn yearColumn = new TableColumn("Year");
        yearColumn.setCellValueFactory(
                new PropertyValueFactory<LoanYear, Integer>("year"));
        TableColumn repaymentColumn = new TableColumn("Repayment");
        repaymentColumn.setCellValueFactory(
                new PropertyValueFactory<LoanYear, Double>("repayment"));
        TableColumn interestColumn = new TableColumn("Interest");
        interestColumn.setCellValueFactory(
                new PropertyValueFactory<LoanYear, Double>("interest"));
        TableColumn installmentColumn = new TableColumn("Installment");
        installmentColumn.setCellValueFactory(
                new PropertyValueFactory<LoanYear, Double>("installment"));
        TableColumn remainingColumn = new TableColumn("Remaining");
        remainingColumn.setCellValueFactory(
                new PropertyValueFactory<LoanYear, Double>("remaining"));
        table.getColumns().addAll(yearColumn, repaymentColumn, interestColumn, installmentColumn, remainingColumn);
        table.setItems(data);
        root.setCenter(table);

        primaryStage.setTitle("Loan Calculator");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();

        calcButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try
                {
                    years = Integer.parseInt(yearField.getText());
                    rate = Double.parseDouble(rateField.getText());
                    amount = Integer.parseInt(amountField.getText());
                    double repayment = (double) amount / years;
                    double interest;
                    double installment;
                    double remaining = amount;

                    for(int i=1; i<=years; i++)
                    {
                        interest = remaining * rate / 100;
                        installment = repayment + interest;
                        remaining -= repayment;
                        data.add(new LoanYear(i, repayment, interest, installment, remaining));
                    }
                }
                catch(Exception ex)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Check your input!");
                    alert.showAndWait();
                    return;
                }
            }
        });

        saveButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                File file = fileChooser.showSaveDialog(null);

                if(file != null)
                {
                    try(PrintWriter out = new PrintWriter(new FileOutputStream(file)))
                    {
                        out.print("Year\tRepayment\tInterest\tInstallment\tRemaining");
                        data.forEach(y -> out.print("\n" + y.toString()));
                    }
                    catch (FileNotFoundException ex)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Bad path!");
                        alert.showAndWait();
                    }
                }

            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static class LoanYear
    {
        private int year;
        private double repayment;
        private double interest;
        private double installment;
        private double remaining;

        public LoanYear(int year, double repayment, double interest, double installment, double remaining) {
            this.year = year;
            this.repayment = repayment;
            this.interest = interest;
            this.installment = installment;
            this.remaining = remaining;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public double getRepayment() {
            return repayment;
        }

        public void setRepayment(double repayment) {
            this.repayment = repayment;
        }

        public double getInterest() {
            return interest;
        }

        public void setInterest(double interest) {
            this.interest = interest;
        }

        public double getInstallment() {
            return installment;
        }

        public void setInstallment(double installment) {
            this.installment = installment;
        }

        public double getRemaining() {
            return remaining;
        }

        public void setRemaining(double remaining) {
            this.remaining = remaining;
        }

        public String toString()
        {
            return year + "\t" + repayment + "\t" + interest + "\t" + installment + "\t" + remaining;
        }
    }
}
