package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class WebsiteMapGeneratorTask extends RecursiveTask<Node> {
    private final Node node;
    private final Set<String> uniqueLinks;
    private final LinkFilter filter;

    public WebsiteMapGeneratorTask(Node node, Set<String> uniqueLinks, LinkFilter filter) {
        this.node = node;
        this.uniqueLinks = uniqueLinks;
        this.filter = filter;
    }

    @Override
    protected Node compute() {

        try {
            List<WebsiteMapGeneratorTask> taskList = new ArrayList<>();
            List<Node> children = new WebPageParser().getNodesFromPage(node, filter);

            for (Node child : children) {

                synchronized (uniqueLinks) {

                    if (uniqueLinks.add(child.getUrl())) {
                        child.setNumberOfTabs(node.getNumberOfTabs() + 1);
                        node.addChild(child);
                        WebsiteMapGeneratorTask task = new WebsiteMapGeneratorTask(child, uniqueLinks, filter);
                        task.fork();
                        taskList.add(task);
                    }
                }
            }

            for (WebsiteMapGeneratorTask task : taskList) {
                task.join();
            }

        } catch (WebPageParserException e) {
            System.err.println("Ошибка обработки: " + node.getUrl() + e.getMessage());
        }
        return node;
    }

}
