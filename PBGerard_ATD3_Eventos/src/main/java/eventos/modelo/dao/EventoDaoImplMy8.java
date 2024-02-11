package eventos.modelo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eventos.modelo.entity.Evento;
import eventos.modelo.entity.Tipo;
import eventos.repository.EventoRepository;

/**
 * Clase que implementa la interface EventoDao para poder utilizar sus metodos
 * 
 * @author Gerard Perujo
 *
 */
@Repository
public class EventoDaoImplMy8 implements EventoDao{
	
	
	/**
	 * Importamos la clase EventoRepository para poder utilizar los metodos de JPA
	 */
	@Autowired
	private EventoRepository evenrepo;

	/**
	 * Metodo que utilizamos para mostrar una lista de todos los eventos que hay
	 */
	@Override
	public List<Evento> sacarTodo() {
		
		return evenrepo.findAll();
	}

	
	/**
	 * Metodo que utilizamos para mostrar una lista de todos los eventos segun el estado
	 * que nos pasan
	 */
	@Override
	public List<Evento> buscarPorActivo(String estado){
		return evenrepo.findPorActivo(estado);
	}

	/**
	 * Metodo que utilizamos para buscar un Evento segun la id que nos pasan, en caso de encontrarlo
	 * nos devolvera un objeto Evento sino nos devolvera nulo
	 */
	@Override
	public Evento buscarUno(int idEvento) {
	
		return evenrepo.findById(idEvento).orElse(null);
	}

	/**
	 * Metodo que nos muestra una lista de todos los eventos segun el parametro destacado
	 */
	@Override
	public List<Evento> buscarPorDestacado(String destacado) {
		return evenrepo.findPorDestacado(destacado);
	}

	
	/**
	 * Metodo que nos muestra una lista de todos los eventos, buscandolos segun su tipo
	 * y por destacado.
	 */
	@Override
	public List<Evento> buscarPorEventoDestacado(Tipo tipo, String destacado){
		
			String tipos = tipo.getNombre();//cogemos el nombre del tipo de evento que nos entra
			return evenrepo.findPorTipoDestacado(tipos,destacado);// utilizamos el metodo que hemos creado en el repository, para buscar los eventos segun su tipo y destacados
		
	}

	/**
	 * Metodo que nos muestra una lista de todos los eventos , segun su tipo y estado
	 */
	@Override
	public List<Evento> BuscarPorEventoEstado(Tipo tipo, String estado) {
		
		String tipos = tipo.getNombre();//cogemos el nombre del tipo de evento que nos entra
		return evenrepo.findPorTipoEstado(tipos, estado);// utilizamos el metodo que hemos creado en el repository, para buscar los eventos segun su tipo y estado
	}

	/**
	 * Metodo que nos muesta una lista de todos los eventos segun su tipo
	 */
	@Override
	public List<Evento> BuscarPorEventosTipo(Tipo tipo) {
		String tipos = tipo.getNombre();//cogemos el nombre del tipo de evento que nos entra
		return evenrepo.findPorTipo(tipos);// utilizamos el metodo que hemos creado en el repository, para buscar los eventos segun su tipo
	}

	
}
