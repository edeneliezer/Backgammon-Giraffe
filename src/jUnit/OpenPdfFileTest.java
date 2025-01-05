package jUnit;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.Test;
import view.backgammonUI;

public class OpenPdfFileTest {

    @Test
    public void testOpenValidPdfFile() throws URISyntaxException, IOException {
        backgammonUI ui = new backgammonUI();

        // Provide a valid PDF file path for testing
        URL validFileUrl = getClass().getClassLoader().getResource("BackgammonRules.pdf");
        assertNotNull("Valid PDF file should exist", validFileUrl);

        File validFile = new File(validFileUrl.toURI());
        assertTrue("Valid PDF file should exist", validFile.exists());

        // Simulate opening the valid file
        try {
            ui.openPdfFile("BackgammonRules.pdf");
        } catch (Exception e) {
            fail("No exception should be thrown for a valid file");
        }
    }

}
