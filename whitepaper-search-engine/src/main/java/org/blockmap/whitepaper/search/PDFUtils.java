package org.blockmap.whitepaper.search;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xingfeiy on 4/17/18.
 */
public class PDFUtils {
    private String content = StringUtils.EMPTY;

    private List<String> meta = new ArrayList<String>();

    private String fileName = StringUtils.EMPTY;

    public PDFUtils(String fileStr) {
        if(StringUtils.isBlank(fileStr)) return;
        File file = new File(fileStr);
        if(file != null) {
            this.fileName = file.getName();
            parse(file);
        }
    }

    private boolean parse(File file) {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        try {
            FileInputStream inputstream = new FileInputStream(file);
            ParseContext pcontext = new ParseContext();

            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputstream, handler, metadata,pcontext);
            this.content = handler.toString();
            meta = Arrays.asList(metadata.names());

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public String getContent() {
        return content;
    }

    public List<String> getMeta() {
        return meta;
    }

    public String getFileName() {
        return fileName;
    }
}
