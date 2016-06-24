package net.sf.jabref.importer.fileformat;

import java.io.*;
import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;
import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.model.entry.BibEntry;

public class CSVImporter extends ImportFormat {

    @Override
    public boolean isRecognizedFormat(InputStream in) {
        return true;
    }

    @Override
    public String getFormatName() {
        return "CSV";
    }

    @Override
    public String getCLIId() {
        return "csv";
    }

    @Override
    public List<BibEntry> importEntries(InputStream stream, OutputPrinter status) throws IOException {
        List<BibEntry> results = new LinkedList<>();
        if (stream == null) {
            throw new IOException("No stream given.");
        }

        try (BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream))) {
            String str = "";
            String[] campos = {};
            String[] valores = {};
            str = in.readLine();
            campos = str.split(",");
            int tam = campos.length;

            while (((str = in.readLine()) != null)) {
                BibEntry b = new BibEntry(DEFAULT_BIBTEXENTRY_ID);
                if(str.endsWith(",")) {
                    str = str + " ";
                }
                valores = str.split(",");
                b.setType(valores[0]);
                boolean valido = false;
                for (int i = 1; i < tam; i++) {
                    b.setField(campos[i], valores[i]);
                }
                if (b.getField("bibtexkey").equals(campos[1])) {
                    //Checa se a bibtexkey foi aceita
                    valido = true;
                }
                //Checa se o author eh valido, senao, recebe "UNKNOWN"
                if(b.getField("author").equals("") || b.getField("author") == null) {
                    b.setField("author", "UNKNOWN");
                }
                //Se a bibtexkey nao foi validada, recebe author + year
                if (!valido) {
                    //Nao precisa checar o year, pois, se invalido, sempre recebe "2016"
                    b.setField("bibtexkey", b.getField("author") + b.getField("year"));
                }
                results.add(b);
            }
        }
        return results;
    }
}
