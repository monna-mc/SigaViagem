/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.MotoristaJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
//import modelos.Endereco;
import modelos.Motorista;
import util.JPAUtil;

/**
 *
 * @author denis
 */
@ManagedBean
@RequestScoped
public class MotoristaBean {
    
    MotoristaJpaController motoristaDAO = new MotoristaJpaController(JPAUtil.factory);

    private Motorista motorista = new Motorista();
     
    private String mensagem;
    
    /**
     * Creates a new instance of MotoristaBean
     */
    public MotoristaBean() {
    }
    
    public void inserir(){
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try{
            motorista.setId(null);// insere o valor null como id
            motoristaDAO.create(motorista);// chamada o metódo create do DAO, para criar e salvar o motorista no BD 
            motorista = new Motorista();
            context.addMessage("formMotorista", new FacesMessage("Motorista foi inserido com sucesso!"));/*adiciona 
            mensagem quando o objeto motorista for inserido corretamente*/ 
        }catch(Exception ex){
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser inserido"));/*adiciona 
            mensagem quando algum tipo de erro na alteração acontecer */ 
           Logger.getLogger(MotoristaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            motoristaDAO.edit(motorista);// acessa o método do DAO, para editar o objeto motorista
            motorista = new Motorista();
            context.addMessage("formMotorista", new FacesMessage("Motorista foi alterado com sucesso!"));/*adiciona 
            mensagem quando o objeto motorista for inserido corretamente*/ 
        } catch (NonexistentEntityException ex) {
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser alterado"));/*adiciona 
            mensagem quando o objeto motorista a ser alterado não exitir no BD */ 
            Logger.getLogger(MotoristaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser alterado"));/*adiciona 
            mensagem quando algum tipo de erro na alteração acontecer */ 
            Logger.getLogger(MotoristaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void excluir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            motoristaDAO.destroy(motorista.getId());//acessa o método do DAO, para excluir do BD o motorista(usando sua ID)
            motorista = new Motorista();
            context.addMessage("formMotorista", new FacesMessage("Motorista foi excluido com sucesso!"));/*adiciona 
            mensagem quando o objeto motorista for excluído do BD */ 
        } catch (Exception ex) {
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser excluido"));/*adiciona 
            mensagem quando ocorrer erro na exclusão do objeto motorista*/ 
            Logger.getLogger(MotoristaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the motorista
     */
    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista){
        this.motorista = motorista;
    }
    
    public List<Motorista> getMotoristas(){
        List<Motorista> mot = new ArrayList<Motorista>();
        for (Motorista m : motoristaDAO.findMotoristaEntities()) {// percorre toda a lista de motoristas inserido no BD
            if (m.getId() != 1){// verifica se a Id do motorista não é igual a Zero
                mot.add(m);// se condição satisfeita adiciona o objeto motorista a lista 
            }
        }
        return mot;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
