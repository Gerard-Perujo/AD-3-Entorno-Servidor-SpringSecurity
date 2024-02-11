package eventos.modelo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import eventos.modelo.entity.Usuario;
import eventos.repository.UsuarioRepository;

/**
 * Clase que implementa la interface UsuarioDao para poder utilizar sus metodos
 * 
 * @author Gerard Perujo
 *
 */
@Repository
public class UsuarioDaoImplMy8 implements UsuarioDao{
	
	/**
	 * Importamos la clase UsuarioRepositoy para poder utilizar los metodos de JPA
	 */
	@Autowired
	private UsuarioRepository usurepo;

	
	/**
	 * Metodo para buscar un usuario pasandole su username, en caso de encontrarlo
	 * nos devolvera un objeto Usuario sino sera nulo
	 */
	@Override
	public Usuario buscarUsuario(String username) {
		return usurepo.findById(username).orElse(null);
	
	}

	/**
	 * Metodo que utilizaremos para agregar un nuevo Usuario, en caso de que le usuario
	 * existiera nos devolvera nulo
	 */
	@Override
	public Usuario agregarUsuario(Usuario usuario) {
		/**
		 * como los metodos save de JPA levantan exepciones hacemos un try catch
		 */
		try {
			if(buscarUsuario(usuario.getUsername()) == null) {// en caso de que el usuario no existe lo agrega
				return usurepo.save(usuario);
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Metodos que utilizaremos para buscar un usuario con su password, en caso de que uno
	 * de los 2 campos no coincida nos devolvera nulo sino nos devolvera el Usuario en 
	 * question
	 */
	@Override
	public Usuario buscarUsuarioConPass(String username, String password) {
		
		return usurepo.buscarUsuarioPorPass(username, password);
	}

}
