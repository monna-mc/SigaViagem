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
    
    //private Endereco endereco = new Endereco();
    
    private String mensagem;
    
    /**
     * Creates a new instance of MotoristaBean
     */
    public MotoristaBean() {
    }
    
    public void inserir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            motorista.setId(null);
            //motorista.setEndereco(endereco);
            motoristaDAO.create(motorista);
            motorista = new Motorista();
            //endereco = new Endereco();
            context.addMessage("formMotorista", new FacesMessage("Motorista foi inserido com sucesso!"));
        }catch(Exception ex){
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser inserido"));
           Logger.getLogger(MotoristaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //motorista.setEndereco(endereco);
            motoristaDAO.edit(motorista);
            motorista = new Motorista();
            //endereco = new Endereco();
            context.addMessage("formMotorista", new FacesMessage("Motorista foi alterado com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser alterado"));
            Logger.getLogger(MotoristaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser alterado"));
            Logger.getLogger(MotoristaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void excluir() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            motoristaDAO.destroy(motorista.getId());
            motorista = new Motorista();
            //endereco = new Endereco();
            context.addMessage("formMotorista", new FacesMessage("Motorista foi excluido com sucesso!"));
        } catch (Exception ex) {
            context.addMessage("formMotorista", new FacesMessage("Motorista não pode ser excluido"));
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

    /*public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }*/
    
    public List<Motorista> getMotoristas(){
        List<Motorista> mot = new ArrayList<Motorista>();
        for (Motorista m : motoristaDAO.findMotoristaEntities()) {
            if (m.getId() != 1){
                mot.add(m);
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
