package br.com.fiap.autoescola.domain.usuario;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        Perfil perfil,
        Boolean ativo
) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getPerfil(), usuario.getAtivo());
    }
}