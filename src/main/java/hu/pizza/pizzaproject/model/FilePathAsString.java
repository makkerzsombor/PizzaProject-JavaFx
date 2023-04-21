package hu.pizza.pizzaproject.model;

/**
 * FájlÚtSzövegként osztály.
 */
public abstract class FilePathAsString {
    /**
     * Egy String változó ami az adott kép elérési útvonalát tárolja.
     */
    private static String filePath;

    /**
     * Visszaadja a String változót, akárhol meghívható.
     * @return String ami a kép elérési útvonalát tárolja.
     */
    public static String getFilePath() {
        return filePath;
    }

    /**
     * A classban található filePath Stringet változtatja, akárhonnan meghívható.
     * @param filePath Egy String, ami az új filePath lesz.
     */
    public static void setFilePath(String filePath) {
        FilePathAsString.filePath = filePath;
    }
}
