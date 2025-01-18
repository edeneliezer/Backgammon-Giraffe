package Model;

public abstract class SpecialStation {
    // Template Method
    public final void activate(Player player) {
        displayMessage(player); // שלב משותף, הודעה בסיסית
        performAction(player); // שלב ספציפי לכל תחנה
    }

    // מתודה קבועה
    private void displayMessage(Player player) {
        System.out.println(player.getName() + " landed on a special station!");
    }

    // מתודה מופשטת שתמומש על ידי תחנות שונות
    protected abstract void performAction(Player player);
}

