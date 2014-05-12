package fi.muni.pv168.ui.table;

import fi.muni.pv168.ui.resources.Resources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class LocalizedCellHeader implements TableCellRenderer{

    DefaultTableCellRenderer renderer;

    public LocalizedCellHeader(JTable table) {
        renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String cell_content = Resources.getString((String) value);
        return renderer.getTableCellRendererComponent(table, cell_content, isSelected, hasFocus, row, col);
    }
}
