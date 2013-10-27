package org.papiez.pdfSplitter.app;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.papiez.pdfSplitter.engine.PdfSplitter;
import org.papiez.pdfSplitter.engine.PdfSplitterImpl;
import org.papiez.pdfSplitter.engine.SplitterConfiguration;

public class App {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            throw new IllegalArgumentException("Not enough arguments!");
        }

        if (!args[0].endsWith(".pdf")) {
            throw new IllegalArgumentException(
                    "The input file has to end with .pdf extension!");
        }

        PdfSplitter pdfSplitter = null;
        String coreFilename = args[0].replace(".pdf", "");
        File frontsFile = null;
        File backsFile = null;

        try (InputStream inputStream = new FileInputStream(args[0])) {

            pdfSplitter = new PdfSplitterImpl(new SplitterConfiguration(),
                    inputStream);

            long start = System.currentTimeMillis();

            pdfSplitter.doIt();

            long end = System.currentTimeMillis();

            System.out.println("Splitting took: " + (end - start) + " ms");

            frontsFile = new File(coreFilename + "-fronts.pdf");
            backsFile = new File(coreFilename + "-backs.pdf");

            pdfSplitter.getFronts().save(frontsFile);
            pdfSplitter.getBacks().save(backsFile);

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (COSVisitorException e) {
            e.printStackTrace();
        } finally {
            pdfSplitter.close();
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Desktop.getDesktop().open(new File("input-fronts.pdf"));
                    Desktop.getDesktop().open(new File("input-backs.pdf"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
