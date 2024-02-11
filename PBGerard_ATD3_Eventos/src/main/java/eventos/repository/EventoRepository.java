package eventos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import eventos.modelo.entity.Evento;

/**
 * Creo la clase Evento repository que coge todos los metodos que tiene JPA
 * de esta manera esta clase se encargara de hacer todas las operaciones
 * necesarias con sus metodos propios
 * 
 * @author Geard Perujo
 *
 */
public interface EventoRepository extends JpaRepository<Evento, Integer>{
	
	/**
	 * Me creo el metodo findPorActivo para sacar una lista de todos los
	 * eventos que tengan el estado deseando
	 * 
	 * @param estado string es el estado en que se encuentra los eventos
	 *  
	 * @return devuelve una lista de todos los eventos que esten en el estado que hemos buscado
	 */
	@Query("select e from Evento e where e.estado = ?1")
	public List<Evento> findPorActivo(String estado);
	
	
	/**
	 * Metodo que saca una lista de todos los eventos que esten Destacados
	 * 
	 * @param destacado parametro que nos entra para realizar la busqueda
	 * 
	 * @return nos devuelve una lista de todos los eventos que esten destacados
	 */
	@Query("select e from Evento e where e.destacado = ?1")
	public List<Evento> findPorDestacado(String destacado);
	
	/**
	 * Metodo que busca todos los eventos por su tipo y por destacado
	 * 
	 * @param nombre String el nombre del tipo de evento que buscamos
	 * 
	 * @param destacado String el valor del parametro en que se encuentra destacado
	 * 
	 * @return devuelve una lista de todos los eventos segun el nombre del tipo de evento y si estan
	 * destacados o no
	 */
	
	@Query("select t from Evento t where t.tipo.nombre = ?1 AND t.destacado = ?2")
	public List<Evento> findPorTipoDestacado(String nombre, String destacado);
	
	/**
	 * Metodo que nos busca todos los eventos segun el nombre del tipo de evento y por su 
	 * estado
	 * 
	 * @param nombre String el nombre del tipo de evento que buscamos
	 * 
	 * @param estado String el valor del parametro que se encuentra el estado
	 * 
	 * @return devuelve una lista con todos los eventos que sean del mismo tipo y estado
	 */
	@Query("select t from Evento t where t.tipo.nombre = ?1 AND t.estado = ?2")
	public List<Evento> findPorTipoEstado(String nombre, String estado);
	
	/**
	 * Metodo que nos saca una lista de todos los eventos segun su tipo
	 * 
	 * @param nombre String el nombre del tipo de evento que buscamos
	 * 
	 * @return devuelve una lista de todos los eventos que sean del mismo tipo
	 */
	@Query("select t from Evento t where t.tipo.nombre = ?1")
	public List<Evento> findPorTipo(String nombre);
	
	
	/**
	 * Metodo que busca un evento segun su tipo
	 * 
	 * @param nombre String el nombre del tipo de evento que buscamos
	 * 
	 * @return devuelve un Evento segun el tipo que hayamos buscado
	 */
	@Query("select t from Evento t where t.tipo.nombre = ?1")
	public  Evento buscarPorTipo(String nombre);

}
