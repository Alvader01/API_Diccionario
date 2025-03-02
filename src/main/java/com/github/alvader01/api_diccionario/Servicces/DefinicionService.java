package com.github.alvader01.api_diccionario.Servicces;

import com.github.alvader01.api_diccionario.Entities.Definicion;
import com.github.alvader01.api_diccionario.Entities.Palabra;
import com.github.alvader01.api_diccionario.Repositories.DefinicionRepository;
import com.github.alvader01.api_diccionario.Repositories.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefinicionService {
    @Autowired
    private DefinicionRepository definicionRepository;

    @Autowired
    private PalabraRepository palabraRepository;

    public List<Definicion> getDefinicionesByPalabraId(Long palabraId) {
        return definicionRepository.findAll().stream()
                .filter(def -> def.getPalabra().getId().equals(palabraId))
                .toList();
    }

    public Definicion addDefinicion(Long palabraId, Definicion definicion) throws RecordNotFoundException {
        Palabra palabra = palabraRepository.findById(palabraId)
                .orElseThrow(() -> new RecordNotFoundException("No existe Palabra para el id: ", palabraId));
        definicion.setPalabra(palabra);
        return definicionRepository.save(definicion);
    }

    public void deleteDefinicion(Long id) throws RecordNotFoundException {
        if (!definicionRepository.existsById(id)) {
            throw new RecordNotFoundException("No existe Definicion para el id: ", id);
        }
        definicionRepository.deleteById(id);
    }
}