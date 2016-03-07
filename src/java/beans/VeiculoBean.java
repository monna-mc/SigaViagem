/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.VeiculoJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import modelos.Veiculo;
import util.JPAUtil;

/**
 *
 * @author denis
 */
@ManagedBean
@RequestScoped
public class VeiculoBean {
    
    VeiculoJpaController veiculoDAO = new VeiculoJpaController(JPAUtil.factory);
    private Veiculo veiculo = new Veiculo();
    
    private String mensagem;

    /**
     * Creates a new instance of FuncionarioMB
     */
    public VeiculoBean() {
    }

    public void inserir() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            veiculo.setId(null);
            veiculo.setStatus("disponivel");
            veiculoDAO.create(veiculo);
            veiculo = new Veiculo();
            context.addMessage("formVeiculo", new FacesMessage("Veículo foi inserido com sucesso!"));
        } catch (Exception ex) {
            Logger.getLogger(VeiculoBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage("formVeiculo", new FacesMessage("Veículo não pode ser inserido"));
        }
    }

    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            veiculoDAO.edit(veiculo);
            veiculo = new Veiculo();
            context.addMessage("formVeiculo", new FacesMessage("Veículo foi alterado com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formVeiculo", new FacesMessage("Veículo não pode ser alterado"));
            Logger.getLogger(VeiculoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formVeiculo", new FacesMessage("Veículo não pode ser alterado"));
            Logger.getLogger(VeiculoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir() {
          FacesContext context = FacesContext.getCurrentInstance();
        try {
            veiculoDAO.destroy(veiculo.getId());
            veiculo = new Veiculo();
            context.addMessage("formVeiculo", new FacesMessage("Veículo foi excluido com sucesso!"));
        } catch (Exception ex) {
             context.addMessage("formVeiculo", new FacesMessage("Veiculo não pode ser excluido"));
            Logger.getLogger(VeiculoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
    
    public List<Veiculo> getVeiculos(){
        return veiculoDAO.findVeiculoEntities();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
