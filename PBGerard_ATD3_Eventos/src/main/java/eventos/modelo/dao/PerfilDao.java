package eventos.modelo.dao;

import eventos.modelo.entity.Perfil;

/**
 * Interface de Perfil donde tendremos nuestros metodos que utilizaremos para
 * sacar la informacion
 * 
 * @author Gerard Perujo
 *
 */
public interface PerfilDao {
	
	Perfil buscarUno(int idPerfil);

}
