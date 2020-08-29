package vip.youwe.shell.core.ui.component;

import vip.youwe.shell.core.ui.imp.ActionDblClick;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Vector;


public class DataView extends JTable {

    private static final long serialVersionUID = -8531006713898868252L;
    private JPopupMenu rightClickMenu;
    private RightClickEvent rightClickEvent;
    private int imgColumn;

    private void initJtableConfig() {
        this.rightClickEvent = new RightClickEvent(this.rightClickMenu, this);
        addMouseListener(this.rightClickEvent);
        setSelectionMode(0);
        setAutoCreateRowSorter(true);
        setRowHeight(25);
    }

    public DataView(Vector rowData, Vector columnNames, int imgColumn, int imgMaxWidth) {
        super(rowData, columnNames);
        initJtableConfig();
        this.imgColumn = imgColumn;
        if (imgColumn >= 0) {
            getColumnModel().getColumn(0).setMaxWidth(imgMaxWidth);
        }
    }

    public void setActionDblClick(ActionDblClick actionDblClick) {
        if (this.rightClickEvent != null) {
            this.rightClickEvent.setActionListener(actionDblClick);
        }
    }

    public JPopupMenu getRightClickMenu() {
        return this.rightClickMenu;
    }

    public void RemoveALL() {
        DefaultTableModel defaultTableModel = getModel();
        while (defaultTableModel.getRowCount() > 0) {
            defaultTableModel.removeRow(0);
        }
        updateUI();
    }


    public Class getColumnClass(int column) {
        return (column == this.imgColumn) ? javax.swing.Icon.class : Object.class;
    }


    public Vector GetSelectRow() {
        int select_row_id = getSelectedRow();
        if (select_row_id != -1) {
            int column_num = getColumnCount();
            Vector vector = new Vector();
            for (int i = 0; i < column_num; i++) {
                vector.add(this.getValueAt(select_row_id, i));
            }
            return vector;
        }
        return null;
    }


    public Vector getColumnVector() {
        Vector columnVector = null;
        try {
            DefaultTableModel tableModel = getModel();
            Field field = tableModel.getClass().getDeclaredField("columnIdentifiers");
            field.setAccessible(true);
            columnVector = (Vector) field.get(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnVector;
    }

    public String[] GetSelectRow1() {
        int select_row_id = getSelectedRow();
        if (select_row_id != -1) {
            int column_num = getColumnCount();
            String[] select_row_columns = new String[column_num];
            for (int i = 0; i < column_num; i++) {
                Object value = getValueAt(select_row_id, i);
                if (value instanceof String) {
                    select_row_columns[i] = (String) value;
                }
            }
            return select_row_columns;
        }
        return null;
    }


    public DefaultTableModel getModel() {
        if (this.dataModel != null) {
            return (DefaultTableModel) this.dataModel;
        }
        return null;
    }

    public void AddRow(Object object) {
        Class<?> class1 = object.getClass();
        Field[] fields = class1.getFields();
        String field_name = null;
        String field_value = null;
        DefaultTableModel tableModel = getModel();
        Vector rowVector = new Vector(tableModel.getColumnCount());
        String[] columns = new String[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            columns[i] = tableModel.getColumnName(i).toUpperCase();
            rowVector.add("NULL");
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field_name = field.getName();
            int find_id = Arrays.binarySearch(columns, field_name.substring(2, field_name.length()).toUpperCase());
            if (field_name.substring(0, 2).equals("s_") && find_id != -1) {
                try {
                    if (field.get(object) instanceof String) {
                        field_value = (String) field.get(object);
                    } else {
                        field_value = "NULL";
                    }
                } catch (Exception e) {
                    field_value = "NULL";
                }
                rowVector.set(find_id, field_value);
            }
        }

        tableModel.addRow(rowVector);
    }

    public void AddRow(Vector one_row) {
        DefaultTableModel tableModel = getModel();
        tableModel.addRow(one_row);
    }

    public void AddRows(Vector rows) {
        DefaultTableModel tableModel = getModel();
        Vector columnVector = getColumnVector();
        tableModel.setDataVector(rows, columnVector);
    }

    public void SetRow(int row_id, Object object) {
        Class<?> class1 = object.getClass();
        Field[] fields = class1.getFields();
        String field_name = null;
        String field_value = null;
        DefaultTableModel tableModel = getModel();
        Vector rowVector = (Vector) tableModel.getDataVector().get(row_id);
        String[] columns = new String[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            columns[i] = tableModel.getColumnName(i).toUpperCase();
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field_name = field.getName();
            int find_id = Arrays.binarySearch(columns, field_name.substring(2, field_name.length()).toUpperCase());
            if (field_name.substring(0, 2).equals("s_") && find_id != -1) {
                try {
                    if (field.get(object) instanceof String) {
                        field_value = (String) field.get(object);
                    } else {
                        field_value = "NULL";
                    }
                } catch (Exception e) {
                    field_value = "NULL";
                }
                rowVector.set(find_id, field_value);
            }
        }

    }

    public void find(String regxString) {
        if (!regxString.isEmpty()) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) super.getModel());
            setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter(regxString));
        } else {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) super.getModel());
            setRowSorter(sorter);
            sorter.setRowFilter(null);
        }
    }

    public void setRightClickMenu(JPopupMenu rightClickMenu) {
        this.rightClickMenu = rightClickMenu;
        this.rightClickEvent.setRightClickMenu(rightClickMenu);
    }

    public JTableHeader getTableHeader() {
        JTableHeader tableHeader = super.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        hr.setHorizontalAlignment(0);
        return tableHeader;
    }

    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super.getDefaultRenderer(columnClass);
        cr.setHorizontalAlignment(0);
        return cr;
    }

    public boolean isCellEditable(int paramInt1, int paramInt2) {
        return false;
    }

    private class RightClickEvent extends MouseAdapter {
        private JPopupMenu rightClickMenu;
        private DataView dataView;
        private ActionDblClick actionDblClick;

        public RightClickEvent(JPopupMenu rightClickMenu, DataView jtable) {
            this.rightClickMenu = rightClickMenu;
            this.dataView = jtable;
        }

        public void setRightClickMenu(JPopupMenu rightClickMenu) {
            this.rightClickMenu = rightClickMenu;
        }

        public void setActionListener(ActionDblClick event) {
            this.actionDblClick = event;
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == 3) {
                if (this.rightClickMenu != null) {
                    int i = this.dataView.rowAtPoint(mouseEvent.getPoint());
                    if (i != -1) {
                        this.rightClickMenu.show(this.dataView, mouseEvent.getX(), mouseEvent.getY());
                        this.dataView.setRowSelectionInterval(i, i);
                    }
                }
            } else if (mouseEvent.getClickCount() == 2 && this.actionDblClick != null) {
                this.actionDblClick.dblClick(mouseEvent);
            }
        }
    }
}
