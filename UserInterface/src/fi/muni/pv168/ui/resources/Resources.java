package fi.muni.pv168.ui.resources;

import java.util.Locale;
import java.util.ResourceBundle;

public class Resources {

    public static String TOURNEY_MANAGER = "tourney_manager";

    private static ResourceBundle bundle;

    public static String getString(String key) {
        return getResources().getString(key);
    }

    private static ResourceBundle getResources() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("strings", Locale.getDefault());
        }
        return bundle;
    }

}
