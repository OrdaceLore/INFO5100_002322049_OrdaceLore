import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexDemo {
    public static void main(String[] args) {
        // 1. Email pattern
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        testPattern(emailPattern, "test@example.com", true);
        testPattern(emailPattern, "invalid-email", false);

        // 2. Phone number pattern (US)
        String phonePattern = "^\\(\\d{3}\\) \\d{3}-\\d{4}$";
        testPattern(phonePattern, "(123) 456-7890", true);
        testPattern(phonePattern, "123-456-7890", false);

        // 3. URL pattern
        String urlPattern = "^(https?|ftp)://[\\w.-]+(?:\\.[\\w\\.-]+)+[/\\w\\.-]*$";
        testPattern(urlPattern, "https://www.example.com", true);
        testPattern(urlPattern, "htp://example", false);

        // 4. Date pattern (YYYY-MM-DD)
        String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";
        testPattern(datePattern, "2025-02-22", true);
        testPattern(datePattern, "22-02-2025", false);

        // 5. Hex color code pattern
        String hexColorPattern = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        testPattern(hexColorPattern, "#a3c113", true);
        testPattern(hexColorPattern, "123456", false);
    }

    private static void testPattern(String pattern, String input, boolean expected) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);
        boolean matches = matcher.matches();
        System.out.println("Pattern: " + pattern);
        System.out.println("Input: " + input);
        System.out.println("Expected: " + expected + ", Actual: " + matches);
        System.out.println(matches == expected ? "Test Passed" : "Test Failed");
        System.out.println();
    }
}