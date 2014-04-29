package fi.muni.pv168.ui;

import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class DisciplineList {


    public DisciplineList() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(770, 355));
                frame.setBounds(0, 0, 800, 355);

                frame.setTitle("Discipline Manager");

                frame.add(initTable());

                frame.add(initCreateForm(), BorderLayout.WEST);
                // Zobrazíme okno
                frame.setVisible(true);
            }
        });
    }

    private TableModel knightTableModel = new AbstractTableModel() {

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
        model.getColumn(0).setPreferredWidth(130);
        model.getColumn(1).setPreferredWidth(120);
        model.getColumn(2).setPreferredWidth(120);
        model.getColumn(3).setPreferredWidth(20);


        JScrollPane pane = new JScrollPane(content);
        pane.setPreferredSize(new Dimension(420, 300));
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

    private JPanel initCreateForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JTextField name = new JTextField();
        JDatePicker startDate = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel()));
        JDatePicker endDate = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel()));
        JSpinner startTime = buildTimeSpinner();
        JSpinner endTime = buildTimeSpinner();
        JTextField maxParticipants = new JTextField();

        JLabel title = new JLabel("New Discipline");
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
        JLabel startDateLabel = new JLabel("Start date: ");
        startDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        startDateLabel.setLabelFor((Component) startDate);
        JLabel startTimeLabel = new JLabel("Start time: ");
        startTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        startTimeLabel.setLabelFor(startTime);
        JLabel endDateLabel = new JLabel("End date: ");
        endDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        endDateLabel.setLabelFor((Component) endDate);
        JLabel endTimeLabel = new JLabel("End time: ");
        endTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        endTimeLabel.setLabelFor(endTime);
        JLabel maxParticipantsLabel = new JLabel("Max. participants: ");
        maxParticipantsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        maxParticipantsLabel.setLabelFor(maxParticipants);

        JComponent[] objects = new JComponent[ ] {
                nameLabel, name,
                startDateLabel, (JComponent) startDate, startTimeLabel, startTime,
                endDateLabel, (JComponent) endDate, endTimeLabel, endTime,
                maxParticipantsLabel, maxParticipants
        };

        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.2;
        constraints.insets = new Insets(10, 10, 10, 10);
        for (JComponent c : objects) {
            panel.add(c, constraints);
            constraints.gridx++;
            constraints.weightx = 0.7;
            constraints.insets = new Insets(0,0,0,30);
            if (constraints.gridx == 2) {
                constraints.gridx = 0;
                constraints.gridy++;
                constraints.insets = new Insets(10,10,10,10);
                constraints.weightx = 0.2;
            }
        }

        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(10,10,10,10);
        constraints.weighty = 1;
        JButton button = new JButton("Create");
        panel.add(button, constraints);

        panel.setPreferredSize(new Dimension(350, 100));

        return panel;
    }

    public static JSpinner buildTimeSpinner() {
        SpinnerModel model = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(model);
        JComponent editor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(editor);
        return timeSpinner;
    }

}
