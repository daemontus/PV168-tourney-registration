package fi.muni.pv168.ui.tabs;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class MatchTab implements Tab {

    public MatchTab() {

    }

    @Override
    public JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        JTable content = new JTable(knightTableModel);
        content.setRowHeight(20);

        TableColumnModel model = content.getColumnModel();
        content.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.getColumn(0).setPreferredWidth(150);
        model.getColumn(1).setPreferredWidth(170);
        model.getColumn(2).setPreferredWidth(90);
        model.getColumn(3).setPreferredWidth(50);

        JScrollPane pane = new JScrollPane(content);
        pane.setPreferredSize(new Dimension(420, 455));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        panel.add(pane, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(4,0,0,0);
        panel.add(new JProgressBar(), constraints);
        constraints.insets = new Insets(0,0,0,0);
        constraints.gridx = 1;
        panel.add(new JButton("Edit"), constraints);
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(new JButton("Delete"), constraints);
        return panel;
    }

    @Override
    public JMenu getMenu() {
        return null;
    }

    @Override
    public String getTitleKey() {
        return "matches";
    }

    private TableModel knightTableModel = new AbstractTableModel() {

        private final Object[][] data = new Object[][] {
                new Object[] {"TestKnight", "TestDiscipline", "01", "123"},
                new Object[] {"TestKnight", "TestDiscipline", "01", "123"},
                new Object[] {"TestKnight", "TestDiscipline", "01", "123"},
                new Object[] {"TestKnight", "TestDiscipline", "01", "123"}

        };

        private final String[] COLUMNS = new String[]{"Knight", "Discipline", "Start. number", "Points"};

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public String getColumnName(int i) {
            return COLUMNS[i];
        }
    };

}
