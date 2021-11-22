package cs1302.gallery;

import java.net.URLEncoder;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.image.Image;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import cs1302.gallery.QueryBar;
import cs1302.gallery.Gallery;
import cs1302.gallery.ProgressPane;
import cs1302.gallery.MenuPane;

/**
 * Represents an iTunes GalleryApp!.
 */
public class GalleryApp extends Application {

    /**List to hold all references to the images returned byt he itunes query.*/
    private List<Image> images;
    /**Menu Bar Component.*/
    private MenuPane menuPane;
    /**Query Bar Component.*/
    private QueryBar queryBar
    /**Component for all the images in the gallery.*/;
    private Gallery gallery;
    /**Progress Bar Component.**/
    private ProgressPane progressPane;
    /**THe scene for the app.*/
    private Scene scene;
    /**The container for holding all the components.*/
    private VBox pane;
    /**THe thread that is used for the iTunes query.*/
    private Thread query;
    /**Used to track if the app is in paused mode.*/
    private boolean isPaused = false;
    /**Timeline used for diaplying the images.*/
    private Timeline timeLine;
    /**Used to track the progress of downloading the images.*/
    private double progress = 0.0;
    /**Indicates if the images have been downloaded or not.*/
    private boolean isDownloaded = false;
    /**Keeps track of the previous paused state incase there s an error.*/
    private boolean prevPaused = false;

    /** {@inheritDoc} */
    @Override
    public void init() {
        /*Initlaizes and sets all the components in the vbox container*/
        images = new ArrayList<>();
        menuPane = new MenuPane();
        queryBar = new QueryBar();
        /*Sets the button*/
        this.setActions();
        /*Sets the timeline*/
        this.setTimeline();
        gallery = new Gallery();
        progressPane = new ProgressPane();
        progressPane.setProgressBar(0.0);
        pane = new VBox(2);
        /*Adds all components into the scene*/
        pane.getChildren().addAll(menuPane, queryBar, gallery, progressPane);
        scene = new Scene(pane);
    }

    /**
     * This method sets the actions for the buttons in the toolbar.
     * They call the set methods located in the {@code QueryBar} class
     * and makes it so the update button queries itUnes and pause pauses the actions.
     */
    private void setActions() {
        /*Calls the set method for update*/
        queryBar.setUpdate(e -> {
            /*Resets everything*/
            progress = 0.0;
            isDownloaded = false;
            queryBar.setPauseButtonState(false);
            prevPaused = isPaused;
            isPaused = true;
            this.timeLine.stop();
            this.progressPane.setProgressBar(0.0);
            /*Creates the thread to query itunes*/
            query = new Thread(() -> {
                this.query();
            });
            query.setDaemon(true);
            query.start();
            /*After query resumes in whatever mode the app was in*/
            this.timeLine.play();
        });

        /*Calls the set method for teh pause button*/
        queryBar.setPause(e -> {
            /*Checks to see if the app was paused and sets the states accoirdingly*/
            if (isPaused) {
                isPaused = false;
                this.queryBar.setPauseButtonText("Pause");
                this.timeLine.play();
            } else {
                isPaused = true;
                this.queryBar.setPauseButtonText("Play");
                this.timeLine.pause();
            }
        });
    }

    /** {@inheritdoc}
     * Start method for staring the application.
     */
    @Override
    public void start(Stage stage) {
        /*Initializes the stage with the scene*/
        stage = new Stage();
        stage.setScene(scene);
        stage.setMaxWidth(640);
        stage.setMaxHeight(600);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        /*This is the initial thread to get the application started
          The instance thread vraibale will take over after*/
        Runnable r = () -> {
            this.query();
            this.timeLine.play();
        };
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    } // start

