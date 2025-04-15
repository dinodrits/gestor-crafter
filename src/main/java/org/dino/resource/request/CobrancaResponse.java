package org.dino.resource.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class CobrancaResponse {

    private String status;
    private String txid;
    private int revisao;
    private Recebedor recebedor;
    @JsonProperty("pixCopiaECola")
    private String pixCopiaECola;
    private Loc loc;
    private Devedor devedor;
    private Calendario calendario;
    private Valor valor;
    private String chave;

    // Getters e Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public int getRevisao() {
        return revisao;
    }

    public void setRevisao(int revisao) {
        this.revisao = revisao;
    }

    public Recebedor getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(Recebedor recebedor) {
        this.recebedor = recebedor;
    }

    public String getPixCopiaECola() {
        return pixCopiaECola;
    }

    public void setPixCopiaECola(String pixCopiaECola) {
        this.pixCopiaECola = pixCopiaECola;
    }

    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
    }

    public Devedor getDevedor() {
        return devedor;
    }

    public void setDevedor(Devedor devedor) {
        this.devedor = devedor;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public Valor getValor() {
        return valor;
    }

    public void setValor(Valor valor) {
        this.valor = valor;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    // Classes internas para os objetos aninhados
    public static class Recebedor {
        private String logradouro;
        private String cidade;
        private String uf;
        private String cep;
        private String cnpj;
        private String nomeFantasia;
        private String nome;

        // Getters e Setters
        public String getLogradouro() {
            return logradouro;
        }

        public void setLogradouro(String logradouro) {
            this.logradouro = logradouro;
        }

        public String getCidade() {
            return cidade;
        }

        public void setCidade(String cidade) {
            this.cidade = cidade;
        }

        public String getUf() {
            return uf;
        }

        public void setUf(String uf) {
            this.uf = uf;
        }

        public String getCep() {
            return cep;
        }

        public void setCep(String cep) {
            this.cep = cep;
        }

        public String getCnpj() {
            return cnpj;
        }

        public void setCnpj(String cnpj) {
            this.cnpj = cnpj;
        }

        public String getNomeFantasia() {
            return nomeFantasia;
        }

        public void setNomeFantasia(String nomeFantasia) {
            this.nomeFantasia = nomeFantasia;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public static class Loc {
        private int id;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SS][.S][.SSS]['Z']")
        private LocalDateTime criacao;
        
        private String location;
        private String tipoCob;

        // Getters e Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public LocalDateTime getCriacao() {
            return criacao;
        }

        public void setCriacao(LocalDateTime criacao) {
            this.criacao = criacao;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTipoCob() {
            return tipoCob;
        }

        public void setTipoCob(String tipoCob) {
            this.tipoCob = tipoCob;
        }
    }

    public static class Devedor {
        private String nome;
        private String cpf;

        // Getters e Setters
        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }
    }

    public static class Calendario {
    	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SS][.S][.SSS]['Z']")
        private LocalDateTime criacao;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private String dataDeVencimento;
        
        private String validadeAposVencimento;

        // Getters e Setters
        public LocalDateTime getCriacao() {
            return criacao;
        }

        public void setCriacao(LocalDateTime criacao) {
            this.criacao = criacao;
        }

        public String getDataDeVencimento() {
            return dataDeVencimento;
        }

        public void setDataDeVencimento(String dataDeVencimento) {
            this.dataDeVencimento = dataDeVencimento;
        }

        public String getValidadeAposVencimento() {
            return validadeAposVencimento;
        }

        public void setValidadeAposVencimento(String validadeAposVencimento) {
            this.validadeAposVencimento = validadeAposVencimento;
        }
    }

    public static class Valor {
        private String original;

        // Getters e Setters
        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
    }
}