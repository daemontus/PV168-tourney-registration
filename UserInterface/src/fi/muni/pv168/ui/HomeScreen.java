package fi.muni.pv168.ui;

import fi.muni.pv168.ui.resources.Resources;
import fi.muni.pv168.ui.tabs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class HomeScreen {

    final static Logger logger = LoggerFactory.getLogger(HomeScreen.class);

    JMenu disciplineMenu, matchMenu;

    Tab today, knights, disciplines, matches;

    Tab[] tabs;

    public HomeScreen() {

        //hack to enable native OS X menu functionality
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", Resources.getString(Resources.TOURNEY_MANAGER));
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

                frame.setTitle(Resources.getString(Resources.TOURNEY_MANAGER));

                frame.setJMenuBar(initMenu());

                frame.add(initTabbedLayout(), BorderLayout.NORTH);


                frame.setVisible(true);
            }
        });
    }

    private JMenuBar initMenu() {

        //Where the GUI is created:
        JMenuBar menuBar;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        disciplineMenu = new JMenu("Discipline");
        matchMenu = new JMenu("Match");

        for (Tab tab : tabs) {
            JMenu menu = tab.getMenu();
            if (menu != null) {
                menuBar.add(menu);
            }
        }

        //toto pojde pred hned ako sa prerobia taby
        menuBar.add(disciplineMenu);
        menuBar.add(matchMenu);

        menuItem = new JMenuItem("New Discipline...");
        disciplineMenu.add(menuItem);

        menuItem = new JMenuItem("Edit selected...");
        menuItem.setEnabled(false);
        disciplineMenu.add(menuItem);

        menuItem = new JMenuItem("Delete selected...");
        menuItem.setEnabled(false);
        disciplineMenu.add(menuItem);

        menuItem = new JMenuItem("New Match...");
        matchMenu.add(menuItem);

        menuItem = new JMenuItem("Edit selected...");
        menuItem.setEnabled(false);
        matchMenu.add(menuItem);

        menuItem = new JMenuItem("Delete selected...");
        menuItem.setEnabled(false);
        matchMenu.add(menuItem);

        return menuBar;
    }

    private JTabbedPane initTabbedLayout() {

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab(Resources.getString("today"), today.getPanel());

        tabbedPane.addTab(Resources.getString("knights"), knights.getPanel());

        tabbedPane.addTab(Resources.getString("disciplines"), disciplines.getPanel());

        tabbedPane.addTab(Resources.getString("matches"), matches.getPanel());

        return tabbedPane;
    }

}
