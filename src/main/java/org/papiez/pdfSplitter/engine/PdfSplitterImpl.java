package org.papiez.pdfSplitter.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.pdfbox.util.Splitter;

public class PdfSplitterImpl implements PdfSplitter {

    // TODO splitter config handling
    private final SplitterConfiguration config;
    private final InputStream inputStream;

    private PDDocument inputDocument = null;
    private PDDocument frontPages = null;
    private PDDocument backPages = null;

    public PdfSplitterImpl(final SplitterConfiguration config, final InputStream inputStream) {
        this.config = config;
        this.inputStream = inputStream;
    }

    public void doIt() throws IOException {
        List<PDDocument> pages = null;

        try {
            inputDocument = PDDocument.load(inputStream);
            pages = new Splitter().split(inputDocument);

            frontPages = new PDDocument();
            backPages = new PDDocument();

            mergeIntoFrontsAndBacks(pages);
        } finally {
            closePDDocument(inputDocument);

            for (PDDocument page : pages) {
                closePDDocument(page);
            }
        }
    }

    public PDDocument getFronts() {
        validate();

        return frontPages;
    }

    public PDDocument getBacks() {
        validate();

        return backPages;
    }

    public void close() throws IOException {
        closePDDocument(frontPages);
        closePDDocument(backPages);
    }

    private void validate() {
        if (null == frontPages || null == backPages) {
            throw new IllegalStateException(
                    "The input document has not been split yet!");
        }
    }

    private void mergeIntoFrontsAndBacks(final List<PDDocument> pages)
            throws IOException {

        addPagesToEvenNumber(pages);

        int i = 0;
        PDFMergerUtility merger = new PDFMergerUtility();
        List<PDDocument> frontsInInvalidOrder = new LinkedList<PDDocument>();

        for (PDDocument page : pages) {
            if ((i++ % 2) == 0) {
                frontsInInvalidOrder.add(page);
            } else {
                merger.appendDocument(backPages, page);
            }
        }

        for (int j = frontsInInvalidOrder.size() - 1; j >= 0; j--) {
            merger.appendDocument(frontPages, frontsInInvalidOrder.get(j));
        }
    }

    private void closePDDocument(final PDDocument document) throws IOException {
        if (null != document) {
            document.close();
        }
    }

    private void addPagesToEvenNumber(final List<PDDocument> pages)
            throws IOException {
        if (pages.size() % 2 == 1) {
            pages.add(new PDDocument());
        }
    }
}
