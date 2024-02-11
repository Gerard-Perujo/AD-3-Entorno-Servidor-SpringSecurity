package eventos.configuration;



import javax.sql.DataSource;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase para controlar todos los filtros y permisos del security
 * 
 * @author Gerard Perujo
 *
 */
@EnableWebSecurity
@Configuration
public class DataUserConfiguration{
	

	@Bean

	public UserDetailsManager usersCustom(DataSource dataSource) {

	JdbcUserDetailsManager users = 
			new JdbcUserDetailsManager(dataSource); 
	users.setUsersByUsernameQuery("select username,password,enabled from Usuarios u where username=?"); 
	users.setAuthoritiesByUsernameQuery("select u.username,p.nombre from Usuario_Perfiles up " +
	 "inner join usuarios u on u.username = up.username " +
			"inner join perfiles p on p.id_perfil = up.id_perfil " +
			"where u.username = ?");

	return users;

	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
		.csrf(csrf -> csrf.disable());
		// Los recursos estáticos no requieren autenticación
		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("static/**").permitAll()
			// Las vistas públicas no requieren autenticación
			.requestMatchers("/signup","/","/login", "/logout","/eventos/verUno/**","/inicio").permitAll()
			.requestMatchers("/eventos/activos/**", "/eventos/destacados/**").permitAll()
			.requestMatchers("/rest/encriptar/**", "/listaEventosDestacadosFiltro/**","/filtarTipo").permitAll()
			// Todas las demás URLs de la Aplicación requieren autenticación
			// Asignar permisos a URLs por ROLES
			.requestMatchers("/listaEventos/**").hasAnyAuthority("ROLE_CLIENTE","ROLE_GESTOR","ROLE_ADMINISTRADOR") 
			.requestMatchers("/misReservas/**").hasAnyAuthority("ROLE_CLIENTE","ROLE_GESTOR","ROLE_ADMINISTRADOR") 
			.requestMatchers("/miUsuario/**").hasAnyAuthority("ROLE_CLIENTE","ROLE_GESTOR","ROLE_ADMINISTRADOR")
			.requestMatchers("/reservas/**").hasAnyAuthority("ROLE_CLIENTE","ROLE_GESTOR","ROLE_ADMINISTRADOR")
			.requestMatchers("/eventos/cancelar/**").hasAnyAuthority("ROLE_CLIENTE","ROLE_GESTOR","ROLE_ADMINISTRADOR")
			
			.anyRequest().authenticated());
		// El formulario de Login no requiere autenticacion
		http.formLogin(form -> form.loginPage("/login")//esta es la ruta que nos mostrara nuestro formulario
								.defaultSuccessUrl("/inicio").permitAll())// esta es la ruta que procesara el login de nuestro formulario
		.logout(logout -> logout.logoutSuccessUrl("/").permitAll());//esta es la ruta que mostrara una vez cerremos la session
		return http.build();
	}
	
	
	/*
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	*/
	

}