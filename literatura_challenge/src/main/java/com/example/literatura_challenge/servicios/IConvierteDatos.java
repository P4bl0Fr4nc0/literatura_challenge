package com.example.literatura_challenge.servicios;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);

}
