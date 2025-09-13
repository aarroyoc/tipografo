import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Reader {

    private final Path file;
    private final Pattern namePattern;
    private final Pattern authorPattern;
    private final Pattern licensePattern;
    private final Pattern descriptionPattern;
    private final Pattern filePattern;
    
    Reader(Path file) {
        this.file = file;
        this.namePattern = Pattern.compile("Name: (.*)");
        this.authorPattern = Pattern.compile("Author: (.*)");
        this.licensePattern = Pattern.compile("License: (.*)");
        this.descriptionPattern = Pattern.compile("Description:.*");
        this.filePattern = Pattern.compile("File: (.*)");
    }

    public List<Font> getAllFonts() throws IOException {
        var fonts = new ArrayList<Font>();
        var fis = Files.newInputStream(this.file, StandardOpenOption.READ);
        var sc = new Scanner(fis);

        String name = null;
        String description = null;
        String author = null;
        String license = null;
        List<String> files = new ArrayList<String>();

        while(sc.hasNextLine()) {
            var line = sc.nextLine();
            if (name == null) {
                var m = this.namePattern.matcher(line);
                if (m.matches()) {
                    name = m.group(1);
                    continue;
                }
            }

            if (author == null) {
                var m = this.authorPattern.matcher(line);
                if (m.matches()) {
                    author = m.group(1);
                    continue;
                }
            }

            if (license == null) {
                var m = this.licensePattern.matcher(line);
                if (m.matches()) {
                    license = m.group(1);
                    continue;
                }
            }

            if (description == null) {
                var m = this.descriptionPattern.matcher(line);
                if (m.matches()) {
                    var str = new StringBuilder();
                    while (true) {
                        if (!sc.hasNextLine()) {
                            break;
                        }
                        var descLine = sc.nextLine();
                        if (descLine.equals("")) {
                            break;
                        }
                        str.append(descLine);
                    }
                    description = str.toString();
                }
            }

            var m = this.filePattern.matcher(line);
            if (m.matches()) {
                files.add(m.group(1));
            }

            if (line.equals("===")) {
                var font = new Font(name, author, description, license, files);
                name = null;
                author = null;
                license = null;
                description = null;
                files = new ArrayList<String>();
                fonts.add(font);
            }
        }
        return fonts;
    }
    
}
