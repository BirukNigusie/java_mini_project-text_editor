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

//View label menu items

        MenuItem zoomIn=new MenuItem("Zoom In");
        zoomIn.setOnAction(event -> zoomIn());

        MenuItem zoomOut=new MenuItem("Zoom Out");
        zoomOut.setOnAction(event -> zoomOut());

        MenuItem defaultZoom=new MenuItem("Restore Default");
        defaultZoom.setOnAction(event -> defaultZoom());


        //Format label context menu items
        MenuItem themesMenuItem = new MenuItem("Themes");
        themesMenuItem.setOnAction(event -> openThemesDialog());

        MenuItem fontSizeMenuItem = new MenuItem("Font Size");
        fontSizeMenuItem.setOnAction(event -> openFontSizeDialog());

        MenuItem fontFamilyMenuItem = new MenuItem("Font Family");
        fontFamilyMenuItem.setOnAction(event -> openFontFamilyDialog());

        MenuItem fontStyleMenuItem = new MenuItem("Font Style");
        fontStyleMenuItem.setOnAction(event -> openFontStyleDialog());

        MenuItem wordWrapMenuItem = new MenuItem("Word Wrap");
        wordWrapMenuItem.setOnAction(event -> WordWrap());

        MenuItem textColorMenuItem = new MenuItem("Text Color");
        textColorMenuItem.setOnAction(event -> setTextColor());

        //view label context menu
        ContextMenu contextMenu3=new ContextMenu();
        contextMenu3.getItems().addAll(zoomOut,zoomIn,defaultZoom);

        //Format label context menu
        ContextMenu contextMenu4=new ContextMenu();
        contextMenu4.getItems().addAll(themesMenuItem,fontSizeMenuItem,fontFamilyMenuItem,fontStyleMenuItem,textColorMenuItem,wordWrapMenuItem);

        viewlabel.setOnMouseClicked(event -> {
            if(event.getButton()==MouseButton.PRIMARY){
                contextMenu3.show(viewlabel,event.getScreenX(),event.getScreenY());
            }
        });


        formatLabel.setOnMouseClicked(event -> {
            if(event.getButton()==MouseButton.PRIMARY){
                contextMenu4.show(formatLabel,event.getScreenX(),event.getScreenY());
            }
        });
        formatLabel.setContextMenu(contextMenu4);


        encodingComboBox = new ComboBox<>();
        lineEndingComboBox = new ComboBox<>();

        statusBar();

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
    private void zoomIn(){
        currentFontSize += 2.0;
        textArea.setStyle("-fx-font-size: " + currentFontSize + "px;");
    }

    private void zoomOut(){
        currentFontSize -= 2.0;
        textArea.setStyle("-fx-font-size: " + currentFontSize + "px;");
    }

    private void defaultZoom(){
        currentFontSize = defaultFontSize;
        textArea.setStyle("-fx-font-size: " + currentFontSize + "px;");
    }
    private void WordWrap() {
        boolean isWordWrap = textArea.isWrapText();
        textArea.setWrapText(!isWordWrap);
    }
    private void statusBar() {
        textArea.caretPositionProperty().addListener((observable, oldValue, newValue) -> {
            int position = newValue.intValue();
            int row = textArea.getText().substring(0, position).split("\n").length;
            int column = position - textArea.getText().lastIndexOf("\n", position - 1);

            statusBarLabel.setText("Line: " + row + "   Column: " + column);
        });
    }
    private void setTextColor() {
        Color initialColor = Color.BLACK;
        javafx.scene.control.ColorPicker colorPicker = new javafx.scene.control.ColorPicker(initialColor);
        colorPicker.setPrefWidth(200);
        javafx.scene.control.Dialog<Color> dialog = new javafx.scene.control.Dialog<>();
        dialog.getDialogPane().setContent(colorPicker);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        dialog.setResultConverter(buttonType -> buttonType == javafx.scene.control.ButtonType.OK ? colorPicker.getValue() : null);
        java.util.Optional<Color> result = dialog.showAndWait();
        result.ifPresent(color -> textArea.setStyle("-fx-text-fill: " + toHexCode(color) + ";"));
    }
    private String toHexCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void openThemesDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Select a Theme");

        ListView<String> themeList = new ListView<>();
        themeList.getItems().addAll("Theme 1", "Theme 2", "Theme 3");

        Button applyButton = new Button("Apply");
        applyButton.setOnAction(event -> {
            String selectedTheme = themeList.getSelectionModel().getSelectedItem();
            if (selectedTheme != null) {
                applyTheme(selectedTheme);
                dialogStage.close();
            }
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(themeList, applyButton);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    private void applyTheme(String theme) {
        String cssFile = "theme1.css"; // Default theme

        if (theme.equals("Theme 1")) {
            cssFile = "C:\\Users\\ASHE\\Desktop\\javatest\\src\\main\\resources\\com\\example\\te/theme1.css";
        } else if (theme.equals("Theme 2")) {
            cssFile = "C:\\Users\\ASHE\\Desktop\\javatest\\src\\main\\resources\\com\\example\\te/theme2.css";
        } else if (theme.equals("Theme 3")) {
            cssFile = "C:\\Users\\ASHE\\Desktop\\javatest\\src\\main\\resources\\com\\example\\te/theme3.css";
        }

        try {
            File file = new File(cssFile);
            String css = file.toURI().toURL().toExternalForm();
            textArea.getStylesheets().clear();
            textArea.getStylesheets().add(css);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openFontSizeDialog() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(currentFontSize));
        dialog.setTitle("Font Size");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the font size:");

        dialog.showAndWait().ifPresent(size -> {
            try {
                double fontSize = Double.parseDouble(size);
                if (fontSize > 0) {
                    currentFontSize = fontSize;
                    textArea.setFont(Font.font(textArea.getFont().getFamily(), currentFontSize));
                }
            } catch (NumberFormatException e) {
                // Handle invalid input
            }
        });
    }

    private void openFontFamilyDialog() {
        List<String> fontFamilies = Font.getFamilies();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(textArea.getFont().getFamily(), fontFamilies);
        dialog.setTitle("Font Family");
        dialog.setHeaderText(null);
        dialog.setContentText("Select a font family:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(fontFamily -> {
            textArea.setFont(Font.font(fontFamily, currentFontSize));
        });
    }

    private void openFontStyleDialog() {
        List<String> fontStyles = Arrays.asList("Regular", "Bold", "Italic", "Bold Italic");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Regular", fontStyles);
        dialog.setTitle("Font Style");
        dialog.setHeaderText(null);
        dialog.setContentText("Select a font style:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(fontStyle -> {
            FontWeight fontWeight = FontWeight.NORMAL;
            FontPosture fontPosture = FontPosture.REGULAR;

            if (fontStyle.equals("Bold") || fontStyle.equals("Bold Italic")) {
                fontWeight = FontWeight.BOLD;
            }
            if (fontStyle.equals("Italic") || fontStyle.equals("Bold Italic")) {
                fontPosture = FontPosture.ITALIC;
            }

            textArea.setFont(Font.font(textArea.getFont().getFamily(), fontWeight, fontPosture, currentFontSize));
        });
    }


}


