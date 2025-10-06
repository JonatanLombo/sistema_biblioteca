package com.proyecto.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.biblioteca.model.Libro;

@Repository
public interface ILibroRepository extends JpaRepository<Libro, Long> {

}
