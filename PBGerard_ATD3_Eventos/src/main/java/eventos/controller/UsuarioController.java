package eventos.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import eventos.modelo.dao.UsuarioDao;
import eventos.modelo.entity.Evento;
import eventos.modelo.entity.Reserva;
import eventos.modelo.entity.Tipo;
import eventos.modelo.entity.Usuario;
import jakarta.servlet.http.HttpSession;


/**
 *  Controlador que usaremos para controlar el accesso a las reservas que tiene el usuario
 *  
 * @author Gerard Perujo
 *
 */
@Controller
public class UsuarioController {
	
	
	/**
	 * Importamos la clase UsuarioDao para poder usar los metodos de la clase
	 */
	@Autowired
	private UsuarioDao usudao;
	
	
	/**
	 * Importarmos la clase ReservaDao para poder utilizar los metodos de la clase
	 */
	@Autowired
	private ReservaDao resdao;
	
	
	/**
	 * Importamos la clase EventoDao para poder usar los metodos de la clase
	 */
	@Autowired
	private EventoDao evendao;
	

	
	/**
	 * Metodo que utilizamos para sacar una vista de la pagina miUsuario donde saldra toda la informacion
	 * del usuario en question
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @return nos devuelve una vista donde muestra toda la informacion del usuario que se ha logeado
	 */
	@GetMapping("/miUsuario")
	public String mostrarUsuario(Model model, HttpSession session) {
		
		Usuario user = (Usuario) session.getAttribute("usuario");
		
		Usuario usuario = usudao.buscarUsuario(user.getUsername());
		String pass = usuario.getPassword();
		int sinNoop = 6;
		String password = pass.substring(sinNoop);// le quito el noop de la pasword para que se muestre sin noop ya que desde la BBDD va a llegar con el noop
		usuario.setPassword(password);
		model.addAttribute("user", usuario);
		
			     
			return "mostrarUsuario";
			        
	}
	
	
	/**
	 * Metodo que usaremos para mostrar una vista donde se mostraran todas las reservas que tiene
	 * el Usuario
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param  session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @return devuelve una lista de todas las reservas que tiene el usuario
	 * 
	 */
	@GetMapping("/misReservas")
	public String mostrarMisReservas(Model model, HttpSession session) {
		
		Usuario user = (Usuario) session.getAttribute("usuario");

	
		
			List<Reserva> reserva = resdao.buscarReservaPorUsusario(user);
			model.addAttribute("reservas", reserva);
	
		
		
		return "misReservas";
	}
	
