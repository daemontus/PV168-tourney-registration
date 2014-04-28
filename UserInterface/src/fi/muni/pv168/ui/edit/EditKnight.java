package fi.muni.pv168.ui.edit;

import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class EditKnight {

    public EditKnight() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                JFrame frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(320, 235));
                frame.setBounds(0, 0, 320, 235);
                frame.setResizable(false);
                frame.setTitle("Knight Editor");

                frame.add(initCreateForm());
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

        JTextField name = new JTextField();
        name.setText("Prefilled");
        JTextField castle = new JTextField();
        castle.setText("Prefilled");
        JDatePanel born = new JDatePickerImpl(new JDatePanelImpl(new SqlDateModel(new Date(0))));
        JTextField heraldry = new JTextField();
        heraldry.setText("Prefilled");

        JLabel title = new JLabel("Edit Knight");
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

        panel.setPreferredSize(new Dimension(320, 100));

        return panel;
    }

}