    /**
     * This is the query method that is used to get the images from iTunes.
     *
     * @throws UnsupportedEncodingException if the given encoding method
     * is not supported.
     * @throws MalformedURLException if the url generated is malforme.d
     * @throws IOException if there is not query returned by the api.
     */
    private void query() {
        try {
            /*Builds the url used to query and opens a stream from that url*/
            String term = URLEncoder.encode(queryBar.getText(), "UTF-8");
            String query = "https://itunes.apple.com/search?term=" + term + "&limit=200"
                + "&media=music";
            URL url = new URL(query);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            /*Calls doJsonStuff to parse the JSON file returned*/
            doJsonStuff(reader);
        } catch (UnsupportedEncodingException uee) {
            System.out.println(uee);
        } catch (MalformedURLException murle) {
            System.out.println(murle);
        } catch (IOException ioe) {
            /*This is if there is no JSON data returned
              The only other exception that could occur I found and it displays the
              FileNotFoundException in an error alert and returns the app to the previous
              mode it was in*/
            Platform.runLater(() -> QueryBar.showError(2, ioe.toString()));
            isDownloaded = true;
            isPaused = prevPaused;
            if (isPaused == false) {
                Platform.runLater(() -> queryBar.setPauseButtonText("Pause"));
            }
            this.progressPane.setProgressBar(1.0);
        }
    }

    /**
     * This is the method used to parse the JSON file returned by the iTunes api.
     * @param reader a InputReader that is getting the JSON file from the ituns API.
     * @throws NullPointerException if there is no JSON file to read from.
     * @throws ClassCastException if there is an illgal change of JSON types.
     */
    private void doJsonStuff(InputStreamReader reader) {
        /*Turns the data returned by the reader into a JSON element*/
        JsonElement je = JsonParser.parseReader(reader);
        JsonObject root = je.getAsJsonObject();
        JsonArray results = root.getAsJsonArray("results");
        /*Creates a temporary Array to house the JSON uls*/
        List<String> t = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            try {
                JsonObject result = results.get(i).getAsJsonObject();
                JsonElement artworkUrl100 = result.get("artworkUrl100");
                String art = artworkUrl100.getAsString();
                /*Gets the url as a string and checks if it is unique then adds to temp ArrayList*/
                if (!t.contains(art)) {
                    t.add(art);
                }
            } catch (NullPointerException npe) {
                System.out.println(npe);
            } catch (ClassCastException cce) {
                System.out.println(cce);
            }
        }
        /*If there is enough urls add them to the images and procede*/
        if (t.size() >= 21) {
            images.clear();
            for (int i = 0; i < t.size(); i++) {
                images.add(new Image(t.get(i)));
                Platform.runLater(() -> {
                    progressPane.setProgressBar((double) images.size() / t.size());
                });
            }
            /*Randomly fills the gallery with images*/
            Random r = new Random();
            for (int i = 0; i < 20; i++) {
                this.gallery.setImage(i, this.images.get(r.nextInt(images.size())));
            }
            /*Resume in the state when update was pressed*/
            isDownloaded = true;
            isPaused = prevPaused;
            if (isPaused == false) {
                Platform.runLater(() -> queryBar.setPauseButtonText("Pause"));
            }
        } else {
            /*if there arent enough images resume in the state when update was pressed*/
            Platform.runLater(() -> QueryBar.showError(1, ""));
            if (!images.isEmpty()) {
                isDownloaded = true;
            }
            isPaused = prevPaused;
            if (prevPaused == false) {
                Platform.runLater(() -> queryBar.setPauseButtonText("Pause"));
            }
            Platform.runLater(() -> progressPane.setProgressBar(1.0));
        }
        isPaused = prevPaused;
    }

    /**
     * This method sets the timeline used to display random images on a 2 second interval.
     */
    private void setTimeline() {
        /*Establishes a keyframe*/
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), e -> {
            if (isDownloaded && !isPaused) {
                Random r = new Random();
                /*Picks a random image to replace and a random spot in the gallery to replace*/
                int pick = r.nextInt(images.size());
                int replace = r.nextInt(20);
                gallery.setImage(replace, images.get(pick));
            }
        });

        /*Sets up the final parts of the timeline*/
        timeLine = new Timeline();
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.getKeyFrames().add(keyFrame);
    }
} // GalleryApp
