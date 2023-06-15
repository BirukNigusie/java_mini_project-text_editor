package com.example.te;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class HelloController {

    @FXML
    private TextArea textArea;
    @FXML
    private Label fileLabel;
    @FXML
    private Label formatLabel;
    @FXML
    private Label editLabel;
    @FXML
    private Label viewlabel;
    private File currentFile;
    @FXML
    private Label statusBarLabel;

    private final double  defaultFontSize = 14.0;
    private double currentFontSize = defaultFontSize;
    private Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private ComboBox<String> encodingComboBox;
    private ComboBox<String> lineEndingComboBox;



    public void initialize() {

        //File label menu items

        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.setOnAction(event -> newFile());

        MenuItem newWindowMenuItem = new MenuItem("New window");
        newWindowMenuItem.setOnAction(event -> newWindow());

        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction(event -> openFile());

        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(event -> saveFile());

        MenuItem saveAsMenuItem = new MenuItem("Save As");
        saveAsMenuItem.setOnAction(event -> saveFileAs());

        MenuItem pageSetupMenuItem = new MenuItem("Page Setup");
        pageSetupMenuItem.setOnAction(event -> pageSetup());

        MenuItem printMenuItem = new MenuItem("Print");
        printMenuItem.setOnAction(event -> printFile());

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> exit());
        //File label context menu
        ContextMenu contextMenu1 = new ContextMenu();
        contextMenu1.getItems().addAll(newMenuItem, newWindowMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem,
                new SeparatorMenuItem(), pageSetupMenuItem, printMenuItem, new SeparatorMenuItem(), exitMenuItem);
        fileLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu1.show(fileLabel, event.getScreenX(), event.getScreenY());
            }
        });
        fileLabel.setContextMenu(contextMenu1);


    }
    // file functions
    private void newFile() {
        currentFile = null;
        textArea.clear();
    }

    private void newWindow() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage newStage = new Stage();
            newStage.setTitle("New Window");

            Scene scene = new Scene(root);
            newStage.setScene(scene);

            newStage.show();
        } catch (IOException e) {
            showError("Error", "Failed to open new window.");
        }

    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        File selectedFile = fileChooser.showOpenDialog(textArea.getScene().getWindow());


        if (selectedFile != null) {
            try {
                String content = Files.readString(selectedFile.toPath());
                textArea.setText(content);
                currentFile = selectedFile;
            } catch (IOException e) {
                showError("Error opening file", e.getMessage());
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            try {
                Files.writeString(currentFile.toPath(), textArea.getText());
            } catch (IOException e) {
                showError("Error saving file", e.getMessage());
            }
        }
    }
    private void saveFileAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        File selectedFile = fileChooser.showSaveDialog(textArea.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Files.writeString(selectedFile.toPath(), textArea.getText());
                currentFile = selectedFile;
            } catch (IOException e) {
                showError("Error saving file", e.getMessage());
            }
        }
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void printFile() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {
            job.getJobSettings().setPageLayout(pageLayout);
            boolean showDialog = job.showPrintDialog(stage);
            if (showDialog) {
                boolean printed = job.printPage(textArea);
                if (printed) {
                    job.endJob();
                } else {
                    showError("Printing Error", "Failed to print the document.");
                }
            }
        }
    }
    private void pageSetup() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {
            job.getJobSettings().setPageLayout(pageLayout);
             job.showPageSetupDialog(stage);
            job.endJob();
        }
    }
    public void exit() {
        System.exit(0);
    }
}


