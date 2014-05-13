package fi.muni.pv168.ui.form;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.ui.resources.Resources;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class DisciplineForm {


    private static final int MILLIS_IN_DAY = 1000*60*60*24;

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

                frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(500, 355));
                frame.setBounds(0, 0, 500, 355);
                frame.setResizable(false);
                frame.setTitle(Resources.getString("discipline_editor"));

                frame.add(initCreateForm());

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

        Timestamp start = new Timestamp(Calendar.getInstance().getTime().getTime());
        Timestamp end = new Timestamp(Calendar.getInstance().getTime().getTime());
        if (editable != null) {
            name.setText(editable.getName());
            start = editable.getStart();
            end = editable.getEnd();
            startTime.getModel().setValue(new Date(start.getTime()));
            endTime.getModel().setValue(new Date(end.getTime()));
            maxParticipants.setText(String.valueOf(editable.getMaxParticipants()));
        }
        System.out.println(start.getTime()+" "+end.getTime());
        startDate = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel(new Date(start.getTime()))));
        endDate = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel(new Date(end.getTime()))));

        JLabel title = new JLabel(Resources.getString("discipline_editor"));
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        constraints.insets = new Insets(10,10,10,10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(title, constraints);

        JLabel nameLabel = new JLabel(Resources.getString("name"));
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setLabelFor(name);
        JLabel startDateLabel = new JLabel(Resources.getString("start_date"));
        startDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        startDateLabel.setLabelFor(startDate);
        JLabel startTimeLabel = new JLabel(Resources.getString("start_time"));
        startTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        startTimeLabel.setLabelFor(startTime);
        JLabel endDateLabel = new JLabel(Resources.getString("end_date"));
        endDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        endDateLabel.setLabelFor(endDate);
        JLabel endTimeLabel = new JLabel(Resources.getString("end_time"));
        endTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        endTimeLabel.setLabelFor(endTime);
        JLabel maxParticipantsLabel = new JLabel(Resources.getString("max_participants"));
        maxParticipantsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        maxParticipantsLabel.setLabelFor(maxParticipants);

        JComponent[] objects = new JComponent[ ] {
                nameLabel, name,
                startDateLabel, startDate, startTimeLabel, startTime,
                endDateLabel, endDate, endTimeLabel, endTime,
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
        JButton button = new JButton(Resources.getString("cancel"));
        button.addActionListener(cancel);
        panel.add(button, constraints);
        constraints.gridx = 1;
        constraints.weighty = 0.5;
        button = new JButton(Resources.getString("save"));
        button.addActionListener(submit);
        panel.add(button, constraints);

        panel.setPreferredSize(new Dimension(350, 100));

        return panel;
    }

    private static JSpinner buildTimeSpinner() {
        SpinnerModel model = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(model);
        JComponent editor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(editor);
        return timeSpinner;
    }


    ActionListener cancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (listener != null) {
                listener.onCancel();
            }
            frame.dispose();
        }
    };

    ActionListener submit = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            long start = ((Date) startDate.getModel().getValue()).getTime() + ((java.util.Date)startTime.getModel().getValue()).getTime() % MILLIS_IN_DAY;
            long end = ((Date) endDate.getModel().getValue()).getTime() + ((java.util.Date)endTime.getModel().getValue()).getTime() % MILLIS_IN_DAY;
            System.out.println(start+" "+end);
            //validate form
            if (name.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, Resources.getString("name_too_short"));
                return;
            }
            int maxParticipantsNum;
            try {
                maxParticipantsNum = Integer.parseInt(maxParticipants.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, Resources.getString("participants_not_num"));
                return;
            }
            if (start > end) {
                JOptionPane.showMessageDialog(null, Resources.getString("invalid_duration"));
                return;
            }
            if (editable != null) {
                editable.setName(name.getText());
                editable.setStart(new Timestamp(start));
                editable.setEnd(new Timestamp(end));
                editable.setMaxParticipants(maxParticipantsNum);
            } else {
                editable = new Discipline(null, name.getText(), new Timestamp(start), new Timestamp(end), maxParticipantsNum);
            }
            if (listener != null) {
                listener.onSubmit(editable);
            }
            frame.dispose();
        }
    };
}
