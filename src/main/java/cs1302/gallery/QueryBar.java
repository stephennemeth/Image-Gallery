package cs1302.gallery;

import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class represnts the tool bar used for the application.
 */
public class QueryBar extends HBox {
    /**The pause button.*/
    private Button pause;
    /**The label for the search bar.*/
    private Label search;
    /**The search bar.*/
    private TextField query;
    /**The update button.*/
    private Button update;

    /**
     * A contructor for the tool bar to make a QueryBar object.
     */
    public QueryBar() {
        super(8);
        /*Intializes all the buttons*/
        this.pause = new Button("Pause");
        this.search = new Label("Search Query");
        /*Intializes the search bar with "Michael Jackson
          since that is the default query.*/
        this.query = new TextField("Michael Jackson");
        this.update = new Button("Update Images");

        /*Alligns tham all center left*/
        this.setAlignment(Pos.CENTER_LEFT);

        /*Adds all to the component*/
        this.getChildren().addAll(pause, search, query, update);
    }

    /**
     * This method is used to set the update button by supplying an EventtHandler
     * to its setOnAction() method.
     *
     * @param e an event handler used to set the update button.
     */
    public void setUpdate(EventHandler<ActionEvent> e) {
        this.update.setOnAction(e);
    }

    /**
     * This method is used to set the pause button by supplying an EventHandler
     * to ts setOnAction() method.
     *
     * @param e an EventHandler used to set the pause button
     */
    public void setPause(EventHandler<ActionEvent> e) {
        this.pause.setOnAction(e);
    }

    /**
     * Returns the text in the query textfield.
     *
     * @return the text in the query textfield.
     */
    public String getText() {
        return this.query.getText();
    }

    /**
     * Sets the text on the pause button. Should alternate between
     * Pause and Play.
     *
     * @param s A string for the pause button text.
     */
    public void setPauseButtonText(String s) {
        this.pause.setText(s);
    }

    /**
     * Sets whether the pause button is to be disabled or not.
     *
     * @param b a boolean indicating if the pause button is disabled
     */
    public void setPauseButtonState(boolean b) {
        this.pause.setDisable(b);
    }

    /**
     * Sets whether or not the update button is disabled.
     *
     * @param b A boolean indicating if the update button is disabled.
     */
    public void setUpdateButtonDisable(boolean b) {
        this.update.setDisable(b);
    }

    /**
     * This method is to show the error if less that 21 images are returned from the
     * itunes Query. It displays an Alert of type error if there is not enough with the
     * text "Not Enough images returned from the iTunes API".
     *
     * @param i an integer represnting the type of error that will be shown.
     * @param s  a string showing the error text to be displayed.
     */
    public static void showError(int i, String s) {
        Alert error = new Alert(AlertType.ERROR);
        error.setTitle("Error!");
        error.setHeaderText("Error");
        if (i == 1) {
            error.setContentText("Not enough images returned from the iTunes API");
        } else {
            error.setContentText("Error retriving from iTunes: " + s);
        }
        error.setResizable(true);
        error.getDialogPane().setPrefSize(300, 300);
        error.showAndWait();
    }
}
