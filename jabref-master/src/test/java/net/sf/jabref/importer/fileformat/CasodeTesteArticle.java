package net.sf.jabref.importer.fileformat;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.model.entry.BibEntry;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    public void TesteComAnoAcimaDoPermitido() {
        List<BibEntry> teste1 = BibtexParser.fromString("@article{, year = {2017}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        //Caso o ano seja invalido, eh automaticamente setado como 2016
        esperado.setField("year", "2016");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComAnoAbaixoDoPermitido() {
        List<BibEntry> teste1 = BibtexParser.fromString("@article{, year = {1899}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        //Caso o ano seja invalido, eh automaticamente setado como 2016
        esperado.setField("year", "2016");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComAnoLimiteInferior() {
        List<BibEntry> teste1 = BibtexParser.fromString("@article{, year = {1900}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        esperado.setField("year", "1900");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComAnoLimiteSuperior() {
        List<BibEntry> teste1 = BibtexParser.fromString("@article{, year = {2016}}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");

        //De acordo com a validacao de datas, soh sao aceitos anos entre 1900 e 2016
        esperado.setField("year", "2016");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComBibtexkeyComSomenteUmCaractere(){
        List<BibEntry> teste1 = BibtexParser.fromString("@article{b}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");

        //De acordo com a validacao de bibtexkey, entradas com somente um caractere
        //Resultam em string vazia (nao null)
        esperado.setCiteKey("b");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComBibtexkeyNaoIniciadaComLetra(){
        List<BibEntry> teste1 = BibtexParser.fromString("@article{0Abc}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");

        //De acordo com a validacao de bibtexkey, entradas nao iniciadas com letras
        //Resultam em string vazia (nao null)
        esperado.setCiteKey("0Abc");
        assertEquals(Collections.singletonList(esperado), teste1);
    }

    @Test
    public void TesteComBibtexkeyValida(){
        List<BibEntry> teste1 = BibtexParser.fromString("@article{Abc123}");
        BibEntry esperado = new BibEntry();
        esperado.setType("article");

        //De acordo com a validacao de bibtexkey, so sao aceitas entradas
        //com tamanho maior ou igual a 2 caracteres
        //iniciadas com letra maiuscula ou minuscula
        esperado.setCiteKey("Abc123");
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
    public void testeComCamposAdicionais() {
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
