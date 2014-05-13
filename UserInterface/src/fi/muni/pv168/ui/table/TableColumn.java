package fi.muni.pv168.ui.table;

/**
 * Represents one table cell in column mapping.
 * @param <T> Type of object that is displayed by table.
 */
public interface TableColumn<T> {
    /**
     * Get property value for this column.
     * @param value original object to display
     * @return property of object that should be displayed in this column
     */
    public Object getProperty(T value);

    /**
     * @return Column name (or resource key) that should be displayed in table header.
     */
    public String getColumnName();
}

