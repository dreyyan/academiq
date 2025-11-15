package ums.ui;

public class MenuOperation {
    // * Attributes
    public String[] inputKeys;
    public String displayName;
    public Runnable action;

    // * Constructor (Parameterized)
    public MenuOperation(String[] inputKeys, String displayName, Runnable action) {
        this.inputKeys = inputKeys;
        this.displayName = displayName;
        this.action = action;
    }

    // * Getter
    public String getDisplayName() { return this.displayName; }

    // * Method
        public boolean matches(String input) {
        for (String key : inputKeys) {
            if (key.equalsIgnoreCase(input)) return true;
        }
        return false;
    }
}
