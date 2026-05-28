package quineMcCluskey;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Agrupamentos extends Thread {

    private final Set<ItemLogico> blocoA;
    private final Set<ItemLogico> blocoB;
    private final Map<Integer, Set<ItemLogico>> proximaFase;

    public Agrupamentos(String identificador, Set<ItemLogico> blocoA, Set<ItemLogico> blocoB, Map<Integer, Set<ItemLogico>> proximaFase) {
        super(identificador);
        this.blocoA = blocoA;
        this.blocoB = blocoB;
        this.proximaFase = proximaFase;
    }

    @Override
    public void run() {
        for (ItemLogico itemA : blocoA) {
            for (ItemLogico itemB : blocoB) {
                ItemLogico resultado = itemA.combinar(itemB);
                
                if (resultado != null) {
                    int chaveGrupo = resultado.obterQtdUns();
                    
                    if (!proximaFase.containsKey(chaveGrupo)) {
                        proximaFase.putIfAbsent(chaveGrupo, ConcurrentHashMap.newKeySet());
                    }
                    proximaFase.get(chaveGrupo).add(resultado);
                }
            }
        }
    }
}