package fi.muni.pv168.ui.form;

import fi.muni.pv168.Match;
import fi.muni.pv168.ui.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MatchForm {

    private Match editable;

    private FormResultListener<Match> listener;

    private JComboBox knight;
    private JComboBox discipline;
    private JTextField startNum;
    private JTextField points;

    private JFrame frame;

    public MatchForm(FormResultListener<Match> listener) {
        this(null, listener);
    }

    public MatchForm(Match editable, FormResultListener<Match> listener) {
        this.editable = editable;
        this.listener = listener;

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(320, 235));
                frame.setBounds(0, 0, 320, 235);
                frame.setResizable(false);
                frame.setTitle(Resources.getString("match_editor"));

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

        knight = new JComboBox();
        discipline = new JComboBox();
        startNum = new JTextField();
        points = new JTextField();

        JLabel title = new JLabel(Resources.getString("match_editor"));
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        constraints.insets = new Insets(10,10,10,10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(title, constraints);

        JLabel knightLabel = new JLabel(Resources.getString("knight"));
        knightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        knightLabel.setLabelFor(knight);
        JLabel disciplineLabel = new JLabel(Resources.getString("discipline"));
        disciplineLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        disciplineLabel.setLabelFor(discipline);
        JLabel startNumLabel = new JLabel(Resources.getString("start_num"));
        startNumLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        startNumLabel.setLabelFor(startNum);
        JLabel pointsLabel = new JLabel(Resources.getString("points"));
        pointsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pointsLabel.setLabelFor(points);

        JComponent[] objects = new JComponent[ ] {
                knightLabel, knight, disciplineLabel, discipline, startNumLabel, startNum, pointsLabel, points
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
        constraints.gridwidth = 1;
        constraints.insets = new Insets(10,10,10,10);
        constraints.weighty = 1;
        constraints.weightx = 0.5;
        JButton button = new JButton(Resources.getString("save"));
        button.addActionListener(submit);
        panel.add(button, constraints);
        constraints.gridx = 1;
        constraints.weighty = 0.5;
        button = new JButton(Resources.getString("cancel"));
        button.addActionListener(cancel);
        panel.add(button, constraints);

        panel.setPreferredSize(new Dimension(320, 100));

        return panel;
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
            if (editable != null) {
                //TODO: solve this freaking awesome ComboBoxesssss!
                    /*editable.setKnigh(editable.getKnight());
                    editable.setDiscipline(castle.getText());*/
                editable.setStartNumber(Integer.parseInt(startNum.getText()));
                editable.setPoints(Integer.parseInt(points.getText()));
            } else {
                //TODO: same here
                //editable = new Match (null, name.getText(), castle.getText(), (Date) born.getModel().getValue(), heraldry.getText());
            }
            if (listener != null) {
                listener.onSubmit(editable);
            }
            frame.dispose();
        }
    };
}