	/**
	 * Metodo que utilizaremos para realizar una reserva de algun Evento
	 * 
	 * @param idEvento el id del evento del qual realizaremos la reserva
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @param ratt parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param numEntradas cantidad de entradas que vamos a reservar
	 * 
	 * @return
	 */
	@PostMapping("/reservas/{id}")
	public String mostrarCantidadReservas( @PathVariable("id")int idEvento, HttpSession session, RedirectAttributes ratt, @RequestParam(name = "entradas", required = false)int numEntradas) {
		
		Reserva nuevaReserva = new Reserva();
				Reserva reserva = new Reserva();
		int maxCantidad = 10;// cantidad maxima de entradas que podemos reservar por evento
		
		Usuario user = (Usuario) session.getAttribute("usuario");
		
		Evento even = evendao.buscarUno(idEvento);
		
		
			 	/**
			 	 * En caso de que el usuario aun no haya echo ninguna reserva en ese evento
			 	 */
			     if(resdao.eventoConUsuario(even, user) == null) {
			    	int maxAforo = even.getAforoMaximo();
			    	int asistencia = even.getMinimoAsistencia();
			    	int aforoDisponible = maxAforo - asistencia;// calculo el aforo disponible para comprobar si queda espacio para comprar entradas
					if(aforoDisponible >= numEntradas){// en caso de que el aforodisponible sea superior o igual a las entradas las podra comprar
						int nuevaAsistencia = asistencia + numEntradas;
						even.setMinimoAsistencia(nuevaAsistencia);
						BigDecimal precioEntrada = even.getPrecio();
						int precioIntEntrada = precioEntrada.intValue();
						int precio = precioIntEntrada*numEntradas;// calculo el precio de la compra multiplicando el precio unitario por la cantidad de entradas
						nuevaReserva.setEvento(even);
						nuevaReserva.setUsuario(user);
						nuevaReserva.setPrecioVenta(even.getPrecio());
						nuevaReserva.setCantidad(numEntradas);// inserto la cantidad de entradas que ha comprado 
						if(resdao.agregarReserva(nuevaReserva) != null){
							ratt.addFlashAttribute("mensaje", "Reseva Realizada Correctament");
							ratt.addFlashAttribute("precio", "El precio de tu compra es: " + precio);
							 return"redirect:/eventos/verUno/{id}";
						 }
					}
					
				 /**
				  * en caso de que el usuario ya tenga echa una reserva previa de ese evento
				  */
			 }else {
				 reserva = resdao.eventoConUsuario(even, user);
				 
				 if(resdao.buscarReserva(reserva.getIdReserva()) != null) {
					 	int maxAforo = even.getAforoMaximo();
				    	int asistencia = even.getMinimoAsistencia();
				    	int aforoDisponible = maxAforo - asistencia;// calculo el aforo disponible para comprobar si queda espacio para comprar entradas
						if(aforoDisponible >= numEntradas) {// en caso de que el aforodisponible sea superior o igual a las entradas las podra comprar
							int nuevaAsistencia = asistencia + numEntradas;
							even.setMinimoAsistencia(nuevaAsistencia);
							reserva.setEvento(even);
							int miCantidad = reserva.getCantidad();
							int totalEntradas = miCantidad + numEntradas;// sumo la cantidad de entradas que tengo ya compradas mas las que quiero comprar
								if(totalEntradas <= maxCantidad) {// en caso de que la nueva cantidad de entradas no sea superior a la maxima permitida me dejara comprarlas
									reserva.setCantidad(totalEntradas);
									BigDecimal precioEntrada = even.getPrecio();
									int precioIntEntrada = precioEntrada.intValue();
									int precio = precioIntEntrada*numEntradas;// calculo el precio de la compra multiplicando el precio unitario por la cantidad de entradas
									resdao.modificarReserva(reserva);
									ratt.addFlashAttribute("mensaje", "Reseva Realizada Correctament");
									ratt.addFlashAttribute("precio", "El precio de tu compra es: " + precio);
									return"redirect:/eventos/verUno/{id}";
								
									}		
						}
								
				}
			 }
			 
			
		ratt.addFlashAttribute("mensajeError", "Cantidad de entradas superior a la permitida");
		
		
		return"redirect:/eventos/verUno/{id}";
	}
	
	
	/**
	 * Metodo que utilizamos para Cancelar una Reserva cogiendo el parametro idReserva que hace referencia
	 * a la reserva que queremos cancelar
	 * 
	 * @param idReserva es el id de la Rerserva que queremos cancelar
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @return en caso de que la cancelacion sea satisfactoria nos devuelve un mensaje de que la reserva
	 * se ha cancelado
	 */
	@GetMapping("/eventos/cancelar/{id}")
	public String cancelarTuEvento (@PathVariable("id") int idReserva, HttpSession session, Model model) {
		
		
		Reserva reserva = resdao.buscarReserva(idReserva);
	
			Evento even = reserva.getEvento();
			int disponibilidad = even.getMinimoAsistencia();//cogo el valor de minimo asistencia
			int misReservas = reserva.getCantidad();// cogo la cantidad de entradas que tengo reservadas
			int nuevaDisponibilidad = disponibilidad - misReservas;// resto la cantidad que tenia reservado a la disponibilidad para devolverle el aforo una vez cancelada mi reserva
			even.setMinimoAsistencia(nuevaDisponibilidad);
			reserva.setEvento(even);
			resdao.modificarReserva(reserva);//una vez modificado el aforo hago un save para aplicar los cambios
			resdao.eliminarReserva(reserva);
			model.addAttribute("mensaje", "Tu Reserva ha sido Cancelada");
		
		
		return "forward:/misReservas";
	}
	

}
