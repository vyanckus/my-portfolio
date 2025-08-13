package org.example.filefinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {
    public List<File> findFiles(String path, String fileExtension) {
        List<File> foundFiles = new ArrayList<>();
        File directory = new File(path);

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Указанный путь не является директорией или не существует.");
            return foundFiles;
        }

        try {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith("." + fileExtension.toLowerCase())) {
                        foundFiles.add(file);
                    } else if (file.isDirectory()) {
                        foundFiles.addAll(findFiles(file.getAbsolutePath(), fileExtension));
                    }
                }
            } else {
                System.err.println("Не удалось получить список файлов в директории. Вероятно, директория: " + path + " пуста.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundFiles;
    }

    public void printResults(List<File> files, String fileExtension) {
        if (files.isEmpty()) {
            System.out.println("Файлы с расширением " + fileExtension + " не найдены.");
        } else {
            System.out.println("Найденные файлы с расширением " + fileExtension + ":");
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }
        }
    }
}
