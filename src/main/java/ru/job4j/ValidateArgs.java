package ru.job4j;

import java.util.HashMap;
import java.util.Map;

public class ValidateArgs {

    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("This key: '" + key + "' is missing");
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        for (String prs : args) {
            if (prs == null || !prs.startsWith("-")) {
                throw new IllegalArgumentException("Error: This argument '" + prs + "' does not start with a '-' character");
            }
            String[] part = getStrings(prs);
            if (part[0].contains("?")) {
                throw new IllegalArgumentException("Error: This argument '" + prs + "' contains invalid characters in the key");
            }
            values.put(part[0], part[1]);
        }
    }

    private static String[] getStrings(String prs) {
        String[] part = prs.substring(1).split("=", 2);
        if (part.length != 2 || part[0].isEmpty() || part[1].isEmpty()) {
            if (part[0].isEmpty()) {
                throw new IllegalArgumentException("Error: This argument '" + prs + "' does not contain a key");
            } else if (!prs.contains("=")) {
                throw new IllegalArgumentException("Error: This argument '" + prs + "' does not contain an equal sign");
            } else {
                throw new IllegalArgumentException("Error: This argument '" + prs + "' does not contain a value");
            }
        }
        return part;
    }

    public static ValidateArgs of(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        ValidateArgs names = new ValidateArgs();
        names.parse(args);
        return names;
    }
}
