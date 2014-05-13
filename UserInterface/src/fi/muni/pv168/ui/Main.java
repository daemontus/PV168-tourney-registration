package fi.muni.pv168.ui;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main {

    public static void main(String[] args) {
        ResourceBundle localeStrings = ResourceBundle.getBundle("strings", Locale.getDefault());
        //System.out.println(localeStrings.getString("delete_knight"));

        new HomeScreen();
    }
}
