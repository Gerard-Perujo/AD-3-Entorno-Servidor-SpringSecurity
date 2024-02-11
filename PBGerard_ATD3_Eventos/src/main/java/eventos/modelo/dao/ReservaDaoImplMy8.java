package eventos.modelo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eventos.modelo.entity.Evento;
import eventos.modelo.entity.Reserva;
import eventos.modelo.entity.Usuario;
import eventos.repository.ReservaRepository;

/**
 * Clase que implementa la interface ReservaDao para poder utilizar sus metodos
 * 
 * @author Gerard Perujo
 *
 */
@Repository
public class ReservaDaoImplMy8 implements ReservaDao{
	
	
	/**
	 * Imporamos la clase ReservaRepository para poder utilizar los metodos de JPA
	 */
	@Autowired
	private ReservaRepository reserepo;
	


	/**
	 * Metodo que usaremos para sacar una lista de todas las reservas que tiene el usuario
	 */
	@Override
	public List<Reserva> buscarReservaPorUsusario(Usuario usuario) {
		
		String username = usuario.getUsername();// cogo el username del usuario que nos entra
		
		
		return reserepo.reservaPorUsuario(username);//utilizao el metodo que he creado en el reposotory para buscar todas las reservas que tiene ese usuario
	}



	/**
	 * Metodo que nos devolvera una reserva segun su evento y usuario
	 */
	@Override
	public Reserva eventoConUsuario(Evento evento, Usuario usuario) {
		
		String username = usuario.getUsername();// cogo el username del usuario que nos entra
		int idEvento = evento.getIdEvento();// cogo el idEvento del evento que nos entra
		return reserepo.reservaPorEvento(username, idEvento);//utilizao el metodo que he creado en el reposotory para buscar la reserva de ese usuario que tiene con ese evento
	}



	/**
	 * Metodo que utilizaremos para poder modificar una reserva que ja haya sido creada, en caso de que
	 * no exista la reserva devolvera nulo y si existe modificara la reserva y nos devolvera la nueva
	 * reserva modificada
	 */
	@Override
	public Reserva modificarReserva(Reserva reserva) {
		/**
		 * al utilizar el metodo sace de JPA levanta Excepciones usaremos el trycatch
		 */
		try {
			if(buscarReserva(reserva.getIdReserva())!= null) {//busco la reserva por su idReserva para comprobar que existe
				return reserepo.save(reserva);// en caso de que la reserva exista hago el save para modificarla
			}else {
				return null;
			}
	
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}



	/**
	 * Metodo que utilizaremos para agregar una reserva que no exista, en caso de que esa reserva exista 
	 * nos devolvera nulo y si no existe nos devolvera la reserva que se ha agregado
	 */
	@Override
	public Reserva agregarReserva(Reserva reserva) {
		/**
		 * al utilizar el metodo sace de JPA levanta Excepciones usaremos el trycatch
		 */
		
			try {
				if(buscarReserva(reserva.getIdReserva())== null) {//busco la reserva por su idReserva para comprobar que no exista
					return reserepo.save(reserva);// en caso de que la reserva no exista hago el save para agregarla
				}else {
					return null;
				}
		
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

	}


	/**
	 * Metodo que utilizaremos para eliminar una reserva que se quiera cancelar, en caso de eliminarla
	 * nos va a devolver un 1 en caso de que no se elimine devolera un 
	 * 
	 */
	@Override
	public int eliminarReserva(Reserva reserva) {
		if(buscarReserva(reserva.getIdReserva()) != null) {// compruebo si la reserva existe 
			reserepo.delete(reserva);// en caso de que la reserva exista hago el delete para eliminarla
			return 1;
		}else {
			return 0;
		}
		
	}


	/**
	 * Metodo que utilizaremos para buscar una reserva segun su Id, en caso de que no exita la reserva
	 * nos va a devolver nulo, si existe nos devolvera el objeto reserva
	 */
	@Override
	public Reserva buscarReserva(int idReserva) {
		
		return reserepo.findById(idReserva).orElse(null);
	}


	/**
	 * Metodo que utilizaremos para sacar una lista de todas las reservas que tiene segun el evento 
	 * y el usuario
	 */
	@Override
	public List<Reserva> listaEventosPorUsuario(Evento evento, Usuario usuario) {
		String username = usuario.getUsername();// cogo el username del usuario que nos entra
		int idEvento = evento.getIdEvento();// cogo el idEvento del evento que nos entra
		return reserepo.listaReservaPorEvento(username, idEvento);//utilizao el metodo que he creado en el reposotory para buscar todas las reservas de ese usuario que tiene con ese evento
	}
}
