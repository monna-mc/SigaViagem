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
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            veiculo.setId(null);// insere o valor null como id de veículo
            veiculo.setStatus("disponivel"); // insere o status do veículo sempre como disponivél
            veiculoDAO.create(veiculo);// chamada o metódo create do DAO, para criar e salvar o veículo no BD 
            veiculo = new Veiculo();
            context.addMessage("formVeiculo", new FacesMessage("Veículo foi inserido com sucesso!"));/*adiciona mensagem 
            quando o objeto veículo for inserido corretamente*/ 
        } catch (Exception ex) {
            Logger.getLogger(VeiculoBean.class.getName()).log(Level.SEVERE, null, ex);/*adiciona mensagem 
            quando o objeto veículo não for inserido corretamente*/ 
            context.addMessage("formVeiculo", new FacesMessage("Veículo não pode ser inserido"));
        }
    }

    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            veiculoDAO.edit(veiculo);// acessa o método do DAO, para editar o veículo
            veiculo = new Veiculo();
            context.addMessage("formVeiculo", new FacesMessage("Veículo foi alterado com sucesso!"));/*adiciona mensagem 
            quando o veículo for inserido corretamente*/ 
        } catch (NonexistentEntityException ex) {
            context.addMessage("formVeiculo", new FacesMessage("Veículo não pode ser alterado"));/*adiciona mensagem
            quando o veículo a ser alterado não exitir no BD */ 
            Logger.getLogger(VeiculoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formVeiculo", new FacesMessage("Veículo não pode ser alterado"));/*adiciona mensagem
            quando algum tipo de erro na alteração acontecer */ 
            Logger.getLogger(VeiculoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir() {
          FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            veiculoDAO.destroy(veiculo.getId());// acessa o método do DAO, para excluir do BD o veículo(usando sua ID)
            veiculo = new Veiculo();
            context.addMessage("formVeiculo", new FacesMessage("Veículo foi excluido com sucesso!"));/*adiciona mensagem 
            quando o veículo for excluído do BD */ 
        } catch (Exception ex) {
             context.addMessage("formVeiculo", new FacesMessage("Veiculo não pode ser excluido"));/*adiciona mensagem 
            quando ocorrer erro na exclusão do veículo*/ 
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
