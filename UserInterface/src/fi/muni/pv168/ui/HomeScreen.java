package fi.muni.pv168.ui;

import fi.muni.pv168.ui.resources.Resources;
import fi.muni.pv168.ui.tabs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class HomeScreen {

    final static Logger logger = LoggerFactory.getLogger(HomeScreen.class);

    Tab today, knights, disciplines, matches;

    Tab[] tabs;

    public HomeScreen() {

        //hack to enable native OS X menu functionality
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", Resources.getString("tourney_manager"));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            String description = "Unable to set look and feel feature: "+e.toString();
            logger.error(description, e);
        } catch (InstantiationException e) {
            String description = "Unable to set look and feel feature: "+e.toString();
            logger.error(description, e);
        } catch (IllegalAccessException e) {
            String description = "Unable to set look and feel feature: "+e.toString();
            logger.error(description, e);
        } catch (UnsupportedLookAndFeelException e) {
            String description = "Unable to set look and feel feature: "+e.toString();
            logger.error(description, e);
        }

        today = new TodayTab();
        knights = new KnightTab();
        disciplines = new DisciplineTab();
        matches = new MatchTab();
        tabs = new Tab[] {today, knights, disciplines, matches};

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();

                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setMinimumSize(new Dimension(200, 550));
                frame.setBounds(0, 0, 700, 550);

                frame.setTitle(Resources.getString("tourney_manager"));

                frame.setJMenuBar(initMenu());

                frame.add(initTabbedLayout(), BorderLayout.NORTH);


                frame.setVisible(true);
            }
        });
    }

    private JMenuBar initMenu() {

        JMenuBar menuBar = new JMenuBar();

        for (Tab tab : tabs) {
            JMenu menu = tab.getMenu();
            if (menu != null) {
                menuBar.add(menu);
            }
        }

        return menuBar;
    }

    private JTabbedPane initTabbedLayout() {

        JTabbedPane tabbedPane = new JTabbedPane();

        for (Tab tab : tabs) {
            tabbedPane.addTab(Resources.getString(tab.getTitleKey()), tab.getPanel());
        }

        return tabbedPane;
    }

}
