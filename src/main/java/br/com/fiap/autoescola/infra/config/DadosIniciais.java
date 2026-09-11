package br.com.fiap.autoescola.infra.config;

import br.com.fiap.autoescola.domain.usuario.Perfil;
import br.com.fiap.autoescola.domain.usuario.Usuario;
import br.com.fiap.autoescola.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DadosIniciais implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByLogin("admin")) {
            var admin = new Usuario("admin", passwordEncoder.encode("admin123"), Perfil.ADMIN);
            usuarioRepository.save(admin);
            System.out.println(">>> Usuário administrador criado: login = admin | senha = admin123");
        }
    }
}