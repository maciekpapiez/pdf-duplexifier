package org.papiez.pdfSplitter.engine;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface PdfSplitter {

    void doIt() throws IOException;

    PDDocument getFronts();

    PDDocument getBacks();

    void close() throws IOException;

}
