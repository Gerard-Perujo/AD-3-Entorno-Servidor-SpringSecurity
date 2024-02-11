package eventos.modelo.dao;

import java.util.List;

import eventos.modelo.entity.Evento;
import eventos.modelo.entity.Tipo;


/**
 *  Interface de Evento donde tendremos nuestros metodos que utilizaremos para
 * sacar la informacion
 * 
 * @author Gerard Perujo
 *
 */
public interface EventoDao {
	
	
	Evento buscarUno(int idEvento);
	List<Evento> sacarTodo();
	List<Evento> buscarPorActivo(String estado);
	List<Evento> buscarPorDestacado(String destacado);
	List<Evento> buscarPorEventoDestacado(Tipo tipo, String destacado);
	List<Evento> BuscarPorEventoEstado(Tipo tipo, String estado);
	List<Evento> BuscarPorEventosTipo(Tipo tipo);

}
