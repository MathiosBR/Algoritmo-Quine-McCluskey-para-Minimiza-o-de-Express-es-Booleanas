package quineMcCluskey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorPLA {

    private int qtdVariaveis = 0;
    private final List<String> cubosAtivos = new ArrayList<>();
    private String[] nomesVariaveis = null; // Guarda os nomes reais das variáveis (.ilb)

    public void carregarArquivo(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            
            while ((linha = br.readLine()) != null) {
                linha = tratarEspacos(linha);
                
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                
                if (linha.startsWith(".i")) {
                    String[] partes = linha.split("\\s+");
                    if (partes.length > 1) {
                        this.qtdVariaveis = Integer.parseInt(partes[1]);
                    }
                } else if (linha.startsWith(".ilb")) {
                    // Captura os nomes reais definidos no arquivo PLA
                    String[] partes = linha.split("\\s+");
                    nomesVariaveis = new String[partes.length - 1];
                    System.arraycopy(partes, 1, nomesVariaveis, 0, nomesVariaveis.length);
                } else if (linha.startsWith(".o") || linha.startsWith(".p") || linha.startsWith(".type") || linha.startsWith(".ob")) {
                    continue;
                } else if (linha.startsWith(".e")) {
                    break;
                } else {
                    String[] partes = linha.split("\\s+");
                    if (partes.length >= 2) {
                        String cuboBinario = partes[0];
                        String valorSaida = partes[1];
                        
                        if (valorSaida.equals("1")) {
                            cubosAtivos.add(cuboBinario);
                        }
                    }
                }
            }
        }
        
        // Se o arquivo não definiu .ilb, geramos nomes dinâmicos baseados no índice
        if (nomesVariaveis == null && qtdVariaveis > 0) {
            nomesVariaveis = new String[qtdVariaveis];
            for (int i = 0; i < qtdVariaveis; i++) {
                nomesVariaveis[i] = "v" + i;
            }
        }
    }

    private String tratarEspacos(String str) {
        if (str == null) return "";
        return str.trim();
    }

    public int getQtdVariaveis() { return qtdVariaveis; }
    public List<String> getCubosAtivos() { return cubosAtivos; }
    public String[] getNomesVariaveis() { return nomesVariaveis; }
}