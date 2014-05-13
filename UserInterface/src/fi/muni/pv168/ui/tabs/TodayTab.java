package fi.muni.pv168.ui.tabs;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class TodayTab implements Tab {

    private TableModel disciplineTableModel = new AbstractTableModel() {

        private final Object[][] data = new Object[][] {
                new Object[] {"TestDiscipline", "7:00 12.04.1445", "13:00 12.04.1445", "5"},
                new Object[] {"TestDiscipline", "7:00 12.04.1445", "13:00 12.04.1445", "5"},
                new Object[] {"TestDiscipline", "7:00 12.04.1445", "13:00 12.04.1445", "5"},
                new Object[] {"TestDiscipline", "7:00 12.04.1445", "13:00 12.04.1445", "5"},
                new Object[] {"TestDiscipline", "7:00 12.04.1445", "13:00 12.04.1445", "5"}
        };

        private final String[] COLUMNS = new String[]{"Name", "Start", "End", "Max. participants"};

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

    private TableModel resultsTableModel = new AbstractTableModel() {

        private final Object[][] data = new Object[][] {
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
                new Object[] {"TestKnight", "TestDiscipline", "12345"},
        };

        private final String[] COLUMNS = new String[]{"Knight", "Discipline", "Points"};

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
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JTable content = new JTable(disciplineTableModel);
        JTable results = new JTable(resultsTableModel);
        results.setRowSelectionAllowed(false);
        content.setRowSelectionAllowed(false);
        content.setRowHeight(20);

        TableColumnModel model = content.getColumnModel();
        model.getColumn(0).setPreferredWidth(230);
        model.getColumn(1).setPreferredWidth(220);
        model.getColumn(2).setPreferredWidth(220);
        model.getColumn(3).setPreferredWidth(120);

        JLabel title = new JLabel("Upcoming disciplines:");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        //title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(title);
        JScrollPane pane = new JScrollPane(content);
        pane.setPreferredSize(new Dimension(0,200));
        panel.add(pane);

        title = new JLabel("Today's results:");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        //title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(title);
        pane = new JScrollPane(results);
        pane.setPreferredSize(new Dimension(0,200));
        panel.add(pane);

        return panel;
    }

    @Override
    public JMenu getMenu() {
        return null;
    }

    @Override
    public String getTitleKey() {
        return "today";
    }

}
