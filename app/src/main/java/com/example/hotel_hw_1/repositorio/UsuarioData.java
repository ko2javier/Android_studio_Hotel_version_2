/**
 * Autor: K. Jabier O'Reilly
 * Proyecto: Gestión de Hotel - Práctica 1ª Evaluación (PMDM 2025/2026)
 * Clase: UsuarioData.java

 */

package com.example.hotel_hw_1.repositorio;

import android.content.Context;
import android.util.Log;

import com.example.hotel_hw_1.dto.AppDatabase;
import com.example.hotel_hw_1.dto.UsuarioDao;
import com.example.hotel_hw_1.dto.UsuarioEntity;
import com.example.hotel_hw_1.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioData {
    private static List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Usuario("gerente@hotel.com", "1234", "gerente",
                "Pedrito", "Calvo", "666777888"));
        usuarios.add(new Usuario("recepcion@hotel.com", "1234", "recepcionista",
                "Ana", "Martínez", "666777888"));
        usuarios.add(new Usuario("limpieza@hotel.com", "1234", "limpieza",
                "Luis", "Pérez", "666111222"));
        usuarios.add(new Usuario("mantenimiento@hotel.com", "1234", "mantenimiento",
                "Marcos", "Gómez", "666333444"));
        usuarios.add(new Usuario("huesped_2@hotel.com", "1234", "huesped",
                "Juan", "Lorenzo", "699999999"));
        usuarios.add(new Usuario("huesped@hotel.com", "1234", "huesped",
                "Diana", "Rio", "699999999"));
    }

    /*Metodo para verificar los usuarios y las contraseñas!!*/
    public static Usuario checkLogin(Context ctx, String email, String pass) {
        //hago la instacia de la entidad para hacer la consulta
        UsuarioDao dao = AppDatabase.getInstance(ctx).usuarioDao();
        // compruebo que estan todos los usuarios!!!
        List<UsuarioEntity> lista = dao.getAll();
        Log.d("ROOM", "Usuarios en BD: " + lista.size());
        // hago la consulta!! para verificar el email
        UsuarioEntity entity = dao.login(email, pass);
        /*
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getPass().equals(pass)) {
                return u;
            }
        }*/
        if (entity == null) {
            return null; // Usuario NO existe o pass incorrecta
        }
        // Convertimos UsuarioEntity -> Usuario normal
        return new Usuario(
                entity.getEmail(),
                entity.getPass(),
                entity.getTipo_usuario(),
                entity.getNombre(),
                entity.getApellidos(),
                entity.getTelefono()
        );
    }

    public static void addUsuario(Usuario u) {
        usuarios.add(u);
    }
}
