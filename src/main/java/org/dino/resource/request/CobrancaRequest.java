package org.dino.resource.request;

public class CobrancaRequest {
    public Calendario calendario;
    public Devedor devedor;
    public Valor valor;
    public String chave;

    public static class Calendario {
        public String dataDeVencimento;
    }

    public static class Devedor {
        public String cpf;
        public String nome;
    }

    public static class Valor {
        public String original;
    }
}