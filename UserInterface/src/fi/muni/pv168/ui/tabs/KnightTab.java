package fi.muni.pv168.ui.tabs;

import fi.muni.pv168.Knight;
import fi.muni.pv168.KnightManager;
import fi.muni.pv168.ui.form.FormResultListener;
import fi.muni.pv168.ui.form.KnightForm;
import fi.muni.pv168.ui.resources.ManagerFactory;
import fi.muni.pv168.ui.resources.Resources;
import fi.muni.pv168.ui.table.LocalizedCellHeader;
import fi.muni.pv168.ui.table.model.KnightTableModel;
import fi.muni.pv168.utils.ServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class KnightTab implements Tab {

    private static final Logger logger = LoggerFactory.getLogger(KnightTab.class);

    private final static int CREATE_MENU_POSITION = 0;
    private final static int EDIT_MENU_POSITION = 1;
    private final static int DELETE_MENU_POSITION = 2;

    private KnightTableModel tableModel;
    private KnightManager knightManager;

    private JPanel panel;
    private JTable table;
    private JButton deleteButton;
    private JButton editButton;
    private JMenu menu;
    private JProgressBar loading;

    public KnightTab() {

        tableModel = new KnightTableModel();
        knightManager = ManagerFactory.initKnightManager();

        initUi();

        initMenu();

        loading.setIndeterminate(true);
        new LoadTable().execute();

    }

    private void initMenu() {
        menu = new JMenu(Resources.getString("knights"));
        JMenuItem menuItem = new JMenuItem(Resources.getString("new_knight"));
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

        //init layout
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;

        //init table
        table = new JTable(tableModel);
        table.getTableHeader().setDefaultRenderer(new LocalizedCellHeader(table));
        table.setRowHeight(20);

        TableColumnModel model = table.getColumnModel();

        //selection listener
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

        //progressbar
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

        //edit button
        editButton = new JButton(Resources.getString("edit"));
        editButton.addActionListener(editAction);
        editButton.setEnabled(false);
        panel.add(editButton, constraints);

        constraints.gridx = 2;

        //delete button
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
        return "knights";
    }

    private ActionListener deleteAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                new DeleteKnight(tableModel.getKnight(selected)).execute();
            }
        }
    };

    private ActionListener editAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                new KnightForm(tableModel.getKnight(selected), editListener);
            }
        }
    };

    private ActionListener createAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new KnightForm(createListener);
        }
    };

    FormResultListener<Knight> editListener = new FormResultListener<Knight>() {
        @Override
        public void onSubmit(Knight data) {
            loading.setIndeterminate(true);
            table.clearSelection();
            new EditKnight(data).execute();
        }

        @Override
        public void onCancel() {
            table.clearSelection();
        }
    };

    FormResultListener<Knight> createListener = new FormResultListener<Knight>() {
        @Override
        public void onSubmit(Knight data) {
            loading.setIndeterminate(true);
            table.clearSelection();
            new CreateKnight(data).execute();
        }

        @Override
        public void onCancel() {
            table.clearSelection();
        }
    };

    private class LoadTable extends SwingWorker<Void, Knight> {

        @Override
        protected Void doInBackground() {
            try {
                for (Knight k : knightManager.findAllKnights()) {
                    publish(k);
                }
            } catch (ServiceFailureException e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }

            return null;
        }

        @Override
        protected void process(List<Knight> knights) {
            for (Knight knight : knights) {
                tableModel.addKnight(knight);
            }
        }

        @Override
        protected void done() {
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class DeleteKnight extends SwingWorker<Void, Void> {

        private Knight victim;

        private DeleteKnight(Knight victim) {
            this.victim = victim;
        }

        @Override
        protected Void doInBackground() {
            try {
                knightManager.deleteKnight(victim);
            } catch (ServiceFailureException e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }
            return null;
        }

        @Override
        protected void done() {
            tableModel.removeKnight(victim);
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class EditKnight extends SwingWorker<Void, Void> {

        private Knight updated;

        private EditKnight(Knight victim) {
            this.updated = victim;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                knightManager.updateKnight(updated);
            } catch (ServiceFailureException e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }
            return null;
        }

        @Override
        protected void done() {
            tableModel.refreshKnight(updated);
            loading.setIndeterminate(false);
            super.done();
        }
    }

    private class CreateKnight extends SwingWorker<Void, Void> {

        private Knight toCreate;

        private CreateKnight(Knight toCreate) {
            this.toCreate = toCreate;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                knightManager.createKnight(toCreate);
            } catch (ServiceFailureException e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }
            return null;
        }

        @Override
        protected void done() {
            tableModel.addKnight(toCreate);
            loading.setIndeterminate(false);
            super.done();
        }
    }


}
