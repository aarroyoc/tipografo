import java.util.List;

public record Font(
    String name,
    String author,
    String description,
    String license,
    List<String> files
) {

}
