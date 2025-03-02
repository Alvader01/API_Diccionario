package com.github.alvader01.api_diccionario.Servicces;

import com.github.alvader01.api_diccionario.Entities.Palabra;
import com.github.alvader01.api_diccionario.Exceptions.RecordNotFoundException;
import com.github.alvader01.api_diccionario.Repositories.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PalabraService {
    @Autowired
    private PalabraRepository palabraRepository;

    public List<Palabra> getAllPalabras() {
        List<Palabra> palabras = palabraRepository.findAll();
        return palabras.isEmpty() ? new ArrayList<>() : palabras;
    }

    public Palabra getPalabraById(Long id) throws RecordNotFoundException {
        return palabraRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No existe Palabra para el id: ", id));
    }

    public Palabra createPalabra(Palabra palabra) {
        return palabraRepository.save(palabra);
    }

    public Palabra updatePalabra(Palabra palabra) throws RecordNotFoundException {
        if (palabra.getId() == null) {
            throw new RecordNotFoundException("No hay id en la palabra a actualizar", 0L);
        }
        return palabraRepository.findById(palabra.getId()).map(existingPalabra -> {
            existingPalabra.setTermino(palabra.getTermino());
            existingPalabra.setCategoriaGramatical(palabra.getCategoriaGramatical());
            return palabraRepository.save(existingPalabra);
        }).orElseThrow(() -> new RecordNotFoundException("No existe Palabra para el id: ", palabra.getId()));
    }

    public void deletePalabra(Long id) throws RecordNotFoundException {
        if (!palabraRepository.existsById(id)) {
            throw new RecordNotFoundException("No existe Palabra para el id: ", id);
        }
        palabraRepository.deleteById(id);
    }
}