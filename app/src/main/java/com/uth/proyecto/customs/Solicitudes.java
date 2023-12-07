package com.uth.proyecto.customs;

public class Solicitudes {
        private int foto; // Cambia el tipo de dato según cómo manejes las fotos
        private String nombre;
        private String apellido;
        private String pais;

        public Solicitudes(int foto, String nombre, String apellido, String pais) {
            this.foto = foto;
            this.nombre = nombre;
            this.apellido = apellido;
            this.pais = pais;
        }

        public int getFoto() {
            return foto;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getPais() {
            return pais;
        }
}