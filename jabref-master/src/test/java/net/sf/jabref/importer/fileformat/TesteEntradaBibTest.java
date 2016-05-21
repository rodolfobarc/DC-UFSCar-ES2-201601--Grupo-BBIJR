package net.sf.jabref.importer.fileformat;

    import java.io.IOException;
    import java.io.InputStream;
    import java.util.List;

    import net.sf.jabref.Globals;
    import net.sf.jabref.JabRefPreferences;
    import net.sf.jabref.importer.OutputPrinterToNull;
    import net.sf.jabref.model.entry.BibEntry;

    import org.junit.Before;
    import org.junit.Test;

    import static org.junit.Assert.assertEquals;
    import static org.junit.Assert.assertTrue;

public class TesteEntradaBibTest {

        private BibtexImporter importer;

        @Before
        public void setUp() {
            Globals.prefs = JabRefPreferences.getInstance();
            importer = new BibtexImporter();
        }

        @Test
        public void testIsRecognizedFormat() throws IOException {
            try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("TesteEntradaBib.bib")) {
                assertTrue(importer.isRecognizedFormat(stream));
            }
        }

        @Test
        public void testImportEntries() throws IOException {
            try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("TesteEntradaBib.bib")) {
                List<BibEntry> bibEntries = importer.importEntries(stream, new OutputPrinterToNull());

            assertEquals(2, bibEntries.size());

                for (BibEntry entry : bibEntries) {

                    if (entry.getCiteKey().equals("artigo1")) {
                    assertEquals("Iago and Rodolfo", entry.getField("author"));
                        assertEquals("artigo1", entry.getField("bibtexkey"));
                        assertEquals("2016", entry.getField("date"));
                        assertEquals("Testes, testes, testes", entry.getField("indextitle"));
                    assertEquals("#Veja#", entry.getField("journaltitle"));
                        assertEquals("5", entry.getField("number"));
                        assertEquals("5-20", entry.getField("pages"));
                    assertEquals("Bruno e suas peripécias", entry.getField("title"));
                        assertEquals("10", entry.getField("volume"));

                    } else if (entry.getCiteKey().equals("livro1")) {
                        assertEquals("add livro", entry.getField("address"));
                        assertEquals("Jorge", entry.getField("author"));
                        assertEquals("livro1", entry.getField("bibtexkey"));
                        assertEquals("3ed", entry.getField("edition"));
                        assertEquals("97866", entry.getField("isbn"));
                        assertEquals("teste", entry.getField("keywords"));
                        assertEquals("310", entry.getField("pages"));
                        assertEquals("pubjorge", entry.getField("publisher"));
                        assertEquals("testes no jabref",entry.getField("title"));
                        assertEquals("http://d-nb.info/107601965X", entry.getField("url"));
                        assertEquals("2016", entry.getField("year"));
                    }
                }
            }
        }

        @Test
        public void testGetFormatName() {
            assertEquals("BibTeX", importer.getFormatName());
        }

        @Test
        public void testGetExtensions() {
            assertEquals("bib", importer.getExtensions());
        }
    }