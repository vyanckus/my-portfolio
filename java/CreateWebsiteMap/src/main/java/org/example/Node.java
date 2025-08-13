package org.example;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final String url;
    private final List<Node> children;
    private int numberOfTabs;

    public Node(String url) {
        this.url = url;
        this.children = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getNumberOfTabs() {
        return numberOfTabs;
    }

    public void setNumberOfTabs(int numberOfTabs) {
        this.numberOfTabs = numberOfTabs;
    }

    public void addChild(Node child) {
        children.add(child);
    }
}
