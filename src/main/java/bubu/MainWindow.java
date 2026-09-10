package bubu;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Bubu bubu;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image bubuImage = new Image(this.getClass().getResourceAsStream("/images/bubu.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Bubu instance */
    public void setBubu(Bubu d) {
        assert d != null : "Bubu instance injected into MainWindow cannot be null";
        bubu = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Bubu's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        assert bubu != null : "Bubu controller must be initialized before handling input";
        assert dialogContainer != null : "FXML dialogContainer injection failed";
        String input = userInput.getText();
        String response = bubu.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBubuDialog(response, bubuImage)
        );
        userInput.clear();
    }
}
