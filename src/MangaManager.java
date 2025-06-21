import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MangaManager {

    private static final String DATA_FILE = "src/mangas.txt";
    private static final String PRIMARY_INDEX_FILE = "index_primary.txt";
    private static final String TITLE_INDEX_FILE = "index_title.txt";

    private final List<Manga> mangas = new ArrayList<>();

    public MangaManager() {
        loadData();
    }

    public void addManga(Manga m) {
        mangas.add(m);
        saveAll();
    }

    public Manga getByISBN(String isbn) {
        return mangas.stream()
                .filter(m -> m.isbn.equals(isbn))
                .findFirst()
                .orElse(null);
    }

    public List<Manga> searchByTitle(String title) {
        return mangas.stream()
                .filter(m -> m.titulo.equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    public boolean updateManga(String isbn, Manga novo) {
        for (int i = 0; i < mangas.size(); i++) {
            if (mangas.get(i).isbn.equals(isbn)) {
                mangas.set(i, novo);
                saveAll();
                return true;
            }
        }
        return false;
    }

    public boolean deleteManga(String isbn) {
        Iterator<Manga> it = mangas.iterator();
        while (it.hasNext()) {
            if (it.next().isbn.equals(isbn)) {
                it.remove();
                saveAll();
                return true;
            }
        }
        return false;
    }

    public List<Manga> listAll() {
        return mangas;
    }

    private void loadData() {
        mangas.clear();
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                mangas.add(Manga.fromLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAll() {
        saveMangas();
        rebuildIndices();
    }

    private void saveMangas() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Manga m : mangas) {
                pw.println(m.toLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rebuildIndices() {
        try (PrintWriter primary = new PrintWriter(new FileWriter(PRIMARY_INDEX_FILE));
             PrintWriter title = new PrintWriter(new FileWriter(TITLE_INDEX_FILE))) {

            for (int i = 0; i < mangas.size(); i++) {
                Manga m = mangas.get(i);
                primary.println(m.isbn + ";" + i);
                title.println(m.titulo + ";" + m.isbn);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printAll() {
        for (Manga m : mangas) {
            System.out.println(m.toLine());
        }
    }
}
