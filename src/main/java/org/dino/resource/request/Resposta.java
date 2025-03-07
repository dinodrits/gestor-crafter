package org.dino.resource.request;

public class Resposta {

	private String mensagem;
    private int status;
    
    // Construtor
    public Resposta(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
    }
    
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    
    
}
