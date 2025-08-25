package org.dino.util;

public class Views {
	 // View usada para listagens
    public static class Lista {}

    // View usada para detalhes (herda os campos da lista também)
    public static class Detalhe extends Lista {}
}
