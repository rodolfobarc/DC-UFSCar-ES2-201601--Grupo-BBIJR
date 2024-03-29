/*  Copyright (C) 2003-2016 JabRef contributors.
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
package net.sf.jabref.exporter;

import net.sf.jabref.bibtex.comparator.FieldComparator;
import net.sf.jabref.bibtex.comparator.FieldComparatorStack;
import net.sf.jabref.logic.layout.format.GetOpenOfficeType;
import net.sf.jabref.logic.layout.format.RemoveBrackets;
import net.sf.jabref.logic.layout.format.RemoveWhitespace;
import net.sf.jabref.model.database.BibDatabase;
import net.sf.jabref.model.entry.BibEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Morten O. Alver.
 *         Based on net.sf.jabref.MODSDatabase by Michael Wrighton
 */
class OpenDocumentRepresentation {

    private static final Log LOGGER = LogFactory.getLog(OpenDocumentRepresentation.class);
    private final List<BibEntry> entries;
    private final BibDatabase database;


    public OpenDocumentRepresentation(BibDatabase database, List<BibEntry> entries) {
        this.database = database;
        // Make a list of comparators for sorting the entries:
        List<FieldComparator> comparators = new ArrayList<>();
        comparators.add(new FieldComparator("author"));
        comparators.add(new FieldComparator("year"));
        comparators.add(new FieldComparator(BibEntry.KEY_FIELD));
        // Use glazed lists to get a sorted view of the entries:
        List<BibEntry> entryList = new ArrayList<>();

        // Set up a list of all entries, if entries==null, or the entries in the given list
        if (entries == null) {
            entryList.addAll(database.getEntries());
        } else {
            entryList.addAll(entries);
        }

        Collections.sort(entryList, new FieldComparatorStack<>(comparators));
        this.entries = entryList;
    }

