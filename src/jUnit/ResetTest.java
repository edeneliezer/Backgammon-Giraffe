package jUnit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import Model.Moves;
import controller.GameplayMovesController;
import java.util.HashMap;

public class ResetTest {

    private GameplayMovesController controller;

    @Before
    public void setup() {
        // Initialize the controller with minimal dependencies
        controller = new GameplayMovesController(null, null, null);
    }

    @Test
    public void testReset() {
        // Arrange: Simulate some state changes
        controller.setValidMoves(new Moves(null)); // Simulate moves being set
        controller.setStalemateCount(5);          // Simulate non-zero stalemate count

        // Act: Call the reset method
        controller.reset();

        // Assert: Verify the state is reset
        assertNull("Moves should be null after reset", controller.getValidMoves());
        assertFalse("isMovesMapped should be false after reset", controller.isMapped());
        assertEquals("Stalemate count should be 0 after reset", Integer.valueOf(0), getPrivateField(controller, "stalemateCount"));
        assertTrue("Map should be empty after reset", ((HashMap<?, ?>) getPrivateField(controller, "map")).isEmpty());
    }

    // Helper method to access private fields using reflection
    private <T> T getPrivateField(Object object, String fieldName) {
        try {
            java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access private field: " + fieldName, e);
        }
    }
}
