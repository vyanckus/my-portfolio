package org.example;

import java.io.*;

public class FileWriterHelper {
    public void writeTree(Node node, BufferedWriter writer) throws IOException {
        writer.write("\t".repeat(node.getNumberOfTabs())+ node.getUrl());
        writer.newLine();

        for (Node child : node.getChildren()) {
            writeTree(child, writer);
        }
    }
}
