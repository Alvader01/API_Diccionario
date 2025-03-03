package com.github.alvader01.api_diccionario.Controllers;

import com.github.alvader01.api_diccionario.Entities.Definicion;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Servicces.DefinicionService;
import com.github.alvader01.api_diccionario.Servicces.PalabraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/definiciones")
public class DefinicionController {

    @Autowired
    private DefinicionService definicionService;

    @Autowired
    private PalabraService palabraService;

    /* Metodo que obtiene todas las definiciones asociadas a una palabra mediante su ID.
     Si no se encuentran definiciones para la palabra, se devuelve un error 404.*/
    @CrossOrigin
    @GetMapping("/palabra/{palabraId}")
    public ResponseEntity<?> getDefiniciones(@PathVariable Long palabraId) {
        // Se obtiene la lista de definiciones asociadas a la palabra con el ID proporcionado.
        List<Definicion> list = definicionService.getDefinicionesByPalabraId(palabraId);

        // Si la lista de definiciones está vacía, se devuelve un error 404 con un mensaje.
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se encontraron definiciones para la palabra con ID: " + palabraId));
        }

        // Si se encuentran definiciones, se devuelve la lista con un estado HTTP 200 (OK).
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /* Metodo que agrega una nueva definición para una palabra existente.
     Si no se encuentra la palabra, se lanza una excepción y se devuelve un error 404.*/
    @CrossOrigin
    @PostMapping("/palabra/{palabraId}")
    public ResponseEntity<?> addDefinicion(@PathVariable Long palabraId, @RequestBody Definicion definicion) {
        try {
            // Se agrega la definición a la palabra con el ID proporcionado.
            Definicion createdDefinicion = definicionService.addDefinicion(palabraId, definicion);
            // Si la definición se agrega correctamente, se devuelve con un estado HTTP 201 (CREATED).
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDefinicion);
        } catch (RecordNotFoundException e) {
            // Si no se encuentra la palabra con el ID dado, se captura la excepción y se devuelve un error 404 con un mensaje.
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    /* Metodo que elimina una definición específica según su ID.
     Si no se encuentra la definición a eliminar, se lanza una excepción y se devuelve un error 404.*/
    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDefinicion(@PathVariable Long id) {
        try {
            // Se intenta eliminar la definición con el ID proporcionado.
            definicionService.deleteDefinicion(id);
            // Si la eliminación es exitosa, se devuelve un mensaje de éxito con estado HTTP 202 (ACCEPTED).
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.singletonMap("message", "Definición eliminada con éxito."));
        } catch (RecordNotFoundException e) {
            // Si no se encuentra la definición con el ID dado, se captura la excepción y se devuelve un error 404 con un mensaje.
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    /* Metodo para crear una nueva palabra junto con sus definiciones asociadas.
     Si hay algún problema en la creación de la palabra y las definiciones, se devuelve un error 400.*/
    @CrossOrigin
    @PostMapping("/con-definiciones")
    public ResponseEntity<?> createPalabraConDefiniciones(@RequestBody Definicion definicion) {
        try {
            // Se intenta crear una palabra con las definiciones proporcionadas.
            Definicion createdDefinicion = definicionService.createPalabraConDefiniciones(definicion);
            // Si la creación es exitosa, se devuelve la definición creada con un estado HTTP 201 (CREATED).
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDefinicion);
        } catch (RecordNotFoundException e) {
            /* Si ocurre un error (por ejemplo, la palabra no se encuentra o hay un problema con los datos),
             se captura la excepción y se devuelve un error 400 con un mensaje.*/
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
