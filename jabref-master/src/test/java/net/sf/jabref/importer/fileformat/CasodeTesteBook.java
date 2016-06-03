package net.sf.jabref.importer.fileformat;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import java.util.Collections;
import java.util.List;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.fileformat.BibtexParser;
import net.sf.jabref.model.entry.BibEntry;

public class CasodeTesteBook {

    @BeforeClass
    public static void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
    }

    @Test
    public void TesteComTodosCamposObrigatorios() {
        List<BibEntry> teste1 = BibtexParser.fromString("@book{1, title = {As aventuras de Jorginho},\n"
                + "publisher = {compsc},\n year = {2016},\n author={Bruno},\n editor = {ninguem} }");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");
        esperado.setCiteKey("1");
        esperado.setField("title", "As aventuras de Jorginho");
        esperado.setField("publisher", "compsc");
        esperado.setField("year", "2016");
        esperado.setField("author", "Bruno");
        esperado.setField("editor", "ninguem");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void testecomNull() {
        List<BibEntry> teste2 = BibtexParser.fromString("@book{,}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");
        assertEquals(Collections.singletonList(esperado), teste2);
    }

    @Test
    public void testeComCamposAdcionais() {
        List<BibEntry> teste3 = BibtexParser.fromString("@book{2, title = {As cr�nicas de Bruno},\n"
                + "publisher = {compufscar},\n year = {2017},\n author={Jorge},\n editor = {Abril},\n "
                + "volume = {2},\n series = {two},\n edition = {4},\n note = {nothing}, number = {4},\n"
                + "address = {Rodovia Washington Luis},\n month = {april}}");

        BibEntry esperado = new BibEntry();
        esperado.setType("book");
        esperado.setCiteKey("2");
        esperado.setField("title", "As cr�nicas de Bruno");
        esperado.setField("publisher", "compufscar");
        esperado.setField("year", "2017");
        esperado.setField("author", "Jorge");
        esperado.setField("editor", "Abril");
        esperado.setField("volume", "2");
        esperado.setField("series", "two");
        esperado.setField("edition", "4");
        esperado.setField("note", "nothing");
        esperado.setField("number", "4");
        esperado.setField("address", "Rodovia Washington Luis");
        esperado.setField("month", "april");
        assertEquals(Collections.singletonList(esperado), teste3);

    }

}
