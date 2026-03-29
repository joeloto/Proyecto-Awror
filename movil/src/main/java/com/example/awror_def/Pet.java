package com.example.awror_def;

public class Pet {
    private int id;
    private int user_id;
    private String nombre;
    private String tipo;
    private String imagen;
    private String fecha;

    public Pet() {
    }

    public Pet(int user_id, String nombre, String tipo, String imagen) {
        this.user_id = user_id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getUser_id() {

        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}