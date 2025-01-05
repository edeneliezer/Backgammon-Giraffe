package jUnit;

import static org.junit.Assert.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;
import view.InfoPanel;

import java.util.concurrent.CountDownLatch;

public class InfoPanelTest {

    private InfoPanel infoPanel;

    @Before
    public void setup() throws Exception {
        // Initialize the JavaFX toolkit
        new JFXPanel(); // Ensures the JavaFX runtime is initialized
        CountDownLatch latch = new CountDownLatch(1);

        // Initialize the InfoPanel on the JavaFX Application Thread
        Platform.runLater(() -> {
            infoPanel = new InfoPanel();
            latch.countDown(); // Notify that initialization is complete
        });

        // Wait for the JavaFX thread to complete initialization
        latch.await();
    }

    @Test
    public void testReset() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1); // Declare as final

        // Arrange: Add some text to the text container
        Platform.runLater(() -> {
            Text sampleText = new Text("Sample text");
            infoPanel.getTextContainer().getChildren().add(sampleText);
            latch.countDown();
        });

        // Wait for the JavaFX thread to process the addition
        latch.await();

        // Verify the text container is not empty initially
        assertFalse("Text container should not be empty initially",
                infoPanel.getTextContainer().getChildren().isEmpty());

        final CountDownLatch resetLatch = new CountDownLatch(1); // Declare new latch as final

        // Act: Call the reset method
        Platform.runLater(() -> {
            infoPanel.reset();
            resetLatch.countDown();
        });

        // Wait for the JavaFX thread to process the reset
        resetLatch.await();

        // Assert: Verify the text container is cleared
        assertTrue("Text container should be empty after reset",
                infoPanel.getTextContainer().getChildren().isEmpty());
    }
}
