import java.util.ArrayList;
import java.util.List;

public class GraphInfoExtractor {
    public static void main(String[] args) {
        // Sample input lines
        String[] lines = {
            "** G1: |V|=10  V={0,1,...,9}",
            "** G2: |V|=20  V={0,1,...,19}",
            "** G3: |V|=5   V={0,1,2,3,4}"
        };

        List<Integer> numVerticesList = new ArrayList<>();

        for (String line : lines) {
            // Find the position of "|V|=" in the string
            int startIndex = line.indexOf("|V|=");

            if (startIndex != -1) {
                // Extract the substring after "|V|="
                String substring = line.substring(startIndex + 4);

                // Find the first space character in the substring
                int endIndex = substring.indexOf(" ");

                if (endIndex != -1) {
                    // Extract the number as a substring
                    String numString = substring.substring(0, endIndex);

                    try {
                        // Parse the substring as an integer and add it to the list
                        int numVertices = Integer.parseInt(numString);
                        numVerticesList.add(numVertices);
                    } catch (NumberFormatException e) {
                        System.out.println("Unable to parse the number of vertices.");
                    }
                }
            }
        }

        // Print the list of extracted values
        System.out.println("Extracted number of vertices: " + numVerticesList);
    }
}
