package org.blockmap.whitepaper.search;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by xingfeiy on 4/13/18.
 */
public class SimpleSearcher {
    public static void main(String[] args) throws IOException, SolrServerException {
        String urlString = "http://localhost:8983/solr/chainmap";
        final SolrClient solr = new HttpSolrClient.Builder(urlString).build();

        //Preparing the Solr document
//        final SolrInputDocument doc = new SolrInputDocument();

        Path path = Paths.get("/Users/xingfeiy/Private/baiduyun/blockchain/whitepaper");

        if (Files.isDirectory(path)) {
            //Iterate directory
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        indexDoc(file, solr);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } catch (SolrServerException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            //Index this file
            indexDoc(path, solr);
        }

        //Saving the changes
        solr.commit();
        System.out.println("Documents added");
    }

    private static void indexDoc(Path file, SolrClient Solr) throws IOException, SolrServerException {
        System.out.println("Indexing " + file.getFileName());
        PDFUtils utils = new PDFUtils(file.toAbsolutePath().toString());
        SolrInputDocument doc = new SolrInputDocument();
        if(StringUtils.isNotBlank(utils.getContent())) {
            doc.addField("file_name", file.getFileName().toString());
            doc.addField("content", utils.getContent());
            doc.addField("author", "Xingfei");
        }
        Solr.add(doc);
    }
}
