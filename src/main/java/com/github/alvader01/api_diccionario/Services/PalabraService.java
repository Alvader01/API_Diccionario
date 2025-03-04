package com.github.alvader01.api_diccionario.Services;

import com.github.alvader01.api_diccionario.Entities.Palabra;
import com.github.alvader01.api_diccionario.Exceptions.CategoriaNotFoundException;
import com.github.alvader01.api_diccionario.Exceptions.InicialNotFoundException;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Repositories.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PalabraService {

    @Autowired
    private PalabraRepository palabraRepository;

    /*Este metodo obtiene todas las palabras almacenadas en la base de datos.
    Utiliza el repositorio `palabraRepository` para realizar una búsqueda de todas las palabras.*/
    public List<Palabra> getAllPalabras() {
        return palabraRepository.findAllWithoutDefinitions();
    }

    /* Este metodo busca una palabra por su ID.
     Si la palabra no se encuentra, lanza una excepción `RecordNotFoundException`.*/
    public Palabra getPalabraById(Long id) throws RecordNotFoundException {
        return palabraRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No existe Palabra para el id: ", id));
    }

    /*Este metodo crea una nueva palabra.
     Primero verifica si la palabra ya existe en la base de datos utilizando el término de la palabra.
     Si ya existe, lanza una excepción `IllegalArgumentException`.
     Si no existe, guarda la nueva palabra en el repositorio y la devuelve.*/
    public Palabra createPalabra(Palabra palabra) {
        Optional<Palabra> existingPalabra = palabraRepository.findByTermino(palabra.getTermino());

        // Verificación si la palabra ya existe.
        if (existingPalabra.isPresent()) {
            throw new IllegalArgumentException("La palabra '" + palabra.getTermino() + "' ya existe.");
        }

        try {
            // Guardar la nueva palabra en la base de datos.
            return palabraRepository.save(palabra);
        } catch (DataIntegrityViolationException e) {
            // Si ocurre un error de integridad de datos, lanza una excepción con un mensaje de error.
            throw new IllegalArgumentException("Error al guardar la palabra: " + e.getMessage());
        }
    }

    /*Este metodo actualiza los detalles de una palabra existente.
     Recibe una palabra con el nuevo término y categoría gramatical.
     Si la palabra con el término proporcionado ya existe (y no es la misma palabra), lanza una excepción `IllegalArgumentException`.
     Si no existe, actualiza la palabra con los nuevos detalles y la guarda en el repositorio.*/
    public Palabra updatePalabra(Palabra palabra) throws RecordNotFoundException {
        return palabraRepository.findById(Long.valueOf(palabra.getId()))
                .map(existingPalabra -> {
                    // Verificar si la nueva palabra ya existe en la base de datos.
                    Optional<Palabra> palabraExistente = palabraRepository.findByTermino(palabra.getTermino());

                    // Si la palabra ya existe y no es la misma, lanza una excepción.
                    if (palabraExistente.isPresent() && !palabraExistente.get().getId().equals(palabra.getId())) {
                        throw new IllegalArgumentException("La palabra '" + palabra.getTermino() + "' ya existe.");
                    }

                    // Actualizar la palabra existente con los nuevos detalles.
                    existingPalabra.setTermino(palabra.getTermino());
                    existingPalabra.setCategoriaGramatical(palabra.getCategoriaGramatical());

                    // Guardar la palabra actualizada.
                    return palabraRepository.save(existingPalabra);
                })
                .orElseThrow(() -> new RecordNotFoundException("No existe Palabra con el id: ", palabra.getId()));
    }

    /* Este metodo elimina una palabra por su ID.
     Si la palabra no se encuentra en la base de datos, lanza una excepción `RecordNotFoundException`.*/
    public void deletePalabra(Long id) throws RecordNotFoundException {
        // Verificar si la palabra con el ID dado existe en la base de datos.
        if (!palabraRepository.existsById(id)) {
            throw new RecordNotFoundException("No existe Palabra con el id: ", id);
        }
        // Eliminar la palabra por su ID.
        palabraRepository.deleteById(id);
    }

    /* Este metodo busca todas las palabras asociadas a una categoría gramatical.
     Si no se encuentran palabras con la categoría proporcionada, lanza una excepción `CategoriaNotFoundException`.*/
    public List<Palabra> getPalabrasByCategoria(String categoria) {
        List<Palabra> palabras = palabraRepository.findByCategoriaGramatical(categoria);

        // Verificar si no se encontraron palabras con la categoría solicitada.
        if (palabras.isEmpty()) {
            throw new CategoriaNotFoundException("No existe ninguna categoria con el nombre: " + categoria);
        }

        // Retorna la lista de palabras encontradas.
        return palabras;
    }

    /* Este metodo busca todas las palabras que comienzan con una letra inicial específica.
     Si no se encuentran palabras con esa inicial, lanza una excepción `InicialNotFoundException`.*/
    public List<Palabra> getPalabrasByInicial(String letra) {
        List<Palabra> palabras = palabraRepository.findByInicial(letra);

        // Verificar si no se encontraron palabras con la inicial proporcionada.
        if (palabras.isEmpty()) {
            throw new InicialNotFoundException("No se encuentran palabras con la inicial: " + letra);
        }

        // Retorna la lista de palabras encontradas.
        return palabras;
    }
}
