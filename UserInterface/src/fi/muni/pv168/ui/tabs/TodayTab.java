package fi.muni.pv168.ui.tabs;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.Match;
import fi.muni.pv168.ui.resources.ManagerFactory;
import fi.muni.pv168.ui.resources.Resources;
import fi.muni.pv168.ui.table.LocalizedCellHeader;
import fi.muni.pv168.ui.table.data.Result;
import fi.muni.pv168.ui.table.model.DisciplineTableModel;
import fi.muni.pv168.ui.table.model.ResultTableModel;
import fi.muni.pv168.utils.ServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class TodayTab implements Tab {


    private static final Logger logger = LoggerFactory.getLogger(KnightTab.class);

    private JPanel panel;

    private DisciplineTableModel disciplineTableModel;
    private ResultTableModel resultsTableModel;

    private JButton refresh;
    private JProgressBar loading;

    public TodayTab() {

        disciplineTableModel = new DisciplineTableModel();
        resultsTableModel = new ResultTableModel();

        initUi();

        loading.setIndeterminate(true);
        new LoadDisciplinesTable().execute();

    }

    private void initUi() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JTable content = new JTable(disciplineTableModel);
        JTable results = new JTable(resultsTableModel);
        content.getTableHeader().setDefaultRenderer(new LocalizedCellHeader(content));
        results.getTableHeader().setDefaultRenderer(new LocalizedCellHeader(results));
        results.setRowSelectionAllowed(false);
        content.setRowSelectionAllowed(false);
        content.setRowHeight(20);

        TableColumnModel model = content.getColumnModel();
        model.getColumn(0).setPreferredWidth(230);
        model.getColumn(1).setPreferredWidth(220);
        model.getColumn(2).setPreferredWidth(220);
        model.getColumn(3).setPreferredWidth(120);

        JLabel title = new JLabel(Resources.getString("today_disciplines"));
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        //title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(title);
        JScrollPane pane = new JScrollPane(content);
        pane.setPreferredSize(new Dimension(0,180));
        panel.add(pane);

        title = new JLabel(Resources.getString("results"));
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, (int) (title.getFont().getSize() * 1.5)));
        //title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(title);
        pane = new JScrollPane(results);
        pane.setPreferredSize(new Dimension(0,180));
        panel.add(pane);

        refresh = new JButton(Resources.getString("refresh"));
        refresh.setPreferredSize(new Dimension(100,30));
        refresh.addActionListener(refreshAction);
        //panel.add(refresh);

        loading = new JProgressBar();
        loading.setPreferredSize(new Dimension(300,30));
        JPanel bottom = new JPanel();
        bottom.add(loading, BorderLayout.LINE_START);
        bottom.add(refresh, BorderLayout.LINE_END);
        panel.add(bottom);

    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public JMenu getMenu() {
        return null;
    }

    @Override
    public String getTitleKey() {
        return "today";
    }

    public ActionListener refreshAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            disciplineTableModel.clear();
            resultsTableModel.clear();
            loading.setIndeterminate(true);
            new LoadDisciplinesTable().execute();
        }
    };


    private class LoadDisciplinesTable extends SwingWorker<Void, Discipline> {

        @Override
        protected Void doInBackground() {
            try {
                for (Discipline k : ManagerFactory.initDisciplineManager().getDisciplinesByDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()))) {
                    publish(k);
                }
            } catch (ServiceFailureException e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }

            return null;
        }

        @Override
        protected void process(java.util.List<Discipline> disciplines) {
            for (Discipline discipline : disciplines) {
                disciplineTableModel.addDiscipline(discipline);
            }
        }

        @Override
        protected void done() {
            new LoadResultsTable().execute();
            super.done();
        }
    }

    private class LoadResultsTable extends SwingWorker<Void, Result> {

        @Override
        protected Void doInBackground() {
            try {
                for (Match k : ManagerFactory.initMatchManager().findAllMatches()) {
                    if (k.getPoints() != null) {
                        publish(new Result(k.getKnight(), k.getDiscipline(), k.getPoints()));
                    }
                }
            } catch (ServiceFailureException e) {
                logger.error("Unexpected error while creating match", e);
                JOptionPane.showMessageDialog(null, Resources.getString("unexpected_error"));
            }

            return null;
        }

        @Override
        protected void process(java.util.List<Result> results) {
            for (Result result : results) {
                resultsTableModel.addResult(result);
            }
        }

        @Override
        protected void done() {
            loading.setIndeterminate(false);
            super.done();
        }
    }

}
