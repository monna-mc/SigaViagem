/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.EstadoJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import modelos.Estado;
import util.JPAUtil;

/**
 *
 * @author denis
 */
@ManagedBean
@RequestScoped
public class EstadoBean {

     /**
     * Crea uma nova instancia de EstadoBean
     */
    private Estado estado = new Estado();
    EstadoJpaController daoEstado = new EstadoJpaController(JPAUtil.factory);
    private String mensagem;

    public EstadoBean() {
    }

    /**
     * Método inserir estado
     */
    public void inserir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            estado.setId(null); // insere o valor null como id de estado
            daoEstado.create(estado); // chamada o método create do DAO, para criar e salvar o estado no BD 
            estado = new Estado();
            context.addMessage("formEstado", new FacesMessage("Estado foi inserido com sucesso!"));/*adiciona mensagem 
            quando o objeto estado for inserido corretamente*/ 
        } catch (Exception ex) {
            context.addMessage("formEstado", new FacesMessage("Estado não pode ser inserido"));/*adiciona mensagem 
            quando o objeto estado não for inserido corretamente*/ 
            Logger.getLogger(EstadoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     /**
     * Método alterar estado
     */
    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            daoEstado.edit(estado);// acessa o método do DAO, para editar o objeto estado
            estado = new Estado();
            context.addMessage("formEstado", new FacesMessage("Estado foi alterado com sucesso!"));/*adiciona mensagem 
            quando o objeto estado for inserido corretamente*/ 
        } catch (NonexistentEntityException ex) {
            context.addMessage("formEstado", new FacesMessage("Estado não pode ser alterado 1")); /*adiciona mensagem
            quando o objeto estado a ser alterado não exitir no BD */ 
            Logger.getLogger(EstadoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formEstado", new FacesMessage("Estado não pode ser alterado 2"));/*adiciona mensagem
            quando algum tipo de erro na alteração da estado acontecer */ 
            Logger.getLogger(EstadoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método excluir estado
     */
    public void excluir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            daoEstado.destroy(estado.getId());// acessa o método do DAO, para excluir do BD o estado(usando sua ID)
            estado = new Estado();
            context.addMessage("formEstado", new FacesMessage("Estado foi excluido com sucesso"));/*adiciona mensagem 
            quando o objeto estado for excluído do BD */ 
        } catch (Exception ex) {
            context.addMessage("formEstado", new FacesMessage("Estado não pode ser excluido"));/*adiciona mensagem 
            quando ocorrer erro na exclusão do objeto estado*/ 
            Logger.getLogger(EstadoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /*
    Retorna a lista de todos os estados inserido no BD
    */
    public List<Estado> getEstados() {
        return daoEstado.findEstadoEntities();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
