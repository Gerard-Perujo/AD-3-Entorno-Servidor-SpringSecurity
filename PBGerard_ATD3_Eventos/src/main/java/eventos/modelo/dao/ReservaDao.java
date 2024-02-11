package eventos.modelo.dao;

import java.util.List;

import eventos.modelo.entity.Evento;
import eventos.modelo.entity.Reserva;
import eventos.modelo.entity.Usuario;


/**
 *  Interface de Reserva donde tendremos nuestros metodos que utilizaremos para
 * sacar la informacion
 * 
 * @author Gerard Perujo
 *
 */
public interface ReservaDao {
	
	Reserva buscarReserva(int idReserva);
	int eliminarReserva(Reserva reserva);
	Reserva eventoConUsuario(Evento evento, Usuario usuario);
	List<Reserva> buscarReservaPorUsusario(Usuario usuario);
	Reserva modificarReserva(Reserva reserva);
	Reserva agregarReserva(Reserva reserva);
	List<Reserva> listaEventosPorUsuario(Evento evento, Usuario usuario);

}
