package fi.muni.pv168.ui.edit;

import javax.swing.*;
import java.awt.*;

public class EditMatch {

    public EditMatch() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(300, 225));
                frame.setBounds(0, 0, 300, 225);
                frame.setResizable(false);
                frame.setTitle("Match Editor");

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

        JComboBox knight = new JComboBox(new String[] {"TestKnight", "Knight1", "Knight2"});
        JComboBox discipline = new JComboBox(new String[] {"TestDiscipline", "Discipline"});
        JTextField startNum = new JTextField();
        startNum.setText("10");
        JTextField points = new JTextField();
        points.setText("465");

        JLabel title = new JLabel("Edit Match");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        constraints.insets = new Insets(10,10,10,10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(title, constraints);

        JLabel knightLabel = new JLabel("Knight: ");
        knightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        knightLabel.setLabelFor(knight);
        JLabel disciplineLabel = new JLabel("Discipline: ");
        disciplineLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        disciplineLabel.setLabelFor(discipline);
        JLabel startNumLabel = new JLabel("Starting number: ");
        startNumLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        startNumLabel.setLabelFor(startNum);
        JLabel pointsLabel = new JLabel("Points: ");
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
        JButton button = new JButton("Cancel");
        panel.add(button, constraints);
        constraints.gridx = 1;
        constraints.weighty = 0.5;
        button = new JButton("Save");
        panel.add(button, constraints);

        panel.setPreferredSize(new Dimension(300, 100));

        return panel;
    }

}
