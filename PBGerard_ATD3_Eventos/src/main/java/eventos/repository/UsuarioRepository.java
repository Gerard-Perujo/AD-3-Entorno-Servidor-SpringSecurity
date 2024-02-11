package eventos.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import eventos.modelo.entity.Usuario;


/**
 * Creo la clase Usuario repository que coge todos los metodos que tiene JPA
 * de esta manera esta clase se encargara de hacer todas las operaciones
 * necesarias con sus metodos propios
 * 
 * @author Geard Perujo
 *
 */
public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
	/**
	 * Metodo que sirve para buscar un usuario con su pasword
	 * 
	 * @param username String es el nombre del usuario que buscamos
	 * 
	 * @param password String es la password del usuario que buscamos
	 * 
	 * @return devuelve el usuario que hemos buscado 
	 */
	@Query("select u from Usuario u where u.username = ?1 AND u.password = ?2")
	public Usuario buscarUsuarioPorPass(String username, String password);

}
