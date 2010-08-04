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
public class Splitter {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        final File base;
        if (args.length == 0) {
            base = new File("/usr/share/fortune").exists()
                    ? new File("/usr/share/fortune")
                    : new File("/usr/share/games/fortunes");
        }
        else {
            base = new File(args[0]);
        }

        Splitter splitter;
        if (base.isDirectory()) {
            splitter = new Splitter(base.getPath(),
                    "war/de.saumya.webfortune.WebfortuneTest");
            for (final String file : base.list()) {
                if (!file.contains(".")) {
                    splitter.split(file);
                }
            }
        }
        else {
            splitter = new Splitter(base.getParent(),
                    "war/de.saumya.webfortune.WebfortuneTest");
            splitter.split(base.getName());
        }
        splitter.printTopicsXML();
    }

    private final File         targetDir;

    private final File         sourceDir;

    private final StringBuffer topicsXML = new StringBuffer("<?xml version=\"1.0\"?>\n<topics>\n");

    public Splitter(final String sourceDir, final String targetDir) {
        this.targetDir = new File(targetDir);
        this.sourceDir = new File(sourceDir);
        this.targetDir.mkdirs();
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

    public void split(final String name) throws IOException {
        System.out.println("split " + name + " of directory " + this.sourceDir);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.sourceDir,
                name)),
                "UTF-8"));
        int index = 0;
        while (writeOutFile(reader, name, index++)) {
            ;
        }
        appendXML(name, index - 1);
        reader.close();
    }

    private boolean writeOutFile(final BufferedReader reader,
            final String name, final int index) throws IOException {
        final File file = new File(this.targetDir, name + "/" + index + ".xml");
        file.getParentFile().mkdirs();
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                "UTF-8"));
        writer.append("<pre>\n");
        String line = reader.readLine();
        while (line != null && !line.trim().equals("%")) {
            writer.append(processLine(line)).append("\n");
            line = reader.readLine();
        }
        writer.append("</pre>");
        writer.close();
        if (line == null) {
            file.delete();
        }
        return line != null;
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
