package com.github.alvader01.api_diccionario.Controllers;

import com.github.alvader01.api_diccionario.Entities.Palabra;
import com.github.alvader01.api_diccionario.Exceptions.CategoriaNotFoundException;
import com.github.alvader01.api_diccionario.Exceptions.InicialNotFoundException;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Servicces.PalabraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @CrossOrigin
    @GetMapping
    @Operation(
            summary = "Obtener todas las palabras",
            description = "Este método devuelve todas las palabras almacenadas en el sistema."
    )
    public ResponseEntity<List<Palabra>> findAll() {
        List<Palabra> list = palabraService.getAllPalabras();
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar palabra por ID",
            description = "Este método busca una palabra por su ID."
    )
    public ResponseEntity<Palabra> getPalabraById(
            @Parameter(description = "ID de la palabra a buscar", required = true)
            @PathVariable Long id) throws RecordNotFoundException {
        Palabra palabra = palabraService.getPalabraById(id);
        return new ResponseEntity<>(palabra, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping
    @Operation(
            summary = "Crear una nueva palabra",
            description = "Este método crea una nueva palabra."
    )
    public ResponseEntity<?> createPalabra(
            @Parameter(description = "Palabra a crear", required = true)
            @RequestBody Palabra palabra) {
        try {
            Palabra nuevaPalabra = palabraService.createPalabra(palabra);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPalabra);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @CrossOrigin
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar palabra",
            description = "Este método actualiza una palabra existente."
    )
    public ResponseEntity<?> updatePalabra(
            @Parameter(description = "Palabra a actualizar", required = true)
            @RequestBody Palabra palabra) {
        try {
            Palabra updatedPalabra = palabraService.updatePalabra(palabra);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar palabra",
            description = "Este método elimina una palabra mediante su ID."
    )
    public ResponseEntity<?> deletePalabra(
            @Parameter(description = "ID de la palabra a eliminar", required = true)
            @PathVariable Long id) {
        try {
            palabraService.deletePalabra(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.singletonMap("message", "La palabra con id " + id + " ha sido eliminada."));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No existe Palabra para el id: " + id));
        }
    }

    @CrossOrigin
    @GetMapping("/categoria/{categoria}")
    @Operation(
            summary = "Buscar palabras por categoría",
            description = "Este método busca palabras por su categoría gramatical."
    )
    public ResponseEntity<?> getPalabrasByCategoria(
            @Parameter(description = "Categoría gramatical de las palabras", required = true)
            @PathVariable String categoria) {
        try {
            List<Palabra> palabras = palabraService.getPalabrasByCategoria(categoria);
            if (palabras.isEmpty()) {
                throw new CategoriaNotFoundException("No existe ninguna categoria con el nombre: " + categoria);
            }
            return new ResponseEntity<>(palabras, new HttpHeaders(), HttpStatus.OK);
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @CrossOrigin
    @GetMapping("/inicial/{letra}")
    @Operation(
            summary = "Buscar palabras por inicial",
            description = "Este método busca palabras que comienzan con una letra inicial proporcionada."
    )
    public ResponseEntity<?> getPalabrasByInicial(
            @Parameter(description = "Letra inicial de las palabras", required = true)
            @PathVariable String letra) {
        try {
            List<Palabra> palabras = palabraService.getPalabrasByInicial(letra);
            return new ResponseEntity<>(palabras, new HttpHeaders(), HttpStatus.OK);
        } catch (InicialNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
