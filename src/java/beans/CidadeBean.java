/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelos.Cidade;
import dao.CidadeJpaController;
import dao.EstadoJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelos.Estado;
import util.JPAUtil;

/**
 *
 * @author denis
 */
@ManagedBean
@RequestScoped
public class CidadeBean {

    
    private Cidade cidade = new Cidade();
    private Estado estado = new Estado();
    CidadeJpaController daoCidade = new CidadeJpaController(JPAUtil.factory);
    EstadoJpaController daoEstado = new EstadoJpaController(JPAUtil.factory);
    
    private String mensagem;

    public CidadeBean() {
    }

    /**
     * Método inserir cidade
     */
    public void inserir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.É por meio deste objeto 
        que temos acesso ao objeto Application (que contém informações e configurações da aplicação JSF)*/
        try{
            cidade.setId(null);// insere o Id como nulo no objeto cidade
            cidade.setEstado(estado); // insere o obejto estado em cidade
            daoCidade.create(cidade); // chamada o método create do DAO, para criar e salvar a cidade no BD 
            estado = new Estado(); 
            cidade = new Cidade();
        } catch (Exception ex) {
            context.addMessage("formCidade", new FacesMessage("Cidade não pode ser inserido!"));/*adiciona mensagem 
            quando ocorrer erro na inserção da cidade*/ 
            Logger.getLogger(CidadeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        context.addMessage("formCidade", new FacesMessage("Cidade foi inserido com sucesso!"));/*adiciona mensagem 
            quando o objeto cidade for inserido corretamente*/ 
        
    }

    /**
     * Método excluir cidade
     */
    public void excluir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.É por meio deste objeto 
        que temos acesso ao objeto Application (que contém informações e configurações da aplicação JSF)*/
        try {
            daoCidade.destroy(cidade.getId());// acessa o método do DAO, para excluir do BD a cidade(usando sua ID)
            estado = new Estado();
            cidade = new Cidade();
        } catch (Exception ex) {
            context.addMessage("formCidade", new FacesMessage("Cidade não pode ser excluído!"));/*adiciona mensagem 
            quando ocorrer erro na exclusão do objeto cidade*/ 
            Logger.getLogger(CidadeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        context.addMessage("formCidade", new FacesMessage("Cidade foi excluída com sucesso!"));/*adiciona mensagem 
            quando o objeto cidade for excluído do BD */ 
    }

    /**
     * Método alterar cidade
     */
    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.É por meio deste objeto 
        que temos acesso ao objeto Application (que contém informações e configurações da aplicação JSF)*/
        try {
            cidade.setEstado(estado);// insere o objeto estado (selecionado), no objeto cidade
            daoCidade.edit(cidade); // acessa o método do DAO, para editar o objeto cidade
            estado = new Estado();
            cidade = new Cidade();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CidadeBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage("formCidade", new FacesMessage("Cidade não foi alterado!"));/*adiciona mensagem
            quando o objeto cidade a ser alterado não exitir no BD */ 
        } catch (Exception ex) {
            Logger.getLogger(CidadeBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage("formCidade", new FacesMessage("Cidade não foi alterado!"));/*adiciona mensagem
            quando algum tipo de erro na alteração da cidade ocorrer */ 
        }
        context.addMessage("formCidade", new FacesMessage("Cidade foi alterado com sucesso!"));/*adiciona mensagem
            quando o objeto cidade for alterado no BD */ 
    }

    public Cidade getCidade() {
        return cidade;
    }
   
    /*
      Método para associar o objeto cidade ao objeto estado
     */
    public void setCidade(Cidade cidade) {
        setEstado(cidade.getEstado());
        this.cidade = cidade;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

     /*
      Retorna a lista de cidades apartir do método do DAO "findCidadeEntities"
     */
    public List<Cidade> getCidades() {
        return daoCidade.findCidadeEntities();
    }
    
    //<!--p:autoComplete id="origem" value="#{viagemBean.origem.cidade}" completeMethod="#{cidadeBean.mostrarCidades}" var="origem" itemValue="#{origem}" itemLabel="#{origem.nome}"/-->
    
     /*
      Pesquisa e retorna uma ou mais cidade pelo nome
     */
    public List<Cidade> mostrarCidades(String query) {
        List<Cidade> cidadesResultantes = new ArrayList<Cidade>();
        for (Cidade c : daoCidade.findCidadeEntities()) {
            if (c.getNome().startsWith(query)){
                cidadesResultantes.add(c);
            }
        }
        return cidadesResultantes;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    /*
    Retorna a lista de todas as cidades inserida no BD
    */
    public List<Estado> getEstados(){
        return daoEstado.findEstadoEntities();    
    }
}
