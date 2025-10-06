package com.proyecto.biblioteca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.biblioteca.dto.PrestamoDTO;
import com.proyecto.biblioteca.dto.PrestamoInfoDTO;
import com.proyecto.biblioteca.model.Libro;
import com.proyecto.biblioteca.model.Prestamo;
import com.proyecto.biblioteca.model.Usuario;
import com.proyecto.biblioteca.repository.ILibroRepository;
import com.proyecto.biblioteca.repository.IPrestamoRepository;
import com.proyecto.biblioteca.repository.IUsuarioRepository;

@Service
public class PrestamoService implements IPrestamoService {

    @Autowired 
    private IPrestamoRepository prestRepo;
    @Autowired
    private ILibroRepository libRepo;
    @Autowired
    private IUsuarioRepository usuaRepo;

    

    //Creación de Prestamos
    @Override
    public void savePrestamos(PrestamoDTO prestDTO) {
        // Traemos los datos de Prestamo
        Prestamo Prestamo = new Prestamo();
        Prestamo.setFechaInicio(prestDTO.getFechaInicio());    
        Prestamo.setFechaEntrega(prestDTO.getFechaEntrega());    

        // Traemos el objeto producto
        List<Libro> libro = libRepo.findAllById(prestDTO.getLibrosId());
        Prestamo.setListaLibros(libro);

        // Traemos el objeto cliente
        Usuario usua = usuaRepo.findById(prestDTO.getUsuarioId()).orElse(null);
        Prestamo.setUnUsuario(usua);

        // Guardar la busqueda
        prestRepo.save(Prestamo);

        //Modifica el valor true de la variable disponibilidad
        for(Libro libroDispo : libro){
            libroDispo.setDisponibilidad(false);
            libRepo.save(libroDispo);
        }
    }


    //Traer todas las Prestamos
    @Override
    public List<PrestamoInfoDTO> getPrestamos(){

        List<Prestamo> listaPrestamos = prestRepo.findAll();
        List <PrestamoInfoDTO> listaDTO = new ArrayList<>();

        for(Prestamo prest: listaPrestamos){
            listaDTO.add(this.convertirDTO(prest));
        }
        return listaDTO;
    } 


    // Traer un solo Prestamo
    @Override
    public PrestamoInfoDTO findPrestamo(Long id) {

        Prestamo prest = prestRepo.findById(id).orElse(null);

        //Retorno para manejo de ResponseEntity
        if(prest == null){
            return null;
        }
        return this.convertirDTO(prest);
    }


    // Eliminación de una sola Prestamo
    @Override
    public boolean deletePrestamo(Long id) {
        Optional<Prestamo> optionalPrestamo = prestRepo.findById(id);
        // Confirmación si existe el Id
        if (optionalPrestamo.isPresent()) {
            Prestamo prestamo = optionalPrestamo.get();
            // Trae los libros asociados al préstamo
            List<Libro> librosAsociados = prestamo.getListaLibros();
            // Marcar los libros como disponibles nuevamente
            for (Libro libro : librosAsociados) {
                libro.setDisponibilidad(true);
            }
            // Guarda todos los libros actualizados
            libRepo.saveAll(librosAsociados);
            // Elimina el préstamo
            prestRepo.delete(prestamo);
            return true;
        } 
        else {
            return false;
        }
    }

    // Edición de una sola Prestamo
    @Override
    public PrestamoInfoDTO editPrestamo(Long id, PrestamoInfoDTO prestInfoDTO) {
        //Trae el objeto de la Prestamo por Id 
        Prestamo prest = prestRepo.findById(id).orElse(null);
        //Retorno para manejo de ResponseEntity
        if(prest == null){
            return null;
        }
        // Editar atributos de Prestamo
        prest.setFechaInicio(prestInfoDTO.getFechaInicio());
        prest.setFechaEntrega(prestInfoDTO.getFechaEntrega());
        // Guarda el nuevo valor 
        Prestamo PrestamoActualizada = prestRepo.save(prest);   
        //Convierte a DTO el resultado
        return this.convertirDTO(PrestamoActualizada);
    }


    //------------------------------------------------------------------------

    // Trae los valores de prestamo y los Settea en DTO de info
    public PrestamoInfoDTO convertirDTO(Prestamo prest){
        PrestamoInfoDTO prestDTO = new PrestamoInfoDTO();

        // Atributos del prestamo a mostrar
        prestDTO.setCodigoPrestamo(prest.getCodigoPrestamo());
        prestDTO.setFechaInicio(prest.getFechaInicio());
        prestDTO.setFechaEntrega(prest.getFechaEntrega());

        // Atributos de libro a mostrar
        List<String> nomLib = new ArrayList<>();
        for(Libro listaLibros : prest.getListaLibros()){
            nomLib.add(listaLibros.getTitulo());
        }
        prestDTO.setTitulos(nomLib);

        // Atributos a mostrar de usuario
        prestDTO.setNomUsuario(prest.getUnUsuario().getNombre());

        // Se retorna el objeto completo
        return prestDTO;

    }


}
