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
    
    
    /*
      Método que verifica existência do usuário no sistema
     */
    public String login(){
        if(validaLogin()){ // chama metódo que retorna valor boolean, se o valor retornado for true a condição é satisfeita
            if(usuario.getTipoUsuario().equals("admin")){ // verifica se o status do usuário é admin
                return "faces/usuarioAdmin/index.html";//se condição foi satisfeita leva usuário a página inicial do administrador
            }
            else if(usuario.getTipoUsuario().equals("comum")){// verifica se o status do usuário é comum
                return "faces/usuarioComum/index.html";//se condição foi satisfeita, leva para página do usuário comum
            }
        }
        return "faces/login.xhtml";// se o usuário não existir no sistema, retorna o usuário a página de login
    }
    
    /*
      Método para validar o login do usuário
     */
    public boolean validaLogin(){
        Usuario2 u = daoUsuario.findUsuario(usuario.getMatricula(), usuario.getSenha()); /* a variável "u" recebe o objeto
        encontrado apartir da pesquisa no BD do usuário*/
        if (u != null && u.getStatus().equals("Ativo")){ //se o valor da variável "u" diferente de NULL, o usuário existe no BD
            usuario = u;
            return true; // retorna valor true quando cadastro do usuário existir no sistema
        } else {
            return false;// retorna valor false quando o usuário não estiver cadastrado no sistema
        }
    }
   
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
    
     /*
      Método para adicionar usuários a uma lista de usuários
     */
    public List<Usuario2> getUsuarios() {
        List usuarios = new ArrayList<Usuario2>();
        usuarios.add(usuario);
        return usuarios;
    }

    /**
     * @return the logout
     * metodo usado para fazer o logout do usuário no sistema
     */
    public String getLogout() {
        usuario = new Usuario2(); // a variável usuário é reiniciada
        autorizacao = ""; // a variável autorização é reiniciada
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
