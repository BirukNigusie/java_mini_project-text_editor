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

        //Edit label menu items

        MenuItem undoMenuItem = new MenuItem("Undo");
        undoMenuItem.setOnAction(event -> undo());

        MenuItem cutMenuItem = new MenuItem("Cut");
        cutMenuItem.setOnAction(event -> cut());

        MenuItem copyMenuItem = new MenuItem("Copy");
        copyMenuItem.setOnAction(event -> copy());

        MenuItem pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setOnAction(event -> paste());

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> delete());

        MenuItem searchwithbingMenuItem = new MenuItem("Search With Google");
        searchwithbingMenuItem.setOnAction(event -> searchwithbing());

        MenuItem findMenuItem = new MenuItem("Find");
        findMenuItem.setOnAction(event -> find());

        MenuItem findnextMenuItem = new MenuItem("Find Next");
        findnextMenuItem.setOnAction(event -> findnext());

        MenuItem findpreveousMenuItem = new MenuItem("Find Previous");
        findpreveousMenuItem.setOnAction(event -> findPrevious());

        MenuItem replaceMenuItem = new MenuItem("Replace");
        replaceMenuItem.setOnAction(event -> replace());

        MenuItem gotoMenuItem = new MenuItem("Go To");
        gotoMenuItem.setOnAction(event -> go_to());

        MenuItem selectallMenuItem = new MenuItem("Select All");
        selectallMenuItem.setOnAction(event -> selectall());

        MenuItem timedateMenuItem = new MenuItem("Time/Date");
        timedateMenuItem.setOnAction(event -> timedate());

           //Edit Label context menu
        ContextMenu contextMenu2 = new ContextMenu();
        contextMenu2.getItems().addAll(undoMenuItem, deleteMenuItem, pasteMenuItem, copyMenuItem, cutMenuItem,
                new SeparatorMenuItem(), replaceMenuItem, findpreveousMenuItem, findMenuItem, findnextMenuItem ,searchwithbingMenuItem,
                new SeparatorMenuItem(),gotoMenuItem , selectallMenuItem,timedateMenuItem
        );
        editLabel.setOnMouseClicked(event ->{
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu2.show(editLabel, event.getScreenX(), event.getScreenY());
            }
        });
        editLabel.setContextMenu(contextMenu2);


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

    private void undo() {

        textArea.undo();
    }

    private void cut() {
        String selectedText = textArea.getSelectedText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedText);
        clipboard.setContent(content);
        textArea.replaceSelection("");
    }

    private void copy() {

        String selectedText = textArea.getSelectedText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedText);
        clipboard.setContent(content);

    }
    private void paste() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            int caretPosition = textArea.getCaretPosition();
            String clipboardContent = clipboard.getString();
            textArea.insertText(caretPosition, clipboardContent);
        }
    }
    private void delete() {

        textArea.replaceSelection("");
    }
    private void searchwithbing() {
        String searchQuery = textArea.getSelectedText();
        if (!searchQuery.isEmpty()) {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("https://www.google.com/search?q=" + searchQuery));
            } catch (Exception e) {
                showError("Error", "Failed to open the browser.");
            }
        }
    }
    private void find() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Find");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter text to find:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(searchText -> {
            String text = textArea.getText();
            int startIndex = text.indexOf(searchText);
            if (startIndex != -1) {
                textArea.selectRange(startIndex, startIndex + searchText.length());
            } else {
                showError("Not Found", "Text not found.");
            }
        });
    }

    private void findnext() {
        String searchText = textArea.getSelectedText();
        int currentIndex = textArea.getCaretPosition();
        int nextIndex = textArea.getText().indexOf(searchText, currentIndex + 1);
        if (nextIndex != -1) {
            textArea.selectRange(nextIndex, nextIndex + searchText.length());
        } else {
            showError("Not Found", "No more occurrences found.");
        }
    }

    private void findPrevious() {
        String searchText = textArea.getSelectedText();
        if (searchText == null || searchText.isEmpty()) {
            showError("No Selection", "Please select text to search for.");
            return;
        }

        int currentIndex = textArea.getCaretPosition();
        int previousIndex = textArea.getText().lastIndexOf(searchText, currentIndex - searchText.length() - 1);
        if (previousIndex != -1) {
            textArea.selectRange(previousIndex, previousIndex + searchText.length());
        } else {
            showError("Not Found", "No previous occurrences found.");
        }
    }

    private void replace() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Replace");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter text to replace:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(replacementText -> {
            String text = textArea.getText();
            int selectionStart = textArea.getSelection().getStart();
            int selectionEnd = textArea.getSelection().getEnd();
            String selectedText = text.substring(selectionStart, selectionEnd);
            text = text.replace(selectedText, replacementText);
            textArea.setText(text);
        });
    }
    private void go_to() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Go To");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter line number:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(lineNumberText -> {
            try {
                int lineNumber = Integer.parseInt(lineNumberText);
                String[] lines = textArea.getText().split("\\n");
                if (lineNumber >= 1 && lineNumber <= lines.length) {
                    int position = 0;
                    for (int i = 0; i < lineNumber - 1; i++) {
                        position += lines[i].length() + 1; // +1 for the newline character
                    }
                    textArea.positionCaret(position);
                } else {
                    showError("Invalid Line Number", "Please enter a valid line number.");
                }
            } catch (NumberFormatException e) {
                showError("Invalid Input", "Please enter a valid line number.");
            }
        });
    }
    private void selectall() {

        textArea.selectAll();
    }
    private void timedate() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);
        textArea.insertText(textArea.getCaretPosition(), formattedDateTime);
    }

}


