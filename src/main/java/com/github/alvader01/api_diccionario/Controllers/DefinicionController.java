package com.github.alvader01.api_diccionario.Controllers;

import com.github.alvader01.api_diccionario.Entities.Definicion;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Services.DefinicionService;
import com.github.alvader01.api_diccionario.Services.PalabraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @CrossOrigin
    @GetMapping("/palabra/{palabraId}")
    @Operation(
            summary = "Obtener definiciones de una palabra",
            description = "Este método obtiene todas las definiciones asociadas a una palabra, dada su ID."
    )
    public ResponseEntity<?> getDefiniciones(
            @Parameter(description = "ID de la palabra para obtener sus definiciones", required = true)
            @PathVariable Long palabraId) {

        List<Definicion> list = definicionService.getDefinicionesByPalabraId(palabraId);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "No se encontraron definiciones para la palabra con ID: " + palabraId));
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/palabra/{palabraId}")
    @Operation(
            summary = "Agregar definición para una palabra",
            description = "Este método agrega una nueva definición para la palabra con el ID proporcionado."
    )
    public ResponseEntity<?> addDefinicion(
            @Parameter(description = "ID de la palabra para agregar la definición", required = true)
            @PathVariable Long palabraId,
            @Parameter(description = "Definición a agregar", required = true)
            @RequestBody Definicion definicion) {

        try {
            Definicion createdDefinicion = definicionService.addDefinicion(palabraId, definicion);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDefinicion);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar definición",
            description = "Este método elimina la definición correspondiente al ID proporcionado."
    )
    public ResponseEntity<?> deleteDefinicion(
            @Parameter(description = "ID de la definición a eliminar", required = true)
            @PathVariable Long id) {

        try {
            definicionService.deleteDefinicion(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.singletonMap("message", "Definición eliminada con éxito."));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @CrossOrigin
    @PostMapping("/con-definiciones")
    @Operation(
            summary = "Crear palabra con definiciones",
            description = "Este método crea una nueva palabra junto con las definiciones proporcionadas."
    )
    public ResponseEntity<?> createPalabraConDefiniciones(
            @Parameter(description = "Definición para la palabra a crear", required = true)
            @RequestBody Definicion definicion) {

        try {
            Definicion createdDefinicion = definicionService.createPalabraConDefiniciones(definicion);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDefinicion);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}