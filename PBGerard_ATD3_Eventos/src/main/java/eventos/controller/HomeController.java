package eventos.controller;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eventos.modelo.dao.PerfilDao;
import eventos.modelo.dao.UsuarioDao;
import eventos.modelo.entity.Perfil;
import eventos.modelo.entity.Usuario;
import jakarta.servlet.http.HttpSession;


/**
 * Controlador que usaremos para controlar el accesso a la pagina home, el login, logout, y el signin
 * 
 * @author Gerard Perujo
 *
 */
@Controller
public class HomeController {
	
	/**
	 * Importamos la clase UsuarioDao para poder acceder a todos los metodos de ella
	 */
	@Autowired
	private UsuarioDao usudao;
	
	
	/**
	 * Importamos la clase PerfilDao para poder acceder a todos los metodos de ella
	 */
	@Autowired
	private PerfilDao perfidao;
	
	
	
	/**
	 * Metodo que utilizamos para poder acceder a la vista Home y sacar la pagina principal
	 * 
	 * @return hace un GetMapping para sacarnos la vista de la pagina principal
	 */
	@GetMapping("/")
	public String home(){
			return "Home";
		
	}
			
		
	
	
	/**
	 * Metodo que utilizamos para sacar el formulario de login para que los usuarios registrados
	 * puedan acceder a todas las opciones de la pagina
	 * 
	 * @return hace un GetMapping para sacarnos la vista de la pagina login
	 */
	@GetMapping("/login")
	public String mostrarlogin(){
		
		return "login";
	}
	
	
	
	/**
	 * Metodo que utilizamos para procesar el login y comprobar si el usuario introducido existe o ha
	 * introducido correctamente las credenciales
	 * 
	 * @param aut es el parametro con que accedemos a security para sacar el usuario
	 * 
	 * @param model parametro que utilizamos para agregar atributos o sacar mensajes en las vistas
	 * 
	 * @param session parametros que utilizaremos para agregar el usuario en la session y poder acceder
	 * a sus datos hasta que cerremos la session
	 * 
	 * @return hace un GetMapping para procesar el login y en caso de que sea correcto nos devuelve 
	 * a la pagina de eventos destacados
	 */
	@GetMapping("/inicio")
	public String procesarLogin(Authentication aut, Model model, HttpSession session) {
		
		
		Usuario usuario = usudao.buscarUsuario(aut.getName());
		
		
		session.setAttribute("usuario", usuario);
		
		
		return "forward:/eventos/destacados";
	}
	
	 /**
	  * Metodo que utilizamos para acceder a la pagina de registro donde los usuarios nuevos 
	  * se podran registrar
	  *     
	  * @return hace un GetMapping para mostrarnos la pagina de registro.
	  */
	@GetMapping("/signup")
	public String mostrarRegistro() {
		return "registro";
	}
	
	
	/**
	 * Metodo que usamos para procesar el registro y asi poder registrar el nuevo ususario
	 * 
	 * @param usuario es el usuario que nos entra desde el registro
	 * 
	 * @param ratt parametro que utilizamos para sacar un mensaje de error en caso de que el
	 * usuario ha registrar ya existiera
	 * 
	 * @return si el registro es satisfactorio nos devuelve a la pagina de login para poder
	 * ingresar con el usuario que acabamos de registrar
	 */
	@PostMapping("/signup")
	public String hacerRegistro(Usuario usuario, RedirectAttributes ratt) {
		Date fecha = new Date();
		int enabled = 1;
		Perfil perfil = new Perfil();
		perfil = perfidao.buscarUno(3);
		usuario.setFechaRegistro(fecha);
		usuario.setEnabled(enabled);
		String pass = usuario.getPassword();
		usuario.setPassword("{noop}"+pass);
		usuario.addPerfil(perfil);
		
		if(usudao.agregarUsuario(usuario) != null) {
			return "redirect:/login";
		}else {
			ratt.addFlashAttribute("mensaje", "Usuario Introducido ya existe");
			return "redirect:/signup";
		}
	}
	
	/**
	 * Metodo que utilizamos para poder cerrar la session una vez queramos salir
	 * 
	 * @param session parametro del cual accedemos a los datos del usuario que esta en session
	 * 
	 * @param model parametro que usaremos para sacar un mensaje en caso de que la session se cerro correctamente
	 * @return nos devuelve a la pagina login una vez la session se ha cerrado correctamente
	 */
	@GetMapping("/logout")
	public String cerrarSession(HttpSession session, Model model) {
		session.removeAttribute("usuario");
		session.invalidate();
		model.addAttribute("mensajelogout", "La Session se ha Cerrado Correctamente");
		return "forward:/login";
	}
	
	
}
