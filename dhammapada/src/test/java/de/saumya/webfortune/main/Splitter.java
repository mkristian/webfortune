package de.saumya.webfortune.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * command line helper for transforming original file into xml
 */
// TODO share this with webfortune module !!!
public class Splitter {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        new Splitter("src/main/original",
                "war/de.saumya.dhammapada.DhammapadaTest",
                "").createXmlFiles();
    }

    private final File         targetDir;

    private final File         sourceDir;

    private final File         base;

    private final StringBuffer topicsXML = new StringBuffer("<?xml version=\"1.0\"?>\n<topics>\n");

    private final String       quoteSeparatorLine;

    public Splitter(final String base, final String targetDir,
            final String lineSeparator) {
        this.targetDir = new File(targetDir);
        this.base = new File(base);
        this.sourceDir = this.base.isDirectory()
                ? this.base.getAbsoluteFile()
                : this.base.getAbsoluteFile().getParentFile();
        this.targetDir.mkdirs();
        this.quoteSeparatorLine = lineSeparator;
    }

    private void appendXML(final String name, final int max) {
        this.topicsXML.append("  <topic directory=\"" + name
                + "\" displayname=\"" + name + "\" size=\"" + max + "\"/>\n");
    }

    public void printTopicsXML() throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(new File(this.targetDir,
                    "topics.xml")),
                    "UTF-8");
            writer.append(this.topicsXML.append("</topics>\n").toString());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void createXmlFiles() throws IOException {
        if (this.base.isDirectory()) {
            for (final String file : this.base.list()) {
                if (!file.contains(".")) {
                    split(file);
                }
            }
        }
        else {
            split(this.base.getName());
        }
        printTopicsXML();
    }

    public void split(final String name) throws IOException {
        System.out.println("split " + name + " of directory " + this.sourceDir);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.sourceDir,
                name)),
                "UTF-8"));
        int index = 0;
        boolean running = true;
        while (running) {
            switch (writeOutFile(reader, name, index)) {
            case EOF:
                running = false;
                break;
            case OK:
                index++;
            case NO_FILE:
            }
        }
        appendXML(name, index - 1);
        reader.close();
    }

    enum Result {
        EOF, NO_FILE, OK
    }

    private Result writeOutFile(final BufferedReader reader, final String name,
            final int index) throws IOException {
        final File file = new File(this.targetDir, name + "/" + index + ".xml");
        file.getParentFile().mkdirs();
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                "UTF-8"));
        writer.append("<pre>\n");
        String line = reader.readLine();
        boolean first = true;
        while (line != null && !line.trim().equals(this.quoteSeparatorLine)) {
            first = false;
            writer.append(processLine(line)).append("\n");
            line = reader.readLine();
        }
        writer.append("</pre>");
        writer.close();
        if (first) {
            file.delete();
        }
        return line == null ? Result.EOF : (first ? Result.NO_FILE : Result.OK);
    }

    protected String processLine(final String line) {
        if (line.contains("story.php")) {
            return line.replaceFirst("./story.php[?]dat=([0-9]*)&index=([0-9]*)",
                                     "#stories/$1/$2.xml")
                    .replaceFirst("IMG(.*)>&nbsp", "img$1/>\u00a0");
        }
        else {
            return line.replaceFirst("IMG(.*)>&nbsp", "img$1/>\u00a0");
        }
    }
}
