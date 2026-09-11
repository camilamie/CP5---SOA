package br.com.fiap.autoescola.domain.usuario;

import br.com.fiap.autoescola.domain.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public DadosDetalhamentoUsuario cadastrar(DadosCadastroUsuario dados) {
        if (repository.existsByLogin(dados.login())) {
            throw new ValidacaoException("Já existe um usuário com esse login");
        }
        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        var perfil = dados.perfil() != null ? dados.perfil() : Perfil.USER;

        var usuario = new Usuario(dados.login(), senhaCriptografada, perfil);
        repository.save(usuario);

        return new DadosDetalhamentoUsuario(usuario);
    }

    public Page<DadosListagemUsuario> listar(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarPerfil(Long id, DadosAtualizacaoPerfil dados) {
        var usuario = buscarAtivo(id);
        usuario.alterarPerfil(dados.perfil());
        return new DadosDetalhamentoUsuario(usuario);
    }

    @Transactional
    public void excluir(Long id, Usuario usuarioLogado) {
        if (id.equals(usuarioLogado.getId())) {
            throw new ValidacaoException("Você não pode excluir o seu próprio usuário");
        }
        var usuario = buscarAtivo(id);
        usuario.excluir();
    }

    @Transactional
    public void alterarSenha(Usuario usuarioLogado, DadosAlteracaoSenha dados) {
        var usuario = buscarAtivo(usuarioLogado.getId());

        // matches() compara a senha digitada com o hash salvo no banco
        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new ValidacaoException("Senha atual incorreta");
        }
        if (passwordEncoder.matches(dados.novaSenha(), usuario.getSenha())) {
            throw new ValidacaoException("A nova senha deve ser diferente da senha atual");
        }

        usuario.alterarSenha(passwordEncoder.encode(dados.novaSenha()));
    }

    private Usuario buscarAtivo(Long id) {
        return repository.findById(id)
                .filter(Usuario::isEnabled)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
    }
}