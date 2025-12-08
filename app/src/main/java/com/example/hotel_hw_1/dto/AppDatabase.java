package com.example.hotel_hw_1.dto;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(
        entities = {UsuarioEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // ================================
    // ⚡ 1) CALLBACK → DEBE IR ARRIBA
    // ================================
    private static final RoomDatabase.Callback roomCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    Log.d("ROOM", "⚡ CALLBACK EJECUTADO: INSERTANDO USUARIOS");

                    // IMPORTANTE: NO USAR HILOS AQUÍ
                    UsuarioDao dao = instance.usuarioDao();

                    dao.insertar(new UsuarioEntity(
                            "gerente@hotel.com", "1234", "Pedrito", "Calvo", "666777888", "gerente"
                    ));

                    dao.insertar(new UsuarioEntity(
                            "recepcion@hotel.com", "1234", "Ana", "Martínez", "666777888", "recepcionista"
                    ));

                    dao.insertar(new UsuarioEntity(
                            "limpieza@hotel.com", "1234", "Luis", "Pérez", "666111222", "limpieza"
                    ));

                    dao.insertar(new UsuarioEntity(
                            "mantenimiento@hotel.com", "1234", "Marcos", "Gómez", "666333444", "mantenimiento"
                    ));

                    dao.insertar(new UsuarioEntity(
                            "huesped_2@hotel.com", "1234", "Juan", "Lorenzo", "699999999", "huesped"
                    ));

                    dao.insertar(new UsuarioEntity(
                            "huesped@hotel.com", "1234", "Diana", "Río", "699999999", "huesped"
                    ));
                }
            };


    // ================================
    // ⚡ 2) DAO
    // ================================
    public abstract UsuarioDao usuarioDao();

    // ================================
    // ⚡ 3) getInstance (instancia única)
    // ================================
    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "hotel.db"
                    )
                    .allowMainThreadQueries()   // SOLO EN DESARROLLO
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback)  // AHORA SÍ SE REGISTRA
                    .build();
        }
        return instance;
    }
}
