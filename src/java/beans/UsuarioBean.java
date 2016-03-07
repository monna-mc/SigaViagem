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
    //EnderecoJpaController daoEndereco = new EnderecoJpaController(JPAUtil.factory);
    
    private String mensagem;

    public UsuarioBean() {
    }
    
    private boolean confirmarSenha() {
        if (senha.compareTo(confSenha)==0){
            return true;
        }
        return false;
    }

    public void inserir() {
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            usuario.setId(null);
            if(confirmarSenha()){
                if(senha.equals("") || confSenha.equals("")){
                    context.addMessage("formUsuario", new FacesMessage("Campo Senha e/ou Confirmar Senha são Obrigatórios."));
                }else{
                    usuario.setSenha(senha);
                    daoUsuario.create(usuario);
                    context.addMessage("formUsuario", new FacesMessage("Usuario foi inserido com sucesso!"));
                    usuario = new Usuario2();
                    senha = new String();
                    confSenha = new String();
                }
            }else{
                context.addMessage("formUsuario", new FacesMessage("Senhas não são iguais."));
            }
        } catch (Exception ex) {
            context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser inserido"));
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //if (confirmarSenha()) {
            if (senha.equals("")){
                Usuario2 u = daoUsuario.findUsuario(usuario.getId());
                usuario.setSenha(u.getSenha());
            }else{
                usuario.setSenha(senha);
            }
            daoUsuario.edit(usuario);
            context.addMessage("formUsuario", new FacesMessage("Usuario foi alterado com sucesso!"));
            usuario = new Usuario2();
            senha = new String();
            confSenha = new String();
            //}else{
              //  context.addMessage("formUsuario", new FacesMessage("Senhas não são iguais."));
            //}
        } catch (NonexistentEntityException ex) {
            context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser alterado"));
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser alterado"));
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //if (confirmarSenha()) {
            daoUsuario.destroy(usuario.getId());
            context.addMessage("formUsuario", new FacesMessage("Usuario foi excluido com sucesso!"));
            usuario = new Usuario2();
            senha = new String();
            confSenha = new String();
            //}else{
               // context.addMessage("formUsuario", new FacesMessage("Senhas não são iguais."));
            //}
        } catch (Exception ex) {
             context.addMessage("formUsuario", new FacesMessage("Usuario não pode ser excluido"));
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Usuario2 getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario2 usuario) {
        //setEndereco(usuario.getEndereco());
        this.usuario = usuario;
    }

    /*public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }*/
    
    public List<Usuario2> getUsuarios(){
        return daoUsuario.findUsuarioEntities();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    //public List<Endereco> getEnderecos(){
      //  return daoEndereco.findEnderecoEntities();
    //}

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
