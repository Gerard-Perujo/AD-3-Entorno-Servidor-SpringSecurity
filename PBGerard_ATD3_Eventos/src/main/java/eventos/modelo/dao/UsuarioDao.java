package eventos.modelo.dao;

import eventos.modelo.entity.Usuario;

/**
 *  Interface de Usuario donde tendremos nuestros metodos que utilizaremos para
 * sacar la informacion
 * 
 * @author Gerard Perujo
 *
 */
public interface UsuarioDao {
	
	Usuario buscarUsuario(String username);
	Usuario agregarUsuario(Usuario usuario);
	Usuario buscarUsuarioConPass(String username, String password);

}
