package fi.muni.pv168.ui.form;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.Knight;
import fi.muni.pv168.Match;
import fi.muni.pv168.ui.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchForm {

    private Match editable;

    private FormResultListener<Match> listener;

    private JComboBox knight;
    private JComboBox discipline;
    private JTextField startNum;
    private JTextField points;

    private JFrame frame;

    private KnightComboBoxModel knightModel;
    private DisciplineComboBoxModel disciplineModel;

    public MatchForm(FormResultListener<Match> listener, KnightComboBoxModel knightModel, DisciplineComboBoxModel disciplineModel) {
        this(null, listener, knightModel, disciplineModel);
    }

    public MatchForm(Match editable, FormResultListener<Match> listener, KnightComboBoxModel knightModel, DisciplineComboBoxModel disciplineModel) {
        this.editable = editable;
        this.listener = listener;
        this.knightModel = knightModel;
        this.disciplineModel = disciplineModel;

        EventQueue.invokeLater(new Runnable() {
            public void run() {

                frame = new JFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setMinimumSize(new Dimension(320, 235));
                frame.setBounds(0, 0, 320, 235);
                frame.setResizable(false);
                frame.setTitle(Resources.getString("match_editor"));

                frame.add(initCreateForm());
                //Display window
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
        knight.setModel(knightModel);
        discipline.setModel(disciplineModel);

        if (editable != null) {
            startNum.setText(String.valueOf(editable.getStartNumber()));
            if (editable.getPoints() != null) {
                points.setText(String.valueOf(editable.getPoints()));
            }
        }
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
        JLabel startNumLabel = new JLabel(Resources.getString("start_number"));
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
        JButton cancelButton = new JButton(Resources.getString("cancel"));
        cancelButton.addActionListener(cancel);
        panel.add(cancelButton, constraints);
        constraints.gridx = 1;
        constraints.weighty = 0.5;
        JButton submitButton = new JButton(Resources.getString("save"));
        submitButton.addActionListener(submit);
        panel.add(submitButton, constraints);

        panel.setPreferredSize(new Dimension(320, 100));

        frame.getRootPane().setDefaultButton(submitButton);
        submitButton.requestFocus();


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
                editable.setKnight(knightModel.getSelectedKnight());
                editable.setDiscipline(disciplineModel.getSelectedDiscipline());
                editable.setStartNumber(Integer.parseInt(startNum.getText()));
                if (points.getText().isEmpty()) {
                    editable.setPoints(null);
                } else {
                    editable.setPoints(Integer.parseInt(points.getText()));
                }
            } else {
                if (points.getText().isEmpty()) {
                    editable = new Match(
                            null,
                            knightModel.getSelectedKnight(),
                            disciplineModel.getSelectedDiscipline(),
                            Integer.valueOf(startNum.getText()),
                            null);
                } else {
                    editable = new Match(
                            null,
                            knightModel.getSelectedKnight(),
                            disciplineModel.getSelectedDiscipline(),
                            Integer.valueOf(startNum.getText()),
                            Integer.valueOf(points.getText()));
                }
            }
            if (listener != null) {
                listener.onSubmit(editable);
            }
            frame.dispose();
        }
    };

    public static class KnightComboBoxModel extends AbstractListModel implements ComboBoxModel {

        java.util.List<Knight> objects;
        private Map<String, Integer> namesToIndexes = new HashMap<String, Integer>();
        Knight selectedObject;

        public KnightComboBoxModel(java.util.List<Knight> knights) {
            objects = new ArrayList<Knight>(knights);
            for (int i=0; i<objects.size(); i++) {
                namesToIndexes.put(objects.get(i).getName(), i);
            }
        }

        @Override
        public void setSelectedItem(Object anObject) {
            if ((selectedObject != null && !selectedObject.equals(anObject))
                    || selectedObject == null && anObject != null) {
                selectedObject = objects.get(namesToIndexes.get((String) anObject));
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override
        public Object getSelectedItem() {
            if (selectedObject != null) {
                return selectedObject.getName();
            } else {
                return null;
            }
        }

        @Override
        public int getSize() {
            return objects.size();
        }

        @Override
        public String getElementAt(int index) {
            if (index >= 0 && index < objects.size()) {
                return objects.get(index).getName();
            } else {
                return null;
            }
        }

        public Knight getSelectedKnight() {
            return selectedObject;
        }

    }

    public static class DisciplineComboBoxModel extends AbstractListModel implements ComboBoxModel {

        java.util.List<Discipline> objects;
        private Map<String, Integer> namesToIndexes = new HashMap<String, Integer>();
        Discipline selectedObject;

        public DisciplineComboBoxModel(java.util.List<Discipline> disciplines) {
            objects = new ArrayList<Discipline>(disciplines);
            for (int i=0; i<objects.size(); i++) {
                namesToIndexes.put(objects.get(i).getName(), i);
            }
        }

        @Override
        public void setSelectedItem(Object anObject) {
            if ((selectedObject != null && !selectedObject.equals(anObject))
                    || selectedObject == null && anObject != null) {
                selectedObject = objects.get(namesToIndexes.get((String) anObject));
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override
        public Object getSelectedItem() {
            if (selectedObject != null) {
                return selectedObject.getName();
            } else {
                return null;
            }
        }

        @Override
        public int getSize() {
            return objects.size();
        }

        @Override
        public String getElementAt(int index) {
            if (index >= 0 && index < objects.size()) {
                return objects.get(index).getName();
            } else {
                return null;
            }
        }

        public Discipline getSelectedDiscipline() {
            return selectedObject;
        }

    }

 }
