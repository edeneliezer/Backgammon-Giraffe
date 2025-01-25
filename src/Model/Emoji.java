package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import view.PlayerPanel;

/**
 * This class represents the emoji object in the player's panel.
 * 
 * @teamname TeaCup
 */
public class Emoji extends ImageView{
    private ArrayList<Image> defaultImgs, thinkingImgs, hitImgs, winImgs, loseImgs;
    private Random rand = new Random();
    private final int DELAY = 10;

    public Emoji() {
        super();
        initImages();
    }

    private void initImages() {
        defaultImgs = new ArrayList<>();
        thinkingImgs = new ArrayList<>();
        hitImgs = new ArrayList<>();
        winImgs = new ArrayList<>();
        loseImgs = new ArrayList<>();
        loadImagesFromFileIntoArray("default", defaultImgs);
        loadImagesFromFileIntoArray("thinking", thinkingImgs);
        loadImagesFromFileIntoArray("hit", hitImgs);
        loadImagesFromFileIntoArray("win", winImgs);
        loadImagesFromFileIntoArray("lose", loseImgs);
        reset();
    }

    private void loadImagesFromFileIntoArray(String folderPath, ArrayList<Image> arr) {
        String directoryPath = "/game/img/player_panel/".concat(folderPath);

        try (InputStream txtStream = getClass().getResourceAsStream(directoryPath + "/" + folderPath + ".txt")) {
            if (txtStream == null) {
                throw new FileNotFoundException("Resource not found: " + directoryPath + "/" + folderPath + ".txt");
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(txtStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    InputStream imgStream = getClass().getResourceAsStream(directoryPath + "/" + line);
                    if (imgStream == null) {
                        throw new FileNotFoundException("Image not found: " + directoryPath + "/" + line);
                    }
                    arr.add(new Image(imgStream));
                    imgStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image convertFileToImage(File file) {
        Image image = null;
        try (InputStream input = new FileInputStream(file.getPath())) {
            image = new Image(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void setDefaultFace() {
        setImage(defaultImgs.get(rand.nextInt(defaultImgs.size())));
    }

    public void setThinkingFace() {
        setImage(thinkingImgs.get(rand.nextInt(thinkingImgs.size())));
    }

    public void setHitFace() {
        setImage(hitImgs.get(rand.nextInt(hitImgs.size())));

        Timeline hitPause = new Timeline(new KeyFrame(Duration.seconds(DELAY), ev -> setThinkingFace()));
        hitPause.setCycleCount(1);
        hitPause.play();
    }

    public void setWinFace() {
        setImage(winImgs.get(rand.nextInt(winImgs.size())));
    }

    public void setLoseFace() {
        setLoseFace(false);
    }

    public void setLoseFace(boolean hasPause) {
        setImage(loseImgs.get(rand.nextInt(loseImgs.size())));

        if (hasPause) {
            Timeline losePause = new Timeline(new KeyFrame(Duration.seconds(DELAY), ev -> setThinkingFace()));
            losePause.setCycleCount(1);
            losePause.play();
        }
    }

    public void reset() {
        setDefaultFace();
    }
}