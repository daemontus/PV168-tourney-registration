package fi.muni.pv168.ui.tabs;

import javax.swing.*;

public interface Tab {
    public JPanel getPanel();
    public JMenu getMenu();
    public String getTitleKey();
}
