package fi.muni.pv168.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class KnightTab implements Tab {

    private TableModel knightTableModel = new AbstractTableModel() {

        private final Object[][] data = new Object[][] {
                new Object[] {"TestName", "TestCastle", "12.04.1445", "Lorem Ipsum"},
                new Object[] {"TestName", "TestCastle", "12.04.1445", "Lorem Ipsum"},
                new Object[] {"TestName", "TestCastle", "12.04.1445", "Lorem Ipsum"},
                new Object[] {"TestName", "TestCastle", "12.04.1445", "Lorem Ipsum"}

        };

        private final String[] COLUMNS = new String[]{"Name", "Castle", "Born", "Heraldry"};

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
        model.getColumn(0).setPreferredWidth(100);
        model.getColumn(1).setPreferredWidth(100);
        model.getColumn(2).setPreferredWidth(90);
        model.getColumn(3).setPreferredWidth(130);

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

}
