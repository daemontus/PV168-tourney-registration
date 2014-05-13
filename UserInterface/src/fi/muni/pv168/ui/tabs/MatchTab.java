package fi.muni.pv168.ui.tabs;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.Knight;
import fi.muni.pv168.Match;
import fi.muni.pv168.MatchManager;
import fi.muni.pv168.ui.form.FormResultListener;
import fi.muni.pv168.ui.form.MatchForm;
import fi.muni.pv168.ui.resources.ManagerFactory;
import fi.muni.pv168.ui.resources.Resources;
import fi.muni.pv168.ui.table.LocalizedCellHeader;
import fi.muni.pv168.ui.table.model.MatchTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MatchTab implements Tab {


    private static final Logger logger = LoggerFactory.getLogger(MatchTab.class);

    private final static int CREATE_MENU_POSITION = 0;
    private final static int EDIT_MENU_POSITION = 1;
    private final static int DELETE_MENU_POSITION = 2;


    private MatchTableModel tableModel;
    private MatchManager matchManager;

    private JPanel panel;
    private JTable table;
    private JButton deleteButton;
    private JButton editButton;
    private JMenu menu;
    private JProgressBar loading;

    private MatchForm.KnightComboBoxModel knightComboBoxModel;
    private MatchForm.DisciplineComboBoxModel disciplineComboBoxModel;

    public MatchTab() {

        tableModel = new MatchTableModel();
        matchManager = ManagerFactory.initMatchManager();
        knightComboBoxModel = new MatchForm.KnightComboBoxModel(new ArrayList<Knight>());
        disciplineComboBoxModel = new MatchForm.DisciplineComboBoxModel(new ArrayList<Discipline>());

        initUi();

        initMenu();

        new LoadData().execute();

    }

    private void initMenu() {
        menu = new JMenu(Resources.getString("matches"));
        JMenuItem menuItem = new JMenuItem(Resources.getString("new_match"));
        menu.add(menuItem);

        menuItem = new JMenuItem(Resources.getString("edit_selected"));
        menuItem.setEnabled(false);
        menu.add(menuItem);

        menuItem = new JMenuItem(Resources.getString("delete_selected"));
        menuItem.setEnabled(false);
        menu.add(menuItem);

        menu.getItem(DELETE_MENU_POSITION).addActionListener(deleteAction);
        menu.getItem(CREATE_MENU_POSITION).addActionListener(createAction);
        menu.getItem(EDIT_MENU_POSITION).addActionListener(editAction);
    }

    private void initUi() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;

        table = new JTable(tableModel);

        table.getTableHeader().setDefaultRenderer(new LocalizedCellHeader(table));

        table.setRowHeight(20);

        TableColumnModel model = table.getColumnModel();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                boolean enabled = table.getSelectedRow() != -1;
                menu.getItem(EDIT_MENU_POSITION).setEnabled(enabled);
                menu.getItem(DELETE_MENU_POSITION).setEnabled(enabled);
                deleteButton.setEnabled(enabled);
                editButton.setEnabled(enabled);
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.getColumn(0).setPreferredWidth(100);
        model.getColumn(1).setPreferredWidth(100);
        model.getColumn(2).setPreferredWidth(90);
        model.getColumn(3).setPreferredWidth(130);

        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(420, 455));

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
        loading = new JProgressBar();
        panel.add(loading, constraints);


        constraints.insets = new Insets(0,0,0,0);
        constraints.gridx = 1;

        editButton = new JButton(Resources.getString("edit"));
        editButton.addActionListener(editAction);
        editButton.setEnabled(false);
        panel.add(editButton, constraints);

        constraints.gridx = 2;

        deleteButton = new JButton(Resources.getString("delete"));
        deleteButton.addActionListener(deleteAction);
        deleteButton.setEnabled(false);
        panel.add(deleteButton, constraints);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public JMenu getMenu() {
        return menu;
    }

    @Override
    public String getTitleKey() {
        return "matches";
    }

    private ActionListener deleteAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                new DeleteMatch(tableModel.getMatch(selected)).execute();
            }
        }
    };

    private ActionListener editAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                new MatchForm(tableModel.getMatch(selected), editListener, knightComboBoxModel, disciplineComboBoxModel);
            }
        }
    };

    private ActionListener createAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new MatchForm(createListener, knightComboBoxModel, disciplineComboBoxModel);
        }
    };

    FormResultListener<Match> editListener = new FormResultListener<Match>() {
        @Override
        public void onSubmit(Match data) {
            loading.setIndeterminate(true);
            new EditMatch(data).execute();
            table.clearSelection();
        }

        @Override
        public void onCancel() {
            table.clearSelection();
        }
    };

    FormResultListener<Match> createListener = new FormResultListener<Match>() {
        @Override
        public void onSubmit(Match data) {
            loading.setIndeterminate(true);
            new CreateMatch(data).execute();
            table.clearSelection();
        }

        @Override
        public void onCancel() {
            table.clearSelection();
        }
    };

    private class LoadData extends SwingWorker<Void, Match> {

        @Override
        protected Void doInBackground() throws Exception {
            try {
                knightComboBoxModel = new MatchForm.KnightComboBoxModel(ManagerFactory.initKnightManager().findAllKnights());
                disciplineComboBoxModel = new MatchForm.DisciplineComboBoxModel(ManagerFactory.initDisciplineManager().findAllDisciplines());
                for (Match k : matchManager.findAllMatches()) {
                    publish(k);
                }
            } catch (Exception e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }
            return null;
        }

        @Override
        protected void process(java.util.List<Match> matches) {
            for (Match match : matches) {
                tableModel.addMatch(match);
            }
        }

        @Override
        protected void done() {
            super.done();
            loading.setIndeterminate(false);
        }
    }

    private class DeleteMatch extends SwingWorker<Void, Void> {

        private Match victim;

        private DeleteMatch(Match victim) {
            this.victim = victim;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                matchManager.deleteMatch(victim);
            } catch (Exception e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }
            return null;
        }

        @Override
        protected void done() {
            tableModel.removeMatch(victim);
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class EditMatch extends SwingWorker<Void, Void> {

        private Match updated;

        private EditMatch(Match victim) {
            this.updated = victim;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                matchManager.updateMatch(updated);
            } catch (Exception e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }
            return null;
        }

        @Override
        protected void done() {
            tableModel.refreshMatch(updated);
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class CreateMatch extends SwingWorker<Void, Void> {

        private Match toCreate;

        private CreateMatch(Match victim) {
            this.toCreate = victim;
        }

        @Override
        protected Void doInBackground() {
            try {
                matchManager.createMatch(toCreate);
            } catch (Exception e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }
            return null;
        }

        @Override
        protected void done() {
            tableModel.addMatch(toCreate);
            loading.setIndeterminate(false);
            super.done();
        }
    }
}
