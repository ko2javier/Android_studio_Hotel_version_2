package com.example.hotel_hw_1.dto;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class UsuarioEntity {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String email;  // PK natural

    private String pass;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String tipo_usuario;

    // Constructor vacío (obligatorio para Room)
    public UsuarioEntity() {}

    // Constructor útil
    public UsuarioEntity(String email, String pass, String nombre,
                         String apellidos, String telefono, String tipo_usuario) {

        this.email = email;
        this.pass = pass;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.tipo_usuario = tipo_usuario;
    }

    // Getters / Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipo_usuario() { return tipo_usuario; }
    public void setTipo_usuario(String tipo_usuario) { this.tipo_usuario = tipo_usuario; }
}
