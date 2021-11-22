package cs1302.gallery;

import javafx.scene.layout.TilePane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * This class represnts the gallery used to display all the images returned by the iTune API.
 */
public class Gallery extends TilePane {

    /**An array of imageviews that houses all the images currently on diplay.*/
    private ImageView[] gallery;
    /**An integer reprstning the size of the gallery array.*/
    private final int size = 20;

    /**
     * A contructor to build a Gallery component.
     */
    public Gallery() {
        /*Intializes the gallery array and sets the size of
          this component to fit all the image views*/
        gallery = new ImageView[size];
        this.setPrefTileWidth(100.0);
        this.setPrefTileHeight(100.0);
        this.setMaxWidth(500);
        this.setMaxHeight(400);

        /*sets all the imageviews in the gallery array*/
        for (int i = 0; i < size; i++) {
            ImageView galleryImage = new ImageView();
            galleryImage.setPreserveRatio(true);
            galleryImage.setFitWidth(100);
            gallery[i] = galleryImage;
            /*Finally adds the gallery array into this component*/
            this.getChildren().add(gallery[i]);
        }
    }

    /**
     * This is a method that is used to set a selected image view in
     * the gallery array with an image.
     *
     * @param i an index represtning the image view to be replaced.
     * @param img the image used to replace in the imageview selcted.
     */
    public void setImage(int i,Image img) {
        this.gallery[i].setImage(img);
    }
}
