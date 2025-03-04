package com.github.alvader01.api_diccionario.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "palabra", schema = "diccionariodb")
public class Palabra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "termino", nullable = false)
    private String termino;

    @Size(max = 50)
    @NotNull
    @Column(name = "categoria_gramatical", nullable = false, length = 50)
    private String categoriaGramatical;

    @OneToMany(mappedBy = "palabra",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference  // Evita la recursión en el lado de Palabra
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Definicion> definicions = new LinkedHashSet<>();

    public Palabra(Integer id, String termino, String categoriaGramatical) {
        this.id = id;
        this.termino = termino;
        this.categoriaGramatical = categoriaGramatical;
    }

    public Palabra() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public String getCategoriaGramatical() {
        return categoriaGramatical;
    }

    public void setCategoriaGramatical(String categoriaGramatical) {
        this.categoriaGramatical = categoriaGramatical;
    }

    public Set<Definicion> getDefinicions() {
        return definicions;
    }

    public void setDefinicions(Set<Definicion> definicions) {
        this.definicions = definicions;
    }
}