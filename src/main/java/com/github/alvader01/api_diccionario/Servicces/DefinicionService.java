package com.github.alvader01.api_diccionario.Servicces;

import com.github.alvader01.api_diccionario.Entities.Definicion;
import com.github.alvader01.api_diccionario.Entities.Palabra;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Repositories.DefinicionRepository;
import com.github.alvader01.api_diccionario.Repositories.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefinicionService {

    @Autowired
    private DefinicionRepository definicionRepository;

    @Autowired
    private PalabraRepository palabraRepository;

    /* Este metodo obtiene todas las definiciones asociadas a una palabra a través de su ID.
     Busca en el repositorio de definiciones y devuelve todas las definiciones relacionadas.*/
    public List<Definicion> getDefinicionesByPalabraId(Long palabraId) {
        return definicionRepository.findByPalabraId(palabraId);
    }

    /* Este metodo agrega una nueva definición a una palabra.
     Recibe el ID de la palabra y la definición a agregar.
     Si no se encuentra la palabra, lanza una excepción RecordNotFoundException.*/
    public Definicion addDefinicion(Long palabraId, Definicion definicion) throws RecordNotFoundException {
        // Se busca la palabra por su ID en el repositorio de palabras.
        Palabra palabra = palabraRepository.findById(palabraId)
                .orElseThrow(() -> new RecordNotFoundException("No existe ninguna palabra con id: ", palabraId));

        // Se asigna la palabra encontrada a la definición.
        definicion.setPalabra(palabra);

        // Se guarda la nueva definición en el repositorio de definiciones y se retorna.
        return definicionRepository.save(definicion);
    }

    /* Este metodo elimina una definición existente.
     Recibe el ID de la definición a eliminar.
     Si la definición no se encuentra, lanza una excepción RecordNotFoundException.*/
    public void deleteDefinicion(Long id) throws RecordNotFoundException {
        // Se verifica si la definición con el ID dado existe en el repositorio.
        if (!definicionRepository.existsById(id)) {
            // Si no existe, se lanza una excepción.
            throw new RecordNotFoundException("No existe Definicion para el id: ", id);
        }

        // Si existe, se elimina la definición por su ID.
        definicionRepository.deleteById(id);
    }

    /* Este metodo crea una nueva palabra con definiciones asociadas.
     Recibe una definición que contiene una palabra nueva o existente.
     Si la palabra ya existe, lanza una excepción RecordNotFoundException.*/
    public Definicion createPalabraConDefiniciones(Definicion definicion) throws RecordNotFoundException {
        // Se obtiene la palabra asociada a la definición.
        Palabra palabra = definicion.getPalabra();

        // Se verifica si la palabra ya existe en el repositorio de palabras.
        if (palabraRepository.existsByTermino(palabra.getTermino())) {
            // Si ya existe, se lanza una excepción con un mensaje de error.
            throw new RecordNotFoundException("La palabra '" + palabra.getTermino() + "' ya existe.");
        }

        // Si la palabra no existe, se guarda en el repositorio de palabras.
        palabra = palabraRepository.save(palabra);

        // Se asigna la palabra guardada a la definición.
        definicion.setPalabra(palabra);

        // Se guarda la definición asociada a la palabra y se retorna.
        return definicionRepository.save(definicion);
    }
}
