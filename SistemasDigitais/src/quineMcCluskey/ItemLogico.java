package quineMcCluskey;

public class ItemLogico {
    private final String codigoBin;
    private boolean foiAgrupado;

    public ItemLogico(String codigoBin) {
        this.codigoBin = codigoBin;
        this.foiAgrupado = false;
    }

    public int obterQtdUns() {
        int total = 0;
        for (int i = 0; i < codigoBin.length(); i++) {
            if (codigoBin.charAt(i) == '1') {
                total++;
            }
        }
        return total;
    }

    public ItemLogico combinar(ItemLogico outro) {
        if (this.codigoBin.length() != outro.codigoBin.length()) {
            return null;
        }

        int diferencas = 0;
        int posicaoDif = -1;

        for (int i = 0; i < this.codigoBin.length(); i++) {
            if (this.codigoBin.charAt(i) != outro.codigoBin.charAt(i)) {
                diferencas++;
                posicaoDif = i;
            }
        }

        if (diferencas == 1) {
            this.foiAgrupado = true;
            outro.foiAgrupado = true;

            char[] novoBits = this.codigoBin.toCharArray();
            novoBits[posicaoDif] = '-';

            return new ItemLogico(new String(novoBits));
        }
        return null;
    }

    public String getCodigoBin() { return codigoBin; }
    public boolean isFoiAgrupado() { return foiAgrupado; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemLogico)) return false;
        ItemLogico outro = (ItemLogico) obj;
        return this.codigoBin.equals(outro.codigoBin);
    }

    @Override
    public int hashCode() { return this.codigoBin.hashCode(); }

    @Override
    public String toString() { return codigoBin; }
}