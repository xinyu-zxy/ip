package bubu;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    /** Standard profile-picture size used for each conversation participant. */
    private static final double PROFILE_PICTURE_SIZE = 48.0;

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;
    @FXML
    private Label speakerName;
    @FXML
    private VBox messageColumn;

    private DialogBox(String text, Image img, String speaker) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        speakerName.setText(speaker.toUpperCase());
        speakerName.getStyleClass().add("speaker-name");
        displayPicture.setImage(img);
        displayPicture.setFitWidth(PROFILE_PICTURE_SIZE);
        displayPicture.setFitHeight(PROFILE_PICTURE_SIZE);
        Rectangle clip = new Rectangle(PROFILE_PICTURE_SIZE, PROFILE_PICTURE_SIZE);
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        displayPicture.setClip(clip);
        messageColumn.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        messageColumn.setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Returns a dialog box for the user.
     *
     * @param text The text to be displayed in the dialog box.
     * @param img The image to be displayed in the dialog box.
     * @return A dialog box for the user.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox dialogBox = new DialogBox(text, img, "You");
        dialogBox.setAlignment(Pos.CENTER_RIGHT);
        dialogBox.dialog.getStyleClass().add("user-label");
        return dialogBox;
    }

    /**
     * Returns a dialog box for Bubu.
     *
     * @param text The text to be displayed in the dialog box.
     * @param img The image to be displayed in the dialog box.
     * @return A dialog box for Bubu.
     */
    public static DialogBox getBubuDialog(String text, Image img) {
        var db = new DialogBox(text, img, "Bubu");
        db.flip();
        return db;
    }

    /** Applies the attention-grabbing style used for invalid commands. */
    public void setErrorStyle() {
        dialog.getStyleClass().add("error-label");
    }
}
