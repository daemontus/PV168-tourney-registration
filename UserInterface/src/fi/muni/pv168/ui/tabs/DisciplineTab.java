package fi.muni.pv168.ui.tabs;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.DisciplineManager;
import fi.muni.pv168.ui.form.DisciplineForm;
import fi.muni.pv168.ui.form.FormResultListener;
import fi.muni.pv168.ui.resources.ManagerFactory;
import fi.muni.pv168.ui.resources.Resources;
import fi.muni.pv168.ui.table.LocalizedCellHeader;
import fi.muni.pv168.ui.table.model.DisciplineTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisciplineTab implements Tab {

    private final static int CREATE_MENU_POSITION = 0;
    private final static int EDIT_MENU_POSITION = 1;
    private final static int DELETE_MENU_POSITION = 2;


    private DisciplineTableModel tableModel;
    private DisciplineManager disciplineManager;

    private JPanel panel;
    private JTable table;
    private JButton deleteButton;
    private JButton editButton;
    private JMenu menu;
    private JProgressBar loading;

    public DisciplineTab() {

        tableModel = new DisciplineTableModel();
        disciplineManager = ManagerFactory.initDisciplineManager();

        initUi();

        initMenu();

        loading.setIndeterminate(true);
        new LoadTable().execute();

    }

    private void initMenu() {
        menu = new JMenu(Resources.getString("disciplines"));
        JMenuItem menuItem = new JMenuItem(Resources.getString("new_discipline"));
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
        model.getColumn(0).setPreferredWidth(130);
        model.getColumn(1).setPreferredWidth(120);
        model.getColumn(2).setPreferredWidth(120);
        model.getColumn(3).setPreferredWidth(20);

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
        return "disciplines";
    }


    private ActionListener deleteAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                new DeleteDiscipline(tableModel.getDiscipline(selected)).execute();
            }
        }
    };

    private ActionListener editAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                new DisciplineForm(tableModel.getDiscipline(selected), editListener);
            }
        }
    };

    private ActionListener createAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new DisciplineForm(createListener);
        }
    };

    FormResultListener<Discipline> editListener = new FormResultListener<Discipline>() {
        @Override
        public void onSubmit(Discipline data) {
            loading.setIndeterminate(true);
            new EditDiscipline(data).execute();
            table.clearSelection();
        }

        @Override
        public void onCancel() {
            table.clearSelection();
        }
    };

    FormResultListener<Discipline> createListener = new FormResultListener<Discipline>() {
        @Override
        public void onSubmit(Discipline data) {
            loading.setIndeterminate(true);
            new CreateDiscipline(data).execute();
            table.clearSelection();
        }

        @Override
        public void onCancel() {
            table.clearSelection();
        }
    };

    private class LoadTable extends SwingWorker<Void, Discipline> {

        @Override
        protected Void doInBackground() throws InterruptedException {
            for (Discipline k : disciplineManager.findAllDisciplines()) {
                publish(k);
                Thread.sleep(1000);
            }
            return null;
        }

        @Override
        protected void process(java.util.List<Discipline> disciplines) {
            for (Discipline discipline : disciplines) {
                tableModel.addDiscipline(discipline);
            }
        }

        @Override
        protected void done() {
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class DeleteDiscipline extends SwingWorker<Void, Void> {

        private Discipline victim;

        private DeleteDiscipline(Discipline victim) {
            this.victim = victim;
        }

        @Override
        protected Void doInBackground() throws Exception {
            disciplineManager.deleteDiscipline(victim);
            return null;
        }

        @Override
        protected void done() {
            tableModel.removeDiscipline(victim);
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class EditDiscipline extends SwingWorker<Void, Void> {

        private Discipline updated;

        private EditDiscipline(Discipline victim) {
            this.updated = victim;
        }

        @Override
        protected Void doInBackground() throws Exception {
            disciplineManager.updateDiscipline(updated);
            return null;
        }

        @Override
        protected void done() {
            tableModel.refreshDiscipline(updated);
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class CreateDiscipline extends SwingWorker<Void, Void> {

        private Discipline toCreate;

        private CreateDiscipline(Discipline toCreate) {
            this.toCreate = toCreate;
        }

        @Override
        protected Void doInBackground() throws Exception {
            disciplineManager.createDiscipline(toCreate);
            return null;
        }

        @Override
        protected void done() {
            tableModel.addDiscipline(toCreate);
            loading.setIndeterminate(false);
            super.done();
        }
    }



}
