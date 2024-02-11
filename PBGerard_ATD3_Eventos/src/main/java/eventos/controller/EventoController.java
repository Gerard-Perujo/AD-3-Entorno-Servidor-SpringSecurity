package eventos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eventos.modelo.dao.EventoDao;
import eventos.modelo.dao.ReservaDao;
import eventos.modelo.entity.Evento;
import eventos.modelo.entity.Reserva;
import eventos.modelo.entity.Tipo;
import eventos.modelo.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

/**
 * Controlador que usaremos para controlar los accesos a todas las rutas referentes a los Eventos 
 * 
 * @author Gerard Perujo
 *
 */
@Controller
public class EventoController {
	
	/**
	 * Importamos la clase EventoDao para poder acceder a todos los metodos que tiene
	 */
	@Autowired
	private EventoDao evendao;
	
	
	/**
	 * Importamos la clase ReservaDao para poder acceder a todos los metodos que tiene
	 */
	@Autowired
	private ReservaDao resedao;
	
	
	
	/**
	 * Metodo que usaremos para acceder a la vista donde se mostraran todos los eventos activos que hay
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @param tipoEvento parametro que utilizamos para filtrar eventos segun su tipo
	 * 
	 * @return nos saca la pagina de eventos activos donde veremos una lista de todos los eventos que estan activos
	 */
	@GetMapping("/eventos/activos")
	public String mostrarEventosActivos( Model model, HttpSession session, @RequestParam(name = "tipo", required = false)String tipoEvento) {
		
		
		/**
		 * Creo una clase tipo para poder insertar el parametro tipo nombre 
		 * en caso de que quisiera buscar los eventos segun su tipo.
		 */
		Tipo tipos = new Tipo();
		if(tipoEvento == null) {
			tipoEvento = " ";
			tipos.setNombre(tipoEvento);
		}else {
			tipos.setNombre(tipoEvento);
		}
		
		/**
		 * hago un if para filtrar en caso de que el tipo sea blanco nos sacara toda una lista de los
		 * eventos que sean activos y en caso de que el tipo tenga un nombre nos sacara una lista de 
		 * todos los eventos activos segun el tipo le hayamos marcado.
		 */
		if(tipos.getNombre() == " ") {
			List<Evento> even = evendao.buscarPorActivo("ACTIVO");
			model.addAttribute("activos", even);
		}else {
			List<Evento> even = evendao.BuscarPorEventoEstado(tipos,"ACTIVO");
			model.addAttribute("activos", even);
		}
	
		    return "listaEventosActivos";
	
	}
	
	/**
	 * Metodo que nos sacara una vista de todos los eventos que sean destacados
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @param tipoEvento parametro que utilizamos para filtrar eventos segun su tipo
	 * 
	 * @return nos saca la pagina de eventos destacados donde veremos una lista de todos los eventos 
	 * destacados
	 */
	@GetMapping("/eventos/destacados")
	public String mostrarEventosDestacados(Authentication aut, Model model, HttpSession session, @RequestParam(name = "tipo", required = false)String tipoEvento){
		
	
		/**
		 * Creo una clase tipo para poder insertar el parametro tipo nombre 
		 * en caso de que quisiera buscar los eventos segun su tipo.
		 */
		Tipo tipos = new Tipo();
		if(tipoEvento == null) {
			tipoEvento = " ";
			tipos.setNombre(tipoEvento);
		}else {
			tipos.setNombre(tipoEvento);
		}
		
		
		/**
		 * hago un if para filtrar en caso de que el tipo sea blanco nos sacara toda una lista de los
		 * eventos que sean destacados y en caso de que el tipo tenga un nombre nos sacara una lista de 
		 * todos los eventos destacados segun el tipo le hayamos marcado.
		 */
		if(tipos.getNombre() == " ") {
			List<Evento> even = evendao.buscarPorDestacado("S");
			model.addAttribute("destacados", even);
		}else {
			List<Evento> even = evendao.buscarPorEventoDestacado(tipos,"S");
			model.addAttribute("destacados", even);
		}
		
	
				return "listaEventosDestacados";

	}
	
	
	
	/**
	 * Metodo que utilizamos para sacar una vista donde podremos ver la informacion de un evento en
	 * concreto
	 * 
	 * @param idEvento el id del Evento que queremos ver la informacion
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @return nos saca la pagina donde veremos toda la informacion del evento que deseamos
	 */
	@GetMapping("/eventos/verUno/{id}")
	public String verUnEvento(@PathVariable("id") int idEvento, Model model, HttpSession session) {
		
		/**
		 * Cantidad maxima de entradas que se puede comprar por evento
		 */
		int maxCantidad = 10;
		
		
		Usuario user = (Usuario) session.getAttribute("usuario");
		
		Evento even = evendao.buscarUno(idEvento);
		model.addAttribute("evento", even);
		
	
		if(user != null) {
			
			/**
			 * utilizo el metodo eventoConUsuario para comprovar cuantas entradas
			 * tiene reservado ese usuario con el evento en question
			 */
			Reserva reserva = resedao.eventoConUsuario(even, user);
			/**
			 * utilizo un if para poder mostrar la cantidad de entradas que puede reservar, si es nulo
			 * se mostra la cantidad maxima que puede reservar, en caso de que no
			 * sea nulo se mostra la cantidad de entradas que le quedan disponibles para reservar
			 */
			if(reserva != null) {
				int restante = maxCantidad - reserva.getCantidad();
				reserva.setCantidad(restante);
				model.addAttribute("reserva", reserva);
			}else {
				Reserva res = new Reserva();
				res.setCantidad(maxCantidad);
				model.addAttribute("reserva", res);
			}
		}
			
					
		return "verEvento";
	}
	
	
	
	/**
	 * Metodo que utilizamos para mostrar la pagina donde veremos una lista de todos los eventos que hay
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @param tipoEvento parametro que utilizamos para filtrar eventos segun su tipo
	 * 
	 * @return nos saca la pagina listaEventos donde veremos una lista de todos los eventos que podemos
	 * encontrar
	 */
	@GetMapping("/listaEventos")
	public String mostrarTodosEventos(Model model, HttpSession session, @RequestParam(name = "tipo", required = false)String tipoEvento) {
		
		Usuario user = (Usuario) session.getAttribute("usuario");
		
		
		/**
		 * Creo una clase tipo para poder insertar el parametro tipo nombre 
		 * en caso de que quisiera buscar los eventos segun su tipo.
		 */
		Tipo tipos = new Tipo();
		if(tipoEvento == null) {
			tipoEvento = " ";
			tipos.setNombre(tipoEvento);
		}else {
			tipos.setNombre(tipoEvento);
		}
		
		

		/**
		 * hago un if para filtrar en caso de que el tipo sea blanco nos sacara toda una lista de todos
		 * los eventos y en caso de que el tipo tenga un nombre nos sacara una lista de 
		 * todos los eventos segun el tipo le hayamos marcado.
		 */
		if(tipos.getNombre() == " ") {
			List<Evento> even = evendao.sacarTodo();
			model.addAttribute("eventos", even);
		}else {
			List<Evento> even = evendao.BuscarPorEventosTipo(tipos);
			model.addAttribute("eventos", even);
		}
		
		return "mostrarTodosEventos";
	}
	
	

}


