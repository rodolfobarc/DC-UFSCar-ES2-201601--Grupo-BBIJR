package net.sf.jabref.importer.fileformat;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.model.entry.BibEntry;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    public void TesteComAnoAcimaDoPermitido() {
        List<BibEntry> teste1 = BibtexParser.fromString("@book{, year = {2017}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        //Caso o ano seja invalido, eh automaticamente setado como 2016
        esperado.setField("year", "2016");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComAnoAbaixoDoPermitido() {
        List<BibEntry> teste1 = BibtexParser.fromString("@book{, year = {1899}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        //Caso o ano seja invalido, eh automaticamente setado como 2016
        esperado.setField("year", "2016");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComAnoLimiteInferior() {
        List<BibEntry> teste1 = BibtexParser.fromString("@book{, year = {1900}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        esperado.setField("year", "1900");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComAnoLimiteSuperior() {
        List<BibEntry> teste1 = BibtexParser.fromString("@book{, year = {2016}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        esperado.setField("year", "2016");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComBibtexkeyComSomenteUmCaractere(){
        List<BibEntry> teste1 = BibtexParser.fromString("@book{b}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");

        //De acordo com a validacao de bibtexkey, entradas com somente um caractere
        //Resultam em string vazia (nao null)
        esperado.setCiteKey("b");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComBibtexkeyNaoIniciadaComLetra(){
        List<BibEntry> teste1 = BibtexParser.fromString("@book{0Abc}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");

        //De acordo com a validacao de bibtexkey, entradas nao iniciadas com letras
        //Resultam em string vazia (nao null)
        esperado.setCiteKey("0Abc");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComBibtexkeyValida(){
        List<BibEntry> teste1 = BibtexParser.fromString("@book{Abc123}");
        BibEntry esperado = new BibEntry();
        esperado.setType("book");

        //De acordo com a validacao de bibtexkey, so sao aceitas entradas
        //com tamanho maior ou igual a 2 caracteres
        //iniciadas com letra maiuscula ou minuscula
        esperado.setCiteKey("Abc123");
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
    public void testeComCamposAdicionais() {
        List<BibEntry> teste3 = BibtexParser.fromString("@book{2, title = {As cronicas de Bruno},\n"
                + "publisher = {compufscar},\n year = {2017},\n author={Jorge},\n editor = {Abril},\n "
                + "volume = {2},\n series = {two},\n edition = {4},\n note = {nothing}, number = {4},\n"
                + "address = {Rodovia Washington Luis},\n month = {april}}");

        BibEntry esperado = new BibEntry();
        esperado.setType("book");
        esperado.setCiteKey("2");
        esperado.setField("title", "As cronicas de Bruno");
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
