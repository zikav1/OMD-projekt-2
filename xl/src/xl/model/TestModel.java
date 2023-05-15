package xl.model;

public class TestModel {
    public static void main(String[] args) {
        Sheet sheet = new Sheet();
        // Invalid expression test
        sheet.add("A2", "2");
        sheet.add("A1", "A2");
        sheet.add("A2", "1 + A1");
    }
}
