import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.text.MessageFormat;

public class HtmlWriter{

    private final String header = """
      <!DOCTYPE html>
      <html>
      <head>
        <meta charset="utf-8">
        <title>Tipógrafo</title>
        <style>
        body {
          font-family: sans-serif;
          background: #d5f2ce;
          background: radial-gradient(circle,rgba(213, 242, 206, 1) 0%, rgba(236, 242, 233, 1) 100%);
       }

        #fonts {
          display: flex;
          flex-wrap: wrap;
        }

        .font-item {
          width: 20rem;
          margin: 1rem;
        }
      </style>
      </head>
      <body>
        <h1>Tipógrafo</h1>
        <p>A collection of high quality typographies with free licenses that allow redistribution.</p>
        <div id="fonts">
    """;

    private final String fontCard = """
            <style>
            @font-face '{'
              font-family: "{0}";
              src: url("{3}");
            '}'
            </style>
            <section class="font-item" style="font-family: {0}">
            <h2>{0}</h2>{1}
                  <p>
                    <a href="{2}">
                      Read license text
                    </a>
                  </p>
              <a href="#descargar{0}">
                Go to downloads
              </a>
            </section>
        """;

    private final String startDownloadSection = """
                </div>
        <h2>Descargas</h2>
        """;

    private final String footer = """
              </body>
    </html>
        """;

    private final String downloadSection = """
            <section>
                       <h3 id="descargar{0}">{0}</h3>{1}
    </section>
              """;

    public final List<Font> fonts;
    
    HtmlWriter(List<Font> fonts) {
        this.fonts = fonts;
    }

    public void writeTo(Path output) throws IOException {
        var sb = new StringBuilder(header);
        var fontCardFormat = new MessageFormat(this.fontCard);
        for(Font font : fonts) {
            Object[] args = {font.name(), font.description(), font.license(), font.files().get(0)};
            sb.append(fontCardFormat.format(args));
        }
        sb.append(startDownloadSection);
        var downloadSectionFormat = new MessageFormat(this.downloadSection);
        var downloadLinkFormat = new MessageFormat("<li><a href=\"{0}\">{0}</a></li>");
        for(Font font : fonts) {
            var filesSb = new StringBuilder("<ul>");
            for(String file : font.files()) {
                Object[] args = {file};
                filesSb.append(downloadLinkFormat.format(args));
            }
            Object[] args = {font.name(), filesSb.toString()};
            sb.append(downloadSectionFormat.format(args));
        }
        
        sb.append(footer);
        Files.write(output, sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
