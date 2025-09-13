import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {

    public static void main(String[] args) {
        System.out.println("Tipografo 1.0");
        
        var currentPath = Paths.get("..");
        var dataPath = currentPath.resolve("data");
        var dataFile = dataPath.resolve("fonts.dat");
        var cssFile = dataPath.resolve("fonts.css");

        try {
            var reader = new Reader(dataFile);
            var fonts = reader.getAllFonts();

            var htmlWriter = new HtmlWriter(fonts);
            var outputPath = currentPath.resolve("output");
            Files.createDirectory(outputPath);
            var outputHtmlFile = outputPath.resolve("index.html");
            htmlWriter.writeTo(outputHtmlFile);

            // copy fonts
            Files.walk(dataPath).forEach( f -> {
                try {
                    Files.copy(f, outputPath.resolve(f.getFileName()));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(2);
                }});
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
                
        System.out.println("Site generation completed successfully.");
    }
}
