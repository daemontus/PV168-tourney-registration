package fi.muni.pv168.ui.table.model;

import fi.muni.pv168.Knight;
import fi.muni.pv168.ui.table.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple table model to display Knights in table.
 *
 * @author Samuel Pastva
 */
public class KnightTableModel extends AbstractTableModel {

    final static Logger logger = LoggerFactory.getLogger(KnightTableModel.class);

    private final static ArrayList<TableColumn<Knight>> columnMapping = new ArrayList<TableColumn<Knight>>();

    static {
        columnMapping.add(new TableColumn<Knight>() {
            @Override
            public Object getProperty(Knight value) {
                return value.getName();
            }

            @Override
            public String getColumnName() {
                return "name";
            }
        });
        columnMapping.add(new TableColumn<Knight>() {
            @Override
            public Object getProperty(Knight value) {
                return value.getCastle();
            }

            @Override
            public String getColumnName() {
                return "castle";
            }
        });
        columnMapping.add(new TableColumn<Knight>() {
            @Override
            public Object getProperty(Knight value) {
                return value.getBorn().toString();
            }

            @Override
            public String getColumnName() {
                return "born";
            }
        });
        columnMapping.add(new TableColumn<Knight>() {
            @Override
            public Object getProperty(Knight value) {
                return value.getHeraldry();
            }

            @Override
            public String getColumnName() {
                return "heraldry";
            }
        });
    }

    private List<Knight> data = new ArrayList<Knight>();

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

    public void addKnight(Knight value) {
        if (value == null) {
            throw new NullPointerException("Cannot add null knights to table.");
        }
        data.add(value);
        int lastRow = data.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void removeKnight(Knight knight) {
        if (knight == null) {
            throw new NullPointerException("Cannot delete null knight.");
        }
        int i;
        for (i=0; i<data.size(); i++) {
            if (knight.equals(data.get(i))) {
                data.remove(i);
                break;
            }
        }
        fireTableRowsDeleted(i, i);
    }

    public Knight getKnight(int row) {
        if (row < 0 || row > data.size()) {
            String description = "Trying to get invalid row: "+row+" data size: "+data.size();
            logger.error(description);
            throw new RuntimeException(description);
        }
        return data.get(row);
    }

    public void refreshKnight(Knight knight) {
        if (knight == null) {
            throw new NullPointerException("Cannot refresh knight to null.");
        }
        int i;
        for (i=0; i<data.size(); i++) {
            if (knight.equals(data.get(i))) {
                data.set(i, knight);
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
