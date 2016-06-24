package net.sf.jabref.importer.fileformat;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.OutputPrinterToNull;
import net.sf.jabref.model.entry.BibEntry;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TesteEntradaCsvTest {

    private CSVImporter importer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new CSVImporter();
    }

    @Test
    public void testIsRecognizedFormat() throws IOException {
        try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("TesteEntradaCsv.csv")) {
            assertTrue(importer.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testImportEntries() throws IOException {
        try (InputStream stream = CSVImporterTest.class.getResourceAsStream("TesteEntradaCsv.csv")) {
            List<BibEntry> bibEntries = importer.importEntries(stream, new OutputPrinterToNull());

            assertEquals(7, bibEntries.size());

            for (BibEntry entry : bibEntries) {

                if (entry.getCiteKey().equals("Jonas1918")) {
                    assertEquals("Address1", entry.getField("address"));
                    assertEquals("Jonas", entry.getField("author"));
                    assertEquals("Jonas1918", entry.getField("bibtexkey"));
                    assertEquals("Edition1", entry.getField("edition"));
                    assertEquals("Publisher4", entry.getField("publisher"));
                    assertEquals("TesteCamposOpcionais1", entry.getField("title"));
                    assertEquals("1918", entry.getField("year"));
                    assertEquals("Editor4", entry.getField("editor"));
                    assertEquals("1", entry.getField("volume"));
                    assertEquals("Note1", entry.getField("note"));
                    assertEquals("##jan##", entry.getField("month"));
                    assertEquals("Series1", entry.getField("series"));
                }
            }
        }
    }

    @Test
    public void testGetFormatName() {
        assertEquals("CSV", importer.getFormatName());
    }
}