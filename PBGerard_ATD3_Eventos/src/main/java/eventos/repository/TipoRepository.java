package eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eventos.modelo.entity.Reserva;
import eventos.modelo.entity.Tipo;

/**
 * Creo la clase Tipo repository que coge todos los metodos que tiene JPA
 * de esta manera esta clase se encargara de hacer todas las operaciones
 * necesarias con sus metodos propios
 * 
 * @author Geard Perujo
 *
 */
public interface TipoRepository extends JpaRepository<Tipo, Integer>{
	


}
