package eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import eventos.modelo.entity.Reserva;
import eventos.modelo.entity.Usuario;

/**
 * Creo la clase Reserva repository que coge todos los metodos que tiene JPA
 * de esta manera esta clase se encargara de hacer todas las operaciones
 * necesarias con sus metodos propios
 * 
 * @author Geard Perujo
 *
 */
public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	
	/**
	 * Metodo que nos saca toda una lista de reservas segun su usuario
	 * 
	 * @param username String es el nombre del usuario que buscamos
	 * 
	 * @return nos devuelve una lista de todas las reservas que tiene ese usuario
	 */
	@Query("select u from Reserva u where u.usuario.username = ?1")
	public List<Reserva> reservaPorUsuario(String username);
	
	/**
	 * Metodo que nos devuelve una reserva segun su usuario y el id del evento
	 * 
	 * @param username String es el nombre del usuario que buscamos
	 * 
	 * @param idEvento integer es el numero que hace referencia al evento que estamos buscando
	 * 
	 * @return devuelve una reserva segun el usuario y el Evento que estemos buscando
	 */
	@Query("select e from Reserva e where e.usuario.username = ?1 AND e.evento.idEvento = ?2")
	public Reserva reservaPorEvento(String username, int idEvento);
	
	/**
	 * Metodo que nos devuelve una lista de todas las Reservas segun el usuario y el id del Evento
	 * 
	 * @param username String es el nombre del usuario que buscamos
	 * @param idEvento integer es el numero que hace referencia al evento que estamos buscando
	 * 
	 * @return devuelve una lista de todas las reservas que tiene ese usuario con ese evento
	 */
	@Query("select e from Reserva e where e.usuario.username = ?1 AND e.evento.idEvento = ?2")
	public List<Reserva> listaReservaPorEvento(String username, int idEvento);
}
