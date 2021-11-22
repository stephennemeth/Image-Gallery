package cs1302.gallery;

import javafx.scene.layout.HBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents the menu bar in the application.
 */
public class MenuPane extends HBox {

    /**The file menu.*/
    private Menu file;
    /**The help menu.*/
    private Menu help;
    /**The about menu item.*/
    private MenuItem about;
    /**The exit menu item.*/
    private MenuItem exit;
    /**The menu bar for all the menus.*/
    private MenuBar menuBar;
    /**The alert that is displayed if the about menu item is clicked.*/
    private Alert aboutAlert;
    /**The text area that is diaplyed in the about alert.*/
    private TextArea textArea;

    /**
     * A constructor to make a MenuPane component.
     */
    public MenuPane() {
        /*Creates the menu items and
          their corresponding actions*/
        exit = new MenuItem("Exit");
        exit.setOnAction(e -> System.exit(0));
        about = new MenuItem("About");
        about.setOnAction(e -> setHelpAlert());
        /*Creates teh two menus with their apporiate components*/
        file = new Menu("File");
        help = new Menu("Help");
        help.getItems().add(about);
        file.getItems().add(exit);

        /*Finnalt creates the menu bar and adds both menus*/
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, help);

        /*add the menu bar into this component*/
        this.getChildren().add(menuBar);

        /*Sets the Hgrow to always to take up the entire width of the app*/
        HBox.setHgrow(menuBar, Priority.ALWAYS);
    }

    /**
     * This method sets the help alert with the name of the author,
     * version number, email address, and a picture of the author.
     */
    public void setHelpAlert() {
        /*Creates a new alert of type information*/
        Alert info = new Alert(AlertType.INFORMATION);
        info.setTitle("About Stephen Nemeth");
        info.setResizable(true);
        info.getDialogPane().setPrefSize(300, 300);
        /*Sets the header text*/
        info.setHeaderText("About Stephen Nemth");
        /*Sets the content and the image in the alert*/
        info.setContentText("Stephen Nemeth\nsjn57152@uga.edu\nVersion 1.0");
        ImageView me = new ImageView(new Image("https://i.imgur.com/6kRtoCp.jpg"));
        me.setPreserveRatio(true);
        me.setFitHeight(100);
        info.setGraphic(me);
        info.showAndWait();

    }
}
