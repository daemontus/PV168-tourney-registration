package fi.muni.pv168.ui.form;

import fi.muni.pv168.Discipline;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Timestamp;

public class DisciplineForm {

    private Discipline editable;

    private FormResultListener<Discipline> listener;

    private JTextField name;
    private JDatePickerImpl startDate;
    private JDatePickerImpl endDate;
    private JSpinner startTime;
    private JSpinner endTime;
    private JTextField maxParticipants;

    private JFrame frame;

    public DisciplineForm(FormResultListener<Discipline> listener) {
        this(null, listener);
    }
    public DisciplineForm(Discipline editable, FormResultListener<Discipline> listener) {
        this.editable = editable;
        this.listener = listener;

        EventQueue.invokeLater(new Runnable() {
            public void run() {

                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(500, 355));
                frame.setBounds(0, 0, 500, 355);
                frame.setResizable(false);
                frame.setTitle("Discipline Editor");

                frame.add(initCreateForm(), BorderLayout.CENTER);
                // Zobrazíme okno
                frame.setVisible(true);
            }
        });
    }

    private JPanel initCreateForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        name = new JTextField();
        startTime = buildTimeSpinner();
        endTime = buildTimeSpinner();
        maxParticipants = new JTextField();

        Timestamp start = new Timestamp(0);
        Timestamp end = new Timestamp(1);
        if (editable != null) {
            name.setText(editable.getName());
            start = editable.getStart();
            end = editable.getEnd();
            maxParticipants.setText(String.valueOf(editable.getMaxParticipants()));
        }
        startDate = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel(new Date(start.getTime()))));
        endDate = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel(new Date(end.getTime()))));

        JLabel title = new JLabel("Edit Discipline");
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
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10,10,10,10);
        constraints.weighty = 1;
        constraints.weightx = 0.5;
        JButton button = new JButton("Cancel");
        panel.add(button, constraints);
        constraints.gridx = 1;
        constraints.weighty = 0.5;
        button = new JButton("Save");
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
