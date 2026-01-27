package com.example.awror;

public class Publicacion {

    public static final int TIPO_FOTO = 1;
    public static final int TIPO_TEXTO = 2;

    private int tipo;
    private String usuario;
    private int likes;
    private int comentarios;

    private String texto;
    private int imagen;

    public Publicacion(int tipo, String usuario, int likes, int comentarios, String texto, int imagen) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.likes = likes;
        this.comentarios = comentarios;
        this.texto = texto;
        this.imagen = imagen;
    }

    public int getTipo() {
        return tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public int getLikes() {
        return likes;
    }

    public int getComentarios() {
        return comentarios;
    }

    public String getTexto() {
        return texto;
    }

    public int getImagen() {
        return imagen;
    }
}
