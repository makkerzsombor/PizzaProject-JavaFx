package hu.pizza.pizzaproject.model;

public abstract class FilePathAsString {
    private static String filePath;

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        FilePathAsString.filePath = filePath;
    }
}
