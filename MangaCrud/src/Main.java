import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static MangaManager manager = new MangaManager();

    public static void main(String[] args) {

        int opcao;
        do {
            mostrarMenu();
            opcao = lerInt("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> adicionarManga();
                case 2 -> buscarPorISBN();
                case 3 -> buscarPorTitulo();
                case 4 -> listarTodos();
                case 5 -> editarManga();
                case 6 -> apagarManga();
                case 0 -> System.out.println("Encerrando o programa...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== GERENCIADOR DE MANGÁS =====");
        System.out.println("1. Adicionar novo mangá");
        System.out.println("2. Buscar por ISBN");
        System.out.println("3. Buscar por título");
        System.out.println("4. Listar todos os mangás");
        System.out.println("5. Editar mangá");
        System.out.println("6. Apagar mangá");
        System.out.println("0. Sair");
    }

    private static void adicionarManga() {
        Manga m = lerDadosManga();
        manager.addManga(m);
        System.out.println("Mangá adicionado com sucesso!");
    }

    private static void buscarPorISBN() {
        System.out.print("Digite o ISBN: ");
        String isbn = scanner.nextLine();
        Manga m = manager.getByISBN(isbn);
        if (m != null) {
            System.out.println(m.toLine());
        } else {
            System.out.println("Mangá não encontrado.");
        }
    }

    private static void buscarPorTitulo() {
        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine();
        List<Manga> resultados = manager.searchByTitle(titulo);
        if (resultados.isEmpty()) {
            System.out.println("Nenhum mangá encontrado...");
        } else {
            for (Manga m : resultados) {
                System.out.println("\n" + m.toLine());
            }
        }
    }

    private static void listarTodos() {
        List<Manga> todos = manager.listAll();
        if (todos.isEmpty()) {
            System.out.println("Nenhum mangá cadastrado.");
        } else {
            for (Manga m : todos) {
                System.out.println(m.toLine());
            }
        }
    }

    private static void editarManga() {
        System.out.print("Digite o ISBN do mangá a ser editado: ");
        String isbn = scanner.nextLine();
        Manga existente = manager.getByISBN(isbn);
        if (existente == null) {
            System.out.println("Mangá não encontrado.");
            return;
        }

        System.out.println("Digite os novos dados do mangá:");
        Manga novo = lerDadosManga();
        if (manager.updateManga(isbn, novo)) {
            System.out.println("Mangá atualizado com sucesso.");
        } else {
            System.out.println("Falha ao atualizar.");
        }
    }

    private static void apagarManga() {
        System.out.print("Digite o ISBN do mangá a ser apagado: ");
        String isbn = scanner.nextLine();
        Manga m = manager.getByISBN(isbn);
        if (m == null) {
            System.out.println("Mangá não encontrado.");
            return;
        }

        System.out.println("Mangá encontrado: " + m.toLine());
        System.out.print("Tem certeza que deseja apagar? (s/n): ");
        String resp = scanner.nextLine();
        if (resp.equalsIgnoreCase("s")) {
            if (manager.deleteManga(isbn)) {
                System.out.println("Mangá apagado com sucesso.");
            } else {
                System.out.println("Erro ao apagar.");
            }
        } else {
            System.out.println("Ação cancelada.");
        }
    }

    private static Manga lerDadosManga() {
        Manga m = new Manga();

        System.out.print("ISBN: ");
        m.isbn = scanner.nextLine();

        System.out.print("Título: ");
        m.titulo = scanner.nextLine();

        System.out.print("Autor(es): ");
        m.autores = scanner.nextLine();

        m.anoInicio = lerInt("Ano de início: ");
        m.anoFim = lerInt("Ano de fim: ");

        System.out.print("Gênero: ");
        m.genero = scanner.nextLine();

        System.out.print("Revista: ");
        m.revista = scanner.nextLine();

        System.out.print("Editora: ");
        m.editora = scanner.nextLine();

        m.anoEdicao = lerInt("Ano da edição: ");
        m.qtVolumes = lerInt("Quantidade de volumes: ");
        m.qtVolumesAdquiridos = lerInt("Quantidade de volumes adquiridos: ");

        System.out.print("Lista de volumes adquiridos (ex: 1,2,3): ");
        String[] partes = scanner.nextLine().split(",");
        m.volumesAdquiridos = new ArrayList<>();
        for (String s : partes) {
            try {
                m.volumesAdquiridos.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido: " + s + " (ignorando)");
            }
        }

        return m;
    }

    private static int lerInt(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

}