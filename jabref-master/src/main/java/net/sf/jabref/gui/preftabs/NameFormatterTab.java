/*  Copyright (C) 2003-2015 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package net.sf.jabref.gui.preftabs;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.gui.IconTheme;
import net.sf.jabref.gui.OSXCompatibleToolbar;
import net.sf.jabref.gui.help.HelpAction;
import net.sf.jabref.gui.help.HelpFiles;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.layout.format.NameFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NameFormatterTab extends JPanel implements PrefsTab {

    private final JabRefPreferences prefs;
    private final JTable table;
    private final List<TableRow> tableRows = new ArrayList<>(10);
    private boolean tableChanged;
    private int rowCount = -1;


    /**
     * Tab to create custom Name Formatters
     */
    public NameFormatterTab(JabRefPreferences prefs) {
        this.prefs = Objects.requireNonNull(prefs);
        setLayout(new BorderLayout());

        TableModel tableModel = new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return rowCount;
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (row >= tableRows.size()) {
                    return "";
                }
                TableRow tr = tableRows.get(row);
                if (tr == null) {
                    return "";
                }
                // Only two columns
                if (column == 0) {
                    return tr.getName();
                } else {
                    return tr.getFormat();
                }
            }

            @Override
            public String getColumnName(int col) {
                return col == 0 ? Localization.lang("Formatter Name") :
                        Localization.lang("Format String");
            }

            @Override
            public Class<String> getColumnClass(int column) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return true;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                tableChanged = true;

                // Make sure the vector is long enough.
                while (row >= tableRows.size()) {
                    tableRows.add(new TableRow());
                }

                TableRow rowContent = tableRows.get(row);

                if (col == 0) {
                    rowContent.setName(value.toString());
                } else {
                    rowContent.setFormat(value.toString());
                }
            }
        };

        table = new JTable(tableModel);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(140);
        columnModel.getColumn(1).setPreferredWidth(400);

        FormLayout layout = new FormLayout("1dlu, 8dlu, left:pref, 4dlu, fill:pref", "");

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);

        JPanel pan = new JPanel();

        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        table.setPreferredScrollableViewportSize(new Dimension(250, 200));
        scrollPane.setMinimumSize(new Dimension(250, 300));
        scrollPane.setPreferredSize(new Dimension(600, 300));
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        JToolBar toolBar = new OSXCompatibleToolbar(SwingConstants.VERTICAL);
        toolBar.setFloatable(false);
        toolBar.setBorder(null);
        toolBar.add(new AddRowAction());
        toolBar.add(new DeleteRowAction());
        toolBar.add(new HelpAction(Localization.lang("Help on Name Formatting"),
                HelpFiles.CUSTOM_EXPORTS_NAME_FORMATTER).getHelpButton());

        tabPanel.add(toolBar, BorderLayout.EAST);

        builder.appendSeparator(Localization.lang("Special Name Formatters"));
        builder.nextLine();
        builder.append(pan);
        builder.append(tabPanel);
        builder.nextLine();

        pan = builder.getPanel();
        pan.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(pan, BorderLayout.CENTER);
    }

    @Override
    public void setValues() {
        tableRows.clear();
        List<String> names = prefs.getStringList(NameFormatter.NAME_FORMATER_KEY);
        List<String> formats = prefs.getStringList(NameFormatter.NAME_FORMATTER_VALUE);

        for (int i = 0; i < names.size(); i++) {
            if (i < formats.size()) {
                tableRows.add(new TableRow(names.get(i), formats.get(i)));
            } else {
                tableRows.add(new TableRow(names.get(i)));
            }
        }
        rowCount = tableRows.size() + 5;
    }

    /**
     * Store changes to table preferences. This method is called when the user
     * clicks Ok.
     */
    @Override
    public void storeSettings() {

        if (table.isEditing()) {
            int col = table.getEditingColumn();
            int row = table.getEditingRow();
            table.getCellEditor(row, col).stopCellEditing();
        }

        // Now we need to make sense of the contents the user has made to the
        // table setup table.
        if (tableChanged) {
            // First we remove all rows with empty names.
            int i = 0;
            while (i < tableRows.size()) {
                if (tableRows.get(i).getName().isEmpty()) {
                    tableRows.remove(i);
                } else {
                    i++;
                }
            }
            // Then we make lists

            List<String> names = new ArrayList<>(tableRows.size());
            List<String> formats = new ArrayList<>(tableRows.size());

            for (TableRow tr : tableRows) {
                names.add(tr.getName());
                formats.add(tr.getFormat());
            }

            // Finally, we store the new preferences.
            prefs.putStringList(NameFormatter.NAME_FORMATER_KEY, names);
            prefs.putStringList(NameFormatter.NAME_FORMATTER_VALUE, formats);
        }
    }

    @Override
    public boolean validateSettings() {
        return true;
    }

    @Override
    public String getTabName() {
        return Localization.lang("Name formatter");
    }

    static class TableRow {

        private String name;

        private String format;


        public TableRow() {
            this("");
        }

        public TableRow(String name) {
            this(name, NameFormatter.DEFAULT_FORMAT);
        }

        public TableRow(String name, String format) {
            this.name = name;
            this.format = format;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }

    class DeleteRowAction extends AbstractAction {

        public DeleteRowAction() {
            super("Delete row", IconTheme.JabRefIcon.REMOVE_NOBOX.getIcon());
            putValue(Action.SHORT_DESCRIPTION, Localization.lang("Delete rows"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            tableChanged = true;

            int[] selectedRows = table.getSelectedRows();

            int numberDeleted = 0;

            for (int i = selectedRows.length - 1; i >= 0; i--) {
                if (selectedRows[i] < tableRows.size()) {
                    tableRows.remove(selectedRows[i]);
                    numberDeleted++;
                }
            }

            rowCount -= numberDeleted;

            if (selectedRows.length > 1) {
                table.clearSelection();
            }

            table.revalidate();
            table.repaint();
        }
    }

    class AddRowAction extends AbstractAction {

        public AddRowAction() {
            super("Add row", IconTheme.JabRefIcon.ADD_NOBOX.getIcon());
            putValue(Action.SHORT_DESCRIPTION, Localization.lang("Insert rows"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] rows = table.getSelectedRows();
            if (rows.length == 0) {
                // No rows selected, so we just add one at the end.
                rowCount++;
                table.revalidate();
                table.repaint();
                return;
            }
            for (int i = 0; i < rows.length; i++) {
                if (((rows[i] + i) - 1) < tableRows.size()) {
                    tableRows.add(Math.max(0, (rows[i] + i) - 1), new TableRow());
                }
            }
            rowCount += rows.length;
            if (rows.length > 1) {
                table.clearSelection();
            }
            table.revalidate();
            table.repaint();
            tableChanged = true;
        }
    }
}
