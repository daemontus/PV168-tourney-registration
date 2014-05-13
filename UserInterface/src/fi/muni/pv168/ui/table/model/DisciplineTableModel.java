package fi.muni.pv168.ui.table.model;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.ui.table.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple table model to display disciplines in JTable
 *
 */
public class DisciplineTableModel extends AbstractTableModel {

    final static Logger logger = LoggerFactory.getLogger(DisciplineTableModel.class);

    private final static ArrayList<TableColumn<Discipline>> columnMapping = new ArrayList<TableColumn<Discipline>>();

    static {
        columnMapping.add(new TableColumn<Discipline>() {
            @Override
            public Object getProperty(Discipline value) {
                return value.getName();
            }

            @Override
            public String getColumnName() {
                return "name";
            }
        });
        columnMapping.add(new TableColumn<Discipline>() {
            @Override
            public Object getProperty(Discipline value) {
                return value.getStart().toString();
            }

            @Override
            public String getColumnName() {
                return "start";
            }
        });
        columnMapping.add(new TableColumn<Discipline>() {
            @Override
            public Object getProperty(Discipline value) {
                return value.getEnd().toString();
            }

            @Override
            public String getColumnName() {
                return "end";
            }
        });
        columnMapping.add(new TableColumn<Discipline>() {
            @Override
            public Object getProperty(Discipline value) {
                return value.getMaxParticipants();
            }

            @Override
            public String getColumnName() {
                return "max_participants";
            }
        });
    }

    private List<Discipline> data = new ArrayList<Discipline>();

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

    public void addDiscipline(Discipline value) {
        if (value == null) {
            throw new NullPointerException("Cannot add null disciplines to table.");
        }
        data.add(value);
        int lastRow = data.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void removeDiscipline(Discipline value) {
        if (value == null) {
            throw new NullPointerException("Cannot delete null discipline.");
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

    public Discipline getDiscipline(int row) {
        if (row < 0 || row > data.size()) {
            String description = "Trying to get invalid row: "+row+" data size: "+data.size();
            logger.error(description);
            throw new RuntimeException(description);
        }
        return data.get(row);
    }

    public void refreshDiscipline(Discipline value) {
        if (value == null) {
            throw new NullPointerException("Cannot refresh discipline to null.");
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
