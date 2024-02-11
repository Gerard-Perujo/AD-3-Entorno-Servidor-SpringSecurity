package eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eventos.modelo.entity.Perfil;

/**
 * Creo la clase Perfil repository que coge todos los metodos que tiene JPA
 * de esta manera esta clase se encargara de hacer todas las operaciones
 * necesarias con sus metodos propios
 * 
 * @author Geard Perujo
 *
 */

public interface PerfilRepository extends JpaRepository<Perfil, Integer>{

}
