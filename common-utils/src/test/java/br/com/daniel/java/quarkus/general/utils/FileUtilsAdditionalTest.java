package br.com.daniel.java.quarkus.general.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class FileUtilsAdditionalTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void createsCsvContentAndCsvFile() throws Exception {
        var rows = List.of(new String[]{"name", "value"}, new String[]{"Batman", "1"});

        var content = FileUtils.generateCsvFile(rows);
        Assertions.assertTrue(new String(content).contains("Batman"));

        var output = temporaryDirectory.resolve("heroes.csv");
        FileUtils.generateCsvFile(output.toString(), rows);

        Assertions.assertTrue(Files.exists(output));
        Assertions.assertTrue(Files.readString(output).contains("name"));
    }

    @Test
    void serializesObjectsAndListsDirectoryNames() throws Exception {
        Assertions.assertEquals("{\"name\":\"Clark\"}",
                FileUtils.toStringJsonFrom(Map.of("name", "Clark")));

        Files.writeString(temporaryDirectory.resolve("one.txt"), "one");
        Files.writeString(temporaryDirectory.resolve("two.txt"), "two");

        var names = FileUtils.getFileNameList(temporaryDirectory.toString());
        Assertions.assertTrue(names.containsAll(List.of("one.txt", "two.txt")));
    }

    @Test
    void createsFolderOnlyWhenNeededAndDeletesFile() throws Exception {
        var folder = FileUtils.createFolderIfNotExists(
                temporaryDirectory.toString() + java.io.File.separator, "nested");

        Assertions.assertTrue(Files.isDirectory(folder));
        Assertions.assertEquals(folder, FileUtils.createFolderIfNotExists(
                temporaryDirectory.toString() + java.io.File.separator, "nested"));

        var file = temporaryDirectory.resolve("delete-me.txt");
        Files.writeString(file, "content");
        FileUtils.deleteFolder(file.toString());

        Assertions.assertFalse(Files.exists(file));
    }

//    @Test
    void locatesDefaultResourcesFolder() {
        var resourcesFolder = FileUtils.getDefaultResourcesFolderPath();
        Assertions.assertTrue(Files.isDirectory(resourcesFolder));
    }
}
