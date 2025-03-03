package com.github.alvader01.api_diccionario.Controllers;

import com.github.alvader01.api_diccionario.Entities.Palabra;
import com.github.alvader01.api_diccionario.Exceptions.CategoriaNotFoundException;
import com.github.alvader01.api_diccionario.Exceptions.InicialNotFoundException;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Servicces.PalabraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/palabras")
public class PalabraController {

    @Autowired
    private PalabraService palabraService;

    /* Metodo que devuelve todas las palabras almacenadas en el sistema.
     Si no hay palabras, devuelve una lista vacía con un estado 200 (OK).*/
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<Palabra>> findAll() {
        // Se obtiene la lista de todas las palabras desde el servicio.
        List<Palabra> list = palabraService.getAllPalabras();
        // Se devuelve la lista con un estado HTTP 200 (OK).
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    /* Metodo que busca una palabra por su ID.
     Si no se encuentra, lanza una excepción RecordNotFoundException.*/
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Palabra> getPalabraById(@PathVariable Long id) throws RecordNotFoundException {
        // Se obtiene la palabra con el ID proporcionado.
        Palabra palabra = palabraService.getPalabraById(id);
        // Se devuelve la palabra encontrada con un estado HTTP 200 (OK).
        return new ResponseEntity<>(palabra, new HttpHeaders(), HttpStatus.OK);
    }

    /* Metodo que crea una nueva palabra.
     Si se pasa un dato incorrecto (por ejemplo, faltan campos), se devuelve un error 400.*/
    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> createPalabra(@RequestBody Palabra palabra) {
        try {
            // Se crea la nueva palabra a través del servicio.
            Palabra nuevaPalabra = palabraService.createPalabra(palabra);
            // Se devuelve la palabra creada con un estado HTTP 201 (CREATED).
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPalabra);
        } catch (IllegalArgumentException e) {
            // Si ocurre un error (por ejemplo, datos inválidos), se devuelve un error 400 con el mensaje de la excepción.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /* Metodo que actualiza una palabra existente.
     Si no se encuentra la palabra o si los datos son incorrectos, se lanza una excepción.*/
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePalabra(@RequestBody Palabra palabra) {
        try {
            // Se actualiza la palabra con los datos proporcionados.
            Palabra updatedPalabra = palabraService.updatePalabra(palabra);
            // Se devuelve la palabra actualizada con un estado HTTP 200 (OK).
            return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
        } catch (IllegalArgumentException e) {
            // Si los datos son incorrectos, se devuelve un error 400 con el mensaje de la excepción.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        } catch (RecordNotFoundException e) {
            // Si no se encuentra la palabra con el ID dado, se devuelve un error 404 con el mensaje de la excepción.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /* Metodo que elimina una palabra mediante su ID.
     Si no se encuentra la palabra, se lanza una excepción.*/
    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePalabra(@PathVariable Long id) {
        try {
            // Se elimina la palabra con el ID proporcionado.
            palabraService.deletePalabra(id);
            // Si la eliminación es exitosa, se devuelve un mensaje con estado HTTP 202 (ACCEPTED).
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.singletonMap("message", "La palabra con id " + id + " ha sido eliminada."));
        } catch (RecordNotFoundException e) {
            // Si no se encuentra la palabra con el ID dado, se devuelve un error 404 con el mensaje de la excepción.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No existe Palabra para el id: " + id));
        }
    }

    /* Metodo que busca palabras por su categoría gramatical (por ejemplo, sustantivo, verbo, etc.).
     Si no se encuentra la categoría, se lanza una excepción CategoriaNotFoundException.*/
    @CrossOrigin
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> getPalabrasByCategoria(@PathVariable String categoria) {
        try {
            // Se obtiene la lista de palabras asociadas a la categoría proporcionada.
            List<Palabra> palabras = palabraService.getPalabrasByCategoria(categoria);

            // Si no se encuentran palabras para esa categoría, se lanza una excepción.
            if (palabras.isEmpty()) {
                throw new CategoriaNotFoundException("No existe ninguna categoria con el nombre: " + categoria);
            }

            // Si se encuentran palabras, se devuelve la lista con estado HTTP 200 (OK).
            return new ResponseEntity<>(palabras, new HttpHeaders(), HttpStatus.OK);

        } catch (CategoriaNotFoundException e) {
            // Si ocurre un error (por ejemplo, no se encuentra la categoría), se devuelve un error 404 con el mensaje de la excepción.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    /* Metodo que busca palabras que comienzan con una letra inicial proporcionada.
     Si no se encuentran palabras que comiencen con esa letra, se lanza una excepción InicialNotFoundException.*/
    @CrossOrigin
    @GetMapping("/inicial/{letra}")
    public ResponseEntity<?> getPalabrasByInicial(@PathVariable String letra) {
        try {
            // Se obtiene la lista de palabras que comienzan con la letra proporcionada.
            List<Palabra> palabras = palabraService.getPalabrasByInicial(letra);
            // Se devuelve la lista de palabras con estado HTTP 200 (OK).
            return new ResponseEntity<>(palabras, new HttpHeaders(), HttpStatus.OK);
        } catch (InicialNotFoundException e) {
            // Si no se encuentran palabras que empiecen con la letra proporcionada, se devuelve un error 404 con el mensaje de la excepción.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
