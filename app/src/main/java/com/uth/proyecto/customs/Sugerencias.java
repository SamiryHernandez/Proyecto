package com.uth.proyecto.customs;

public class Sugerencias {

    private int foto; // Cambia el tipo de dato según cómo manejes las fotos
    private String nombre;
    private String apellido;
    private String pais;

    public Sugerencias(int foto, String nombre, String apellido, String pais) {
        this.foto = foto;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
