package net.sf.jabref.importer.fileformat;

import java.io.*;
import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;

import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.model.entry.BibEntry;
/*import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;*/

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
    public List<BibEntry> importEntries(InputStream stream, OutputPrinter status) throws IOException {
        List<BibEntry> results = new LinkedList<>();
        if (stream == null) {
            throw new IOException("No stream given.");
        }

        try (BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream))) {
            String str = "";
            //List<String> entries = new LinkedList<>();
            String[] campos = {};
            String[] valores = {};
            //StringBuilder sb = new StringBuilder();
            str = in.readLine();
            campos = str.split(",");
            int tam = campos.length;
            BibEntry b = new BibEntry(DEFAULT_BIBTEXENTRY_ID, "book");
            while (((str = in.readLine()) != null)) {
                    valores = str.split(",");
                    for (int i = 0; i < tam; i++) {
                        b.setField(campos[i], valores[i]);
                    }
                }
            results.add(b);
            }

        return results;
        }
    }


