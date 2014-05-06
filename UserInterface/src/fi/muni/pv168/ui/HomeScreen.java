package fi.muni.pv168.ui;

import javax.swing.*;
import java.awt.*;

public class HomeScreen {


    public HomeScreen() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(200, 550));
                frame.setBounds(0, 0, 700, 550);

                frame.setTitle("Knight Manager");

                frame.add(initTabbedLayout(), BorderLayout.NORTH);

                frame.setJMenuBar(initMenu());

                frame.setVisible(true);
            }
        });
    }

    private JMenuBar initMenu() {
        //Where the GUI is created:
        JMenuBar menuBar;
        JMenu knightMenu, disciplineMenu, matchMenu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        knightMenu = new JMenu("Knight");
        disciplineMenu = new JMenu("Discipline");
        matchMenu = new JMenu("Match");

        menuBar.add(knightMenu);
        menuBar.add(disciplineMenu);
        menuBar.add(matchMenu);

        menuItem = new JMenuItem("New Knight...");
        knightMenu.add(menuItem);

        menuItem = new JMenuItem("Edit selected...");
        menuItem.setEnabled(false);
        knightMenu.add(menuItem);

        menuItem = new JMenuItem("Delete selected...");
        menuItem.setEnabled(false);
        knightMenu.add(menuItem);

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

        tabbedPane.addTab("Today", new TodayTab().getPanel());

        tabbedPane.addTab("Knights", new KnightTab().getPanel());

        tabbedPane.addTab("Disciplines", new DisciplineTab().getPanel());

        tabbedPane.addTab("Matches", new MatchTab().getPanel());

        return tabbedPane;
    }

}
