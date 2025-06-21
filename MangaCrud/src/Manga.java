import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Manga {
    String isbn = "";
    String titulo = "";
    String autores = "";
    int anoInicio = 0;
    int anoFim = 0;
    String genero = "";
    String revista = "";
    String editora = "";
    int anoEdicao = 0;
    int qtVolumes = 0;
    int qtVolumesAdquiridos = 0;
    List<Integer> volumesAdquiridos = Collections.emptyList();

    public String toLine() {
        String volumesFormatados = volumesAdquiridos.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        return String.join(";", List.of(
                isbn, titulo, autores,
                String.valueOf(anoInicio),
                (anoFim <= 0) ? "-" : String.valueOf(anoFim),
                genero, revista, editora,
                String.valueOf(anoEdicao),
                String.valueOf(qtVolumes),
                String.valueOf(qtVolumesAdquiridos),
                volumesFormatados
        ));
    }

    private static int parseIntOrDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Manga fromLine(String line) {
        String[] parts = line.split(";", -1);
        Manga m = new Manga();

        if (parts.length > 0) m.isbn = parts[0].trim();
        if (parts.length > 1) m.titulo = parts[1].trim();
        if (parts.length > 2) m.autores = parts[2].trim();
        if (parts.length > 3) m.anoInicio = parseIntOrDefault(parts[3], 0);
        if (parts.length > 4) m.anoFim = parseIntOrDefault(parts[4], 0);
        if (parts.length > 5) m.genero = parts[5].trim();
        if (parts.length > 6) m.revista = parts[6].trim();
        if (parts.length > 7) m.editora = parts[7].trim();
        if (parts.length > 8) m.anoEdicao = parseIntOrDefault(parts[8], 0);
        if (parts.length > 9) m.qtVolumes = parseIntOrDefault(parts[9], 0);
        if (parts.length > 10) m.qtVolumesAdquiridos = parseIntOrDefault(parts[10], 0);

        if (parts.length > 11) {
            String volumesStr = parts[11].trim();
            if (volumesStr.length() > 2 && volumesStr.startsWith("[") && volumesStr.endsWith("]")) {
                String cleanedVolumesStr = volumesStr.substring(1, volumesStr.length() - 1);

                if (!cleanedVolumesStr.isEmpty()) {
                    m.volumesAdquiridos = Arrays.stream(cleanedVolumesStr.split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .toList();
                }
            }
        }
        return m;
    }
}