package Model;

public class DoublingCubeStorer extends TouchablesStorer {

    public DoublingCubeStorer() {
        super();
    }

    /**
     * Adds a new DoublingCube to the storer and initializes it.
     */
    public void addCube() {
        initCube();
    }

    /**
     * Initialize the DoublingCube, pushes it to the stack, and starts the timer.
     */
    public void initCube() {
        removeCube();
        
        DoublingCube cube = new DoublingCube();
        push(cube);
        drawCube();

        // Start the timer for the newly added cube
        cube.startTimer();
    }

    /**
     * Removes all DoublingCubes in the storer (clears the stack) and stops their timers.
     */
    public void removeCube() {
        // Stop timers for all cubes before clearing
        for (Touchable touchable : this) {
            if (touchable instanceof DoublingCube) {
                ((DoublingCube) touchable).stopTimer();
            }
        }

        getChildren().clear();
        clear();
        drawCube();
    }
}
