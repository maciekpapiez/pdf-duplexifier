package org.papiez.pdfSplitter.engine;

public class SplitterConfiguration {

    private final int pagesPerSheet;

    public SplitterConfiguration(final int pagesPerSheet) {
        this.pagesPerSheet = pagesPerSheet;
    }

    public SplitterConfiguration() {
        this(1);
    }

    public int getPagesPerSheet() {
        return pagesPerSheet;
    }

}
