package net.sf.jabref.importer.fileformat;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import java.util.Collections;
import java.util.List;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.model.entry.BibEntry;

public class CasodeTesteArticle {

    @BeforeClass
    public static void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
    }

    @Test
    public void TesteComTodosCamposObrigatorios() {
        List<BibEntry> teste1 = BibtexParser.fromString("@article{1, author={Bruno},\n"
                + "title = {As aventuras de Jorginho},\n journal = {jtxt},\n year = {2016} }");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");
        esperado.setCiteKey("1");
	esperado.setField("author", "Bruno");
        esperado.setField("title", "As aventuras de Jorginho");
	esperado.setField("journal", "jtxt");        
	esperado.setField("year", "2016");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void testecomNull() {
        List<BibEntry> teste2 = BibtexParser.fromString("@article{,}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");
        assertEquals(Collections.singletonList(esperado), teste2);
    }

    @Test
    public void testeComCamposAdcionais() {
        List<BibEntry> teste3 = BibtexParser.fromString("@article{2, author={Jorge},\n"
                + "title = {jorge e sua turma},\n journal = {jtxt},\n year = {2016},\n"
		+ "volume = {5},\n number = {40},\n pages = {ten},\n month = {march},\n note = {nn} }");

        BibEntry esperado = new BibEntry();
        esperado.setType("article");
        esperado.setCiteKey("2");
        esperado.setField("author", "Jorge");
        esperado.setField("title", "jorge e sua turma");
	esperado.setField("journal", "jtxt");        
	esperado.setField("year", "2016");
        esperado.setField("volume", "5");
	esperado.setField("number", "40");
	esperado.setField("pages", "ten");
        esperado.setField("month", "march");
        esperado.setField("note", "nn");
        assertEquals(Collections.singletonList(esperado), teste3);

    }

}
