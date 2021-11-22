package cs1302.gallery;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

/**
 * This class represntes the pane at the botton of the application
 * that houses the progress bar.
 */
public class ProgressPane extends HBox {
    /**The progress bar.*/
    private ProgressBar progressBar;

    /** A contructor to make a progressPane that houses only the
     * progress bar inside of an HBox.
     */
    public ProgressPane() {
        progressBar = new ProgressBar();
        this.getChildren().add(progressBar);
    }

    /**
     * This method returns a double representing
     * the progress of the progress bar.
     *
     * @return a double representing the progress of the progress bar.
     */
    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    /**
     * This method take a double and sets the progress of the progress bar.
     *
     * @param d a double that will be used to set the progress of the progress bar.
     */
    public void setProgressBar(double d) {
        this.progressBar.setProgress(d);
    }
}
