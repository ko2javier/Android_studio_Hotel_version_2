package com.example.hotel_hw_1.dto;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void insertar(UsuarioEntity usuario);

    @Query("SELECT * FROM usuarios")
    List<UsuarioEntity> getAll();

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    UsuarioEntity findByEmail(String email);

    @Query("SELECT * FROM usuarios WHERE email = :email AND pass = :pass LIMIT 1")
    UsuarioEntity login(String email, String pass);
}
