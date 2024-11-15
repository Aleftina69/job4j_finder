package ru.job4j;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Search {

    public static void main(String[] args) throws IOException {
        ValidateArgs validateArgs = ValidateArgs.of(args);
        String[] argsFind = new String[4];
        argsFind[0] = validateArgs.get("d");
        argsFind[1] = validateArgs.get("n");
        argsFind[2] = validateArgs.get("t");
        argsFind[3] = validateArgs.get("o");

        Path start = Paths.get(validateArgs.get("d"));
        validate(argsFind);
        List<Path> list = new ArrayList<>();
        if ("mask".equals(argsFind[2])) {
            String regex = argsFind[1].replace("*", "\\w+")
                    .replace(".", "\\.")
                    .replace("?", ".");
            list = search(start, path -> path.toFile().getName().matches(regex));
        }
        if ("name".equals(argsFind[2])) {
            list = search(start, path -> path.toFile().getName().endsWith(argsFind[1]));
        }
        saveTo(list, argsFind[3]);
    }

    public static void saveTo(List<Path> data, String out) {
        try (PrintWriter output = new PrintWriter(new BufferedOutputStream(new FileOutputStream(out)))) {
            data.forEach(output::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        SearchFiles searcher = new SearchFiles(condition);
        Files.walkFileTree(root, searcher);
        return searcher.getList();
    }

    public static void validate(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Root folder "
                    + ", file extension for search"
                    + ", type of search"
                    + ", the name of the file to be recorded");
        }
        if (!Files.isDirectory(Path.of(args[0]))) {
            throw new IllegalArgumentException(args[0] + " \n" + "is not a directory");
        }
        if (!"mask".equals(args[2]) && !"name".equals(args[2])) {
            throw new IllegalArgumentException("parameter '-n', must take values 'name' or 'mask'");
        }
        String regex = "^[\\w,\\s-]+\\.[A-Za-z]{3}$";
        if (!args[3].matches(regex)) {
            throw new IllegalArgumentException("parameter '-o' invalid. Name file is not valid");
        }
    }
}
