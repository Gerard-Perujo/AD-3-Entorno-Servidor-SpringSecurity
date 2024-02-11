package eventos.modelo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import eventos.modelo.entity.Perfil;
import eventos.repository.PerfilRepository;


/**
 *Clase que implementa los metodos del interface PerfilDao
 *
 * @author Gerard Perujo
 *
 */
@Controller
public class PerfilDaoImplMy8 implements PerfilDao{
	
	/**
	 * Importamos la clase PerfilRepository para poder utilizar los metodo de JPA
	 */
	@Autowired
	private PerfilRepository perfirepo;

	
	/**
	 * Metodo para buscar un perfil pasandole su id, en caso de encontrarlo
	 * nos devolvera un objeto Perfil sino sera nulo
	 */
	@Override
	public Perfil buscarUno(int idPerfil) {
		
		return perfirepo.findById(idPerfil).orElse(null);
	}

}
