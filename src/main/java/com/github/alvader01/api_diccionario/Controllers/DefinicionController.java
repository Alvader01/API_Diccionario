package com.github.alvader01.api_diccionario.Controllers;


import com.github.alvader01.api_diccionario.Entities.Definicion;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Servicces.DefinicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/definiciones")
public class DefinicionController {
    @Autowired
    private DefinicionService definicionService;

    @CrossOrigin
    @GetMapping("/palabra/{palabraId}")
    public ResponseEntity<List<Definicion>> getDefiniciones(@PathVariable Long palabraId) {
        List<Definicion> list = definicionService.getDefinicionesByPalabraId(palabraId);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/palabra/{palabraId}")
    public ResponseEntity<Definicion> addDefinicion(@PathVariable Long palabraId, @RequestBody Definicion definicion)
            throws RecordNotFoundException {
        Definicion createdDefinicion = definicionService.addDefinicion(palabraId, definicion);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDefinicion);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public HttpStatus deleteDefinicion(@PathVariable Long id) throws RecordNotFoundException {
        definicionService.deleteDefinicion(id);
        return HttpStatus.ACCEPTED;
    }
}