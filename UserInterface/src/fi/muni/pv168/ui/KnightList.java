package fi.muni.pv168.ui;

import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class KnightList {

    public static void main(String[] args) {

    }

    public KnightList() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setMinimumSize(new Dimension(750,355));
                frame.setBounds(0, 0, 800, 355);

                frame.setTitle("Knight Manager");

                frame.add(initTable());

                frame.add(initCreateForm(), BorderLayout.WEST);
                // Zobrazíme okno
                frame.setVisible(true);
            }
        });
    }

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

    private JComponent initTable() {
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
        pane.setPreferredSize(new Dimension(420, 300));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(pane, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        panel.add(new JButton("Edit"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(new JButton("Delete"), constraints);
        return panel;
    }

    private JPanel initCreateForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JTextField name = new JTextField();
        JTextField castle = new JTextField();
        JDatePanel born = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel()));
        JTextField heraldry = new JTextField();

        JLabel title = new JLabel("New Knight");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        constraints.insets = new Insets(10,10,10,10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(title, constraints);

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setLabelFor(name);
        JLabel castleLabel = new JLabel("Castle: ");
        castleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        castleLabel.setLabelFor(castle);
        JLabel bornLabel = new JLabel("Born: ");
        bornLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        bornLabel.setLabelFor((Component) born);
        JLabel heraldryLabel = new JLabel("Heraldry: ");
        heraldryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        heraldryLabel.setLabelFor(heraldry);

        JComponent[] objects = new JComponent[ ] {
                nameLabel, name, castleLabel, castle, bornLabel, (JComponent) born, heraldryLabel, heraldry
        };

        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.2;
        constraints.insets = new Insets(5, 5, 5, 5);
        for (JComponent c : objects) {
            panel.add(c, constraints);
            constraints.gridx++;
            constraints.weightx = 0.7;
            constraints.insets = new Insets(0,0,0,30);
            if (constraints.gridx == 2) {
                constraints.gridx = 0;
                constraints.gridy++;
                constraints.insets = new Insets(5,5,5,5);
                constraints.weightx = 0.2;
            }
        }

        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(10,10,10,10);
        constraints.weighty = 1;
        JButton button = new JButton("Create");
        panel.add(button, constraints);

        panel.setPreferredSize(new Dimension(300, 100));

        return panel;
    }

}