    public Document getDOMrepresentation() {
        Document result = null;
        try {
            DocumentBuilder dbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            result = dbuild.newDocument();
            Element collection = result.createElement("office:document-content");
            //collection.setAttribute("xmlns", "http://openoffice.org/2000/office");
            collection.setAttribute("xmlns:office", "urn:oasis:names:tc:opendocument:xmlns:office:1.0");
            collection.setAttribute("xmlns:style", "urn:oasis:names:tc:opendocument:xmlns:style:1.0");
            collection.setAttribute("xmlns:text", "urn:oasis:names:tc:opendocument:xmlns:text:1.0");
            collection.setAttribute("xmlns:table", "urn:oasis:names:tc:opendocument:xmlns:table:1.0");
            collection.setAttribute("xmlns:meta", "urn:oasis:names:tc:opendocument:xmlns:meta:1.0");
            collection.setAttribute("office:version", "1.0");
            collection.setAttribute("xmlns:fo", "urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0");
            collection.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
            Element el = result.createElement("office:scripts");
            collection.appendChild(el);

            el = result.createElement("office:automatic-styles");
            Element el2 = result.createElement("style:style");
            el2.setAttribute("style:name", "ro1");
            el2.setAttribute("style:family", "table-row");
            Element el3 = result.createElement("style.table-row-properties");
            el3.setAttribute("style:row-height", "0.1681inch");
            el3.setAttribute("fo:break-before", "auto");
            el3.setAttribute("style:use-optimal-row-height", "true");
            el2.appendChild(el3);
            el.appendChild(el2);
            el2 = result.createElement("style:style");
            el2.setAttribute("style:name", "ta1");
            el2.setAttribute("style:family", "table");
            el2.setAttribute("style:master-page-name", "Default");
            el3 = result.createElement("style:properties");
            el3.setAttribute("table:display", "true");
            el2.appendChild(el3);
            el.appendChild(el2);
            collection.appendChild(el);

            Element body = result.createElement("office:body");
            Element spreadsheet = result.createElement("office:spreadsheet");
            Element table = result.createElement("table:table");
            table.setAttribute("table:name", "biblio");
            table.setAttribute("table.style-name", "ta1");

            Element row = result.createElement("table:table-row");
            row.setAttribute("table.style-name", "ro1");
            addTableCell(result, row, "Identifier");
            addTableCell(result, row, "Type");
            addTableCell(result, row, "Address");
            addTableCell(result, row, "Assignee");
            addTableCell(result, row, "Annote");
            addTableCell(result, row, "Author");
            addTableCell(result, row, "Booktitle");
            addTableCell(result, row, "Chapter");
            addTableCell(result, row, "Day");
            addTableCell(result, row, "Dayfiled");
            addTableCell(result, row, "Edition");
            addTableCell(result, row, "Editor");
            addTableCell(result, row, "Howpublish");
            addTableCell(result, row, "Institution");
            addTableCell(result, row, "Journal");
            addTableCell(result, row, "Language");
            addTableCell(result, row, "Month");
            addTableCell(result, row, "Monthfiled");
            addTableCell(result, row, "Nationality");
            addTableCell(result, row, "Note");
            addTableCell(result, row, "Number");
            addTableCell(result, row, "Organization");
            addTableCell(result, row, "Pages");
            addTableCell(result, row, "Publisher");
            addTableCell(result, row, "Revision");
            addTableCell(result, row, "School");
            addTableCell(result, row, "Series");
            addTableCell(result, row, "Title");
            addTableCell(result, row, "RepType");
            addTableCell(result, row, "Volume");
            addTableCell(result, row, "Year");
            addTableCell(result, row, "Yearfiled");
            addTableCell(result, row, "URL");
            addTableCell(result, row, "Custom1");
            addTableCell(result, row, "Custom2");
            addTableCell(result, row, "Custom3");
            addTableCell(result, row, "Custom4");
            addTableCell(result, row, "Custom5");
            addTableCell(result, row, "ISBN");
            table.appendChild(row);

            for (BibEntry e : entries) {
                row = result.createElement("table:table-row");
                addTableCell(result, row, getField(e, BibEntry.KEY_FIELD));
                addTableCell(result, row, new GetOpenOfficeType().format(e.getType()));
                addTableCell(result, row, getField(e, "address"));
                addTableCell(result, row, getField(e, "assignee"));
                addTableCell(result, row, getField(e, "annote"));
                addTableCell(result, row, getField(e, "author"));//new AuthorLastFirst().format(getField(e, "author")));
                addTableCell(result, row, getField(e, "booktitle"));
                addTableCell(result, row, getField(e, "chapter"));
                addTableCell(result, row, getField(e, "day"));
                addTableCell(result, row, getField(e, "dayfiled"));
                addTableCell(result, row, getField(e, "edition"));
                addTableCell(result, row, getField(e, "editor"));//new AuthorLastFirst().format(getField(e, "editor")));
                addTableCell(result, row, getField(e, "howpublished"));
                addTableCell(result, row, getField(e, "institution"));
                addTableCell(result, row, getField(e, "journal"));
                addTableCell(result, row, getField(e, "language"));
                addTableCell(result, row, getField(e, "month"));
                addTableCell(result, row, getField(e, "monthfiled"));
                addTableCell(result, row, getField(e, "nationality"));
                addTableCell(result, row, getField(e, "note"));
                addTableCell(result, row, getField(e, "number"));
                addTableCell(result, row, getField(e, "organization"));
                addTableCell(result, row, getField(e, "pages"));
                addTableCell(result, row, getField(e, "publisher"));
                addTableCell(result, row, getField(e, "revision"));
                addTableCell(result, row, getField(e, "school"));
                addTableCell(result, row, getField(e, "series"));
                addTableCell(result, row, new RemoveWhitespace().format(new RemoveBrackets().format(getField(e, "title"))));
                addTableCell(result, row, getField(e, "reporttype"));
                addTableCell(result, row, getField(e, "volume"));
                addTableCell(result, row, getField(e, "year"));
                addTableCell(result, row, getField(e, "yearfiled"));
                addTableCell(result, row, getField(e, "url"));
                addTableCell(result, row, "");
                addTableCell(result, row, "");
                addTableCell(result, row, "");
                addTableCell(result, row, "");
                addTableCell(result, row, "");
                addTableCell(result, row, getField(e, "isbn"));
                table.appendChild(row);
            }

            spreadsheet.appendChild(table);
            body.appendChild(spreadsheet);
            collection.appendChild(body);

            result.appendChild(collection);
        } catch (Exception e) {
            LOGGER.warn("Exception caught...", e);
        }
        return result;
    }

    private String getField(BibEntry e, String field) {
        String s = BibDatabase.getResolvedField(field, e, database);
        return s == null ? "" : s;
    }

    private void addTableCell(Document doc, Element parent, String content) {
        Element cell = doc.createElement("table:table-cell");
        Element text = doc.createElement("text:p");
        Text textNode = doc.createTextNode(content);
        text.appendChild(textNode);
        //text.setTextContent(content);
        cell.appendChild(text);
        parent.appendChild(cell);
    }
}
