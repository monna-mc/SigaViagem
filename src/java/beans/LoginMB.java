/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioJpaController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import modelos.Usuario2;
import util.JPAUtil;

/**
 *
 * @author maycon
 */
@ManagedBean
@SessionScoped
public class LoginMB {
    
    private Usuario2 usuario = new Usuario2();
    
    UsuarioJpaController daoUsuario = new UsuarioJpaController(JPAUtil.factory);
    
    private String logout;
    
    private String autorizacao = "";

    /**
     * Creates a new instance of LoginMB
     */
    public LoginMB(){
    }
    
    public String login(){
        if(validaLogin()){
            if(usuario.getTipoUsuario().equals("admin")){
                return "faces/usuarioAdmin/index.html";
            }
            else if(usuario.getTipoUsuario().equals("comum")){
                return "faces/usuarioComum/index.html";
            }
        }
        return "faces/login.xhtml";
    }
    
    public boolean validaLogin(){
        Usuario2 u = daoUsuario.findUsuario(usuario.getMatricula(), usuario.getSenha());
        if (u != null && u.getStatus().equals("Ativo")){
            usuario = u;
            //UsuarioMB umb = FacesUtil.getUsuarioMB();
            //umb.setUsuario(usuario);
            return true;
        } else {
            return false;
        }
    }
    
    //public String logout(){
      //  usuario = new Usuario2();
        //logado = false;
        
        //return "../faces/login.xhtml";
    //}

    /**
     * @return the usuario
     */
    public Usuario2 getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario2 usuario) {
        this.usuario = usuario;
    }
    
    public List<Usuario2> getUsuarios() {
        List usuarios = new ArrayList<Usuario2>();
        usuarios.add(usuario);
        return usuarios;
    }

    /**
     * @return the logout
     */
    public String getLogout() {
        usuario = new Usuario2();
        autorizacao = "";
        return logout;
    }

    /**
     * @param logout the logout to set
     */
    public void setLogout(String logout) {
        this.logout = logout;
    }

    /**
     * @return the autorizacao
     */
    public String getAutorizacao(){
        Usuario2 u = new Usuario2();
        if (usuario.equals(u)){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");  
            } catch (IOException ex) {  
                Logger.getLogger(LoginMB.class.getName()).log(Level.SEVERE, null, ex); 
            }
            return autorizacao;
        }
        if (!usuario.getTipoUsuario().equals(autorizacao)){
            try {  
                FacesContext.getCurrentInstance().getExternalContext().redirect("../usuarioComum/index.html");  
            } catch (IOException ex) {  
                Logger.getLogger(LoginMB.class.getName()).log(Level.SEVERE, null, ex); 
            }
        }
        return autorizacao;
    }

    /**
     * @param autorizacao the autorizacao to set
     */
    public void setAutorizacao(String autorizacao) {
        this.autorizacao = autorizacao;
    }
    
    public void testeAutorizacao(String autorizacao) {
        this.autorizacao = autorizacao;
    }
}
