package fi.muni.pv168.ui.table;

public interface TableCell<T> {
    public Object getProperty(T value);
    public String getColumnName();
}

