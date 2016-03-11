/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import modelos.Usuario2;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import util.JPAUtil;

/**
 *
 * @author denis
 */
@ManagedBean
@RequestScoped
public class UsuarioBean {

    /**
     * Creates a new instance of UsuarioBean
     */
    private Usuario2 usuario = new Usuario2();
    
    private String senha = new String();
    private String confSenha = new String();
    
    UsuarioJpaController daoUsuario = new UsuarioJpaController(JPAUtil.factory);
    
    private String mensagem;

    public UsuarioBean() {
    }
    
    /* 
        verifica se os campos de senha do formulário são iguais
    */
    private boolean confirmarSenha() {
        if (senha.compareTo(confSenha)==0){
            return true;
        }
        return false;
    }

    
    public void inserir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.*/
        try{
            usuario.setId(null); // insere o valor null como id
            if(confirmarSenha()){//chama método para confirmar se os campos senha conferem , se o valor retornado for true a condição é satisfeita
                if(senha.equals("") || confSenha.equals("")){ // verifica se o preenchimento dos campos senhas não está em branco
                    context.addMessage("formUsuario", new FacesMessage("Campo Senha e/ou Confirmar Senha são Obrigatórios."));
                }else{//entra na condição quando campo senha estiver preenchido corretamente 
                    usuario.setSenha(senha);
                    daoUsuario.create(usuario);// chama metodo so DAO para criar novo usuário
                    context.addMessage("formUsuario", new FacesMessage("Usuario foi inserido com sucesso!"));/*adiciona 
                    mensagem quando o usuário for inserido corretamente*/ 
                    usuario = new Usuario2();
                    senha = new String();
                    confSenha = new String();
                }
            }else{
                context.addMessage("formUsuario", new FacesMessage("Senhas não são iguais."));/*adiciona mensagem quando 
                senhas digitadas no formulários forem diferentes*/ 
            }
        } catch (Exception ex) {
            context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser inserido"));/*adiciona mensagem quando 
            ocorrer algum erro e usuário não puder ser inserido no BD*/ 
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.*/
        try {
            if (senha.equals("")){ // Verifica se campo senhas no formulário está em branco
                Usuario2 u = daoUsuario.findUsuario(usuario.getId()); // pesquisa o usuário pelo Id
                usuario.setSenha(u.getSenha()); // insere a senha do usuário, que consta no BD no campo senha
            }else{
                usuario.setSenha(senha);// se campo senha não estiver vazio, insere o valor digitado no formulário na variável senha
            }
            daoUsuario.edit(usuario); // chama método do DAO para alterar os dados do usuário no BD
            context.addMessage("formUsuario", new FacesMessage("Usuario foi alterado com sucesso!"));// adiciona mensagem
            usuario = new Usuario2();
            senha = new String();
            confSenha = new String();
        } catch (NonexistentEntityException ex) {
            context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser alterado"));/* adiciona mensagem de erro, quando
            o cadastro do usuário que está sendo alterado não for encontrado no BD.*/
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser alterado"));// adiciona mensagem de erro
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.*/
        try {
            daoUsuario.destroy(usuario.getId());//usa o Id do objeto usuário para apagar seu cadastro do sistema, através do método no DAO
            context.addMessage("formUsuario", new FacesMessage("Usuario foi excluido com sucesso!"));//adiciona mensagem de exclusão realizada
            usuario = new Usuario2();
            senha = new String();
            confSenha = new String();
        } catch (Exception ex) {
             context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser excluido"));/*adiciona mensagem quando algum
             erro acontecer e a exclusão não puder ser efetuada*/
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Usuario2 getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario2 usuario) {
        this.usuario = usuario;
    }
    
    public List<Usuario2> getUsuarios(){
        return daoUsuario.findUsuarioEntities();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public String getConfSenha() {
        return confSenha;
    }

    public void setConfSenha(String confSennha) {
        this.confSenha = confSennha;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
