package fi.muni.pv168.ui.table.model;

import fi.muni.pv168.Match;
import fi.muni.pv168.ui.table.TableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic table model to display Matches in table.
 *
 * Created by daemontus on 13/05/14.
 */
public class MatchTableModel extends AbstractTableModel {

    final static Logger logger = LoggerFactory.getLogger(MatchTableModel.class);

    private final static ArrayList<TableCell<Match>> columnMapping = new ArrayList<TableCell<Match>>();

    static {
        columnMapping.add(new TableCell<Match>() {
            @Override
            public Object getProperty(Match value) {
                return value.getKnight().getName();
            }

            @Override
            public String getColumnName() {
                return "knight";
            }
        });
        columnMapping.add(new TableCell<Match>() {
            @Override
            public Object getProperty(Match value) {
                return value.getDiscipline().getName();
            }

            @Override
            public String getColumnName() {
                return "discipline";
            }
        });
        columnMapping.add(new TableCell<Match>() {
            @Override
            public Object getProperty(Match value) {
                return value.getStartNumber();
            }

            @Override
            public String getColumnName() {
                return "start_number";
            }
        });
        columnMapping.add(new TableCell<Match>() {
            @Override
            public Object getProperty(Match value) {
                return value.getPoints();
            }

            @Override
            public String getColumnName() {
                return "points";
            }
        });
    }

    private List<Match> data = new ArrayList<Match>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnMapping.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (row < 0 || row > data.size()) {
            String description = "Trying to display invalid row: "+row+" data size: "+data.size();
            logger.error(description);
            throw new RuntimeException(description);
        }
        if (col < 0 || col > columnMapping.size()) {
            String description = "Trying to display invalid column: "+col+" num of columns: "+columnMapping.size();
            logger.error(description);
            throw new RuntimeException(description);
        }
        return columnMapping.get(col).getProperty(data.get(row));
    }

    @Override
    public String getColumnName(int col) {
        if (col < 0 || col > columnMapping.size()) {
            String description = "Trying to display invalid column: "+col+" num of columns: "+columnMapping.size();
            logger.error(description);
            throw new RuntimeException(description);
        }
        return columnMapping.get(col).getColumnName();
    }

    public void addMatch(Match value) {
        if (value == null) {
            throw new NullPointerException("Cannot add null Matches to table.");
        }
        data.add(value);
        int lastRow = data.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void removeMatch(Match value) {
        if (value == null) {
            throw new NullPointerException("Cannot delete null Match.");
        }
        int i;
        for (i=0; i<data.size(); i++) {
            if (value.equals(data.get(i))) {
                data.remove(i);
                break;
            }
        }
        fireTableRowsDeleted(i, i);
    }

    public Match getMatch(int row) {
        if (row < 0 || row > data.size()) {
            String description = "Trying to get invalid row: "+row+" data size: "+data.size();
            logger.error(description);
            throw new RuntimeException(description);
        }
        return data.get(row);
    }

    public void refreshMatch(Match value) {
        if (value == null) {
            throw new NullPointerException("Cannot refresh Match to null.");
        }
        int i;
        for (i=0; i<data.size(); i++) {
            if (value.equals(data.get(i))) {
                data.set(i, value);
                break;
            }
        }
        fireTableRowsUpdated(i,i);
    }

    public void clear() {
        int size = data.size();
        data.clear();
        fireTableRowsDeleted(0, size-1);
    }

}
