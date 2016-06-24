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

public class CSVImporterTest {

    private CSVImporter importer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new CSVImporter();
    }

    @Test
    public void testIsRecognizedFormat() throws IOException {
        try (InputStream stream = CSVImporterTest.class.getResourceAsStream("TesteEntradaCsv.csv")) {
            assertTrue(importer.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testImportEntries() throws IOException {
        try (InputStream stream = CSVImporterTest.class.getResourceAsStream("TesteEntradaCsv.csv")) {
            List<BibEntry> bibEntries = importer.importEntries(stream, new OutputPrinterToNull());

            assertEquals(7, bibEntries.size());

            for (BibEntry entry : bibEntries) {
                if (entry.getCiteKey().equals("Lucas1999")) {
                    assertEquals("Lucas", entry.getField("author"));
                    assertEquals("Lucas1999", entry.getField("bibtexkey"));
                    assertEquals("1999", entry.getField("year"));
                    assertEquals("TesteCamposOpcionais2", entry.getField("title"));
                    assertEquals("Journal4", entry.getField("journal"));
                    assertEquals("1", entry.getField("volume"));
                    assertEquals("214", entry.getField("pages"));
                    assertEquals("Note2", entry.getField("note"));
                    assertEquals("2", entry.getField("number"));
                    assertEquals("##feb##", entry.getField("month"));
                }
            }
        }
    }

    @Test
    public void testGetFormatName() {
        assertEquals("CSV", importer.getFormatName());
    }
}
