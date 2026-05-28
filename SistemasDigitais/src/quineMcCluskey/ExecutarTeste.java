package quineMcCluskey;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ExecutarTeste {
    public static void main(String[] args) {
        // Defina o caminho do arquivo do teste atual
        String caminhoArquivo = "src/arquivosTeste/truth-table-sumula.pla"; 
        
        LeitorPLA leitor = new LeitorPLA();
        
        try {
            System.out.println("Carregando benchmark...");
            leitor.carregarArquivo(caminhoArquivo);
            
            int variaveis = leitor.getQtdVariaveis();
            List<String> cubosAtivos = leitor.getCubosAtivos();
            String[] rótulosVariáveis = leitor.getNomesVariaveis();
            
            if (variaveis == 0 || cubosAtivos.isEmpty()) {
                System.out.println("[AVISO] Arquivo inválido ou sem termos ativos.");
                return;
            }
            
            System.out.println("[SISTEMA] Variáveis identificadas: " + variaveis);
            System.out.println("[SISTEMA] Mintermos ativos (1): " + cubosAtivos.size());
            System.out.println("[SISTEMA] Minimizando com Agrupamentos Concorrentes...");

            AlgoritmoQM qm = new AlgoritmoQM(variaveis, cubosAtivos);
            
            long tempoInicio = System.currentTimeMillis();
            Set<ItemLogico> primos = qm.executarMinimizacao();
            long tempoFim = System.currentTimeMillis();
            
            long tempoAlgoritmoMs = tempoFim - tempoInicio;

            // IMPRESSÃO DOS RESULTADOS CONFORME DIRETRIZES DO LAB 06
            System.out.println("\n==================================================");
            System.out.println("    RESULTADO DA MINIMIZAÇÃO DE Quine-McCluskey     ");
            System.out.println("==================================================");
            System.out.println(" > Tempo gasto na minimização: " + tempoAlgoritmoMs + " ms");
            System.out.println(" > Total de Implicantes Primos gerados: " + primos.size());
            System.out.println("--------------------------------------------------");
            System.out.println(" [Lista de Cubos Reduzidos]");
            
            int contador = 1;
            for (ItemLogico termo : primos) {
                if (contador <= 50) {
                    System.out.println("   Termo " + contador + ": " + termo.getCodigoBin());
                } else {
                    System.out.println("   ... Amostra limitada para a consola.");
                    break;
                }
                contador++;
            }
            
            System.out.println("--------------------------------------------------");
            System.out.println(" [Expressão Booleana Dinâmica Simplificada]");
            System.out.print("   F = ");
            
            int iTermo = 0;
            for (ItemLogico termo : primos) {
                if (iTermo > 0) {
                    System.out.print(" + ");
                }
                
                // Passa o cubo e o array com os nomes identificados do arquivo
                System.out.print(converterCuboParaExpressao(termo.getCodigoBin(), rótulosVariáveis));
                iTermo++;
                
                if (iTermo >= 30) {
                    System.out.print(" + ... [Expressão extensa]");
                    break;
                }
            }
            System.out.println("\n==================================================");
            
        } catch (IOException e) {
            System.err.println("[ERRO] Falha ao ler o arquivo PLA.");
        } catch (InterruptedException e) {
            System.err.println("[ERRO] O processo de Threads falhou.");
        }
    }

    /**
     * Analisa o cubo bit a bit, identifica quais variáveis variam de fato (-) 
     * e monta o termo utilizando os rótulos reais identificados pelo Leitor.
     */
    private static String converterCuboParaExpressao(String cubo, String[] mapeamentoNomes) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < cubo.length(); i++) {
            char bit = cubo.charAt(i);
            
            // SE FOR '-': A variável varia de fato na tabela verdade, logo é ELIMINADA do termo.
            if (bit == '-') {
                continue; 
            }
            
            // Se houver mais variáveis no cubo do que nomes (por segurança), usamos índices genéricos
            String nomeVar = (i < mapeamentoNomes.length) ? mapeamentoNomes[i] : ("v" + i);
            
            if (bit == '1') {
                sb.append(nomeVar);
            } else if (bit == '0') {
                sb.append(nomeVar).append("'"); // Adiciona o sinal de negação para bit zero
            }
        }
        
        if (sb.length() == 0) {
            return "1"; // Se todas as variáveis variarem, o termo simplifica-se na constante lógica 1
        }
        return "(" + sb.toString() + ")";
    }
}