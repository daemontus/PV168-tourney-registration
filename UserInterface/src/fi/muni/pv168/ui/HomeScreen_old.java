package fi.muni.pv168.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class HomeScreen_old {

    public HomeScreen_old() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(810, 715));
                frame.setBounds(0, 0, 810, 715);

                frame.setTitle("Knight Manager");

                frame.add(initTable(), BorderLayout.NORTH);

                // Zobrazíme okno
                frame.setVisible(true);
            }
        });
    }

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

    private JComponent initTable() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        JTable content = new JTable(disciplineTableModel);
        JTable results = new JTable(resultsTableModel);
        results.setRowSelectionAllowed(false);
        content.setRowSelectionAllowed(false);
        content.setRowHeight(20);

        TableColumnModel model = content.getColumnModel();
        content.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.getColumn(0).setPreferredWidth(230);
        model.getColumn(1).setPreferredWidth(220);
        model.getColumn(2).setPreferredWidth(220);
        model.getColumn(3).setPreferredWidth(120);


        constraints.gridwidth = 3;
        JLabel title = new JLabel("Today's disciplines:");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        constraints.insets = new Insets(10,10,10,10);
        panel.add(title, constraints);
        constraints.insets = new Insets(0,0,0,0);
        JScrollPane pane = new JScrollPane(content);
        pane.setPreferredSize(new Dimension(750, 250));
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(pane, constraints);

        title = new JLabel("Today's results:");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(10,10,10,10);
        panel.add(title, constraints);
        constraints.insets = new Insets(0,0,0,0);
        pane = new JScrollPane(results);
        pane.setPreferredSize(new Dimension(750, 250));
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(pane, constraints);

        constraints.gridwidth = 1;
        constraints.insets = new Insets(10,10,10,10);
        JButton button = new JButton("Knight manager");
        button.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.2)));
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.ipady = 50;
        panel.add(button, constraints);
        button = new JButton("Discipline manager");
        button.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.2)));
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.ipady = 50;
        panel.add(button, constraints);
        button = new JButton("Match manager");
        button.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.2)));
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.ipady = 50;
        panel.add(button, constraints);

        return panel;
    }

}
