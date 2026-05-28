package quineMcCluskey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AlgoritmoQM {

    private final int qtdVariaveis;
    private final List<String> listaCubos;

    public AlgoritmoQM(int qtdVariaveis, List<String> listaCubos) {
        this.qtdVariaveis = qtdVariaveis;
        this.listaCubos = listaCubos;
    }

    public Set<ItemLogico> executarMinimizacao() throws InterruptedException {
        Map<Integer, Set<ItemLogico>> tabelaAtual = mapeamentoInicial();
        Set<ItemLogico> primosImplicantes = ConcurrentHashMap.newKeySet();

        boolean continuarFase = true;

        while (continuarFase) {
            continuarFase = false;
            Map<Integer, Set<ItemLogico>> proximaFase = new ConcurrentHashMap<>();
            List<Agrupamentos> listaThreads = new ArrayList<>();

            // Varre todos os grupos possíveis criados na fase de mapeamento
            for (int i = 0; i <= qtdVariaveis; i++) {
                Set<ItemLogico> bA = tabelaAtual.get(i);
                Set<ItemLogico> bB = tabelaAtual.get(i + 1);

                if (bA == null) bA = Collections.emptySet();
                if (bB == null) bB = Collections.emptySet();

                // Dispara a thread de agrupamento apenas se ambos os blocos vizinhos contiverem dados
                if (!bA.isEmpty() && !bB.isEmpty()) {
                    Agrupamentos worker = new Agrupamentos("Worker_Bloco_" + i, bA, bB, proximaFase);
                    listaThreads.add(worker);
                    worker.start();
                }
            }

            // Aguarda todas as tarefas paralelas terminarem o passo atual da tabela
            for (Agrupamentos t : listaThreads) {
                t.join();
            }

            // Isola os implicantes que não conseguiram sofrer redução
            for (Set<ItemLogico> subGrupo : tabelaAtual.values()) {
                for (ItemLogico item : subGrupo) {
                    if (!item.isFoiAgrupado()) {
                        primosImplicantes.add(item);
                    }
                }
            }

            if (!proximaFase.isEmpty()) {
                continuarFase = true;
                tabelaAtual = proximaFase;
            }
        }

        return primosImplicantes;
    }

    private Map<Integer, Set<ItemLogico>> mapeamentoInicial() {
        Map<Integer, Set<ItemLogico>> mapaInicial = new ConcurrentHashMap<>();
        
        for (String cuboBinario : listaCubos) {
            ItemLogico novoItem = new ItemLogico(cuboBinario);
            int chave = novoItem.obterQtdUns();

            if (!mapaInicial.containsKey(chave)) {
                mapaInicial.putIfAbsent(chave, ConcurrentHashMap.newKeySet());
            }
            mapaInicial.get(chave).add(novoItem);
        }
        return mapaInicial;
    }
}