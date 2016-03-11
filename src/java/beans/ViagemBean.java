/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.MotoristaJpaController;
import dao.VeiculoJpaController;
import dao.ViagemJpaController;
import dao.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import modelos.Cidade;
//import modelos.Endereco;
import modelos.Usuario2;
import modelos.Motorista;
import modelos.Veiculo;
import modelos.Viagem;
import util.JPAUtil;

/**
 *
 * @author denis
 */
@ManagedBean
@SessionScoped
public class ViagemBean {

    /**
     * Creates a new instance of ViagemBean
     */
    private Viagem viagem = new Viagem();
    private Motorista motorista = new Motorista();
    private Usuario2 usuario = new Usuario2();
    private Veiculo veiculo = new Veiculo();
    private Cidade origem = new Cidade();
    private Cidade destino = new Cidade();
    private List<Viagem> viagens = new ArrayList<Viagem>();
    private Date dat = null;
    ViagemJpaController daoViagem = new ViagemJpaController(JPAUtil.factory);
    MotoristaJpaController daoMotorista = new MotoristaJpaController(JPAUtil.factory);
    VeiculoJpaController daoVeiculo = new VeiculoJpaController(JPAUtil.factory);
    private String mensagem;

    public ViagemBean() {
    }

    /*
     Método Cadastar viagem
     */
    public void inserir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            viagem.setId(null);// inicializa variável Id com valor NULL
            if (!motorista.equals(new Motorista())){
                viagem.setMotorista(motorista);
            }else{
                viagem.setMotorista(daoMotorista.findMotorista(Long.min(1, 2)));
            }
            viagem.setVeiculo(veiculo);
            viagem.setUsuario(usuario);
            viagem.setDestino(getDestino());
            viagem.setOrigem(getOrigem());
            daoViagem.create(viagem);
            motorista = new Motorista();
            usuario = new Usuario2();
            veiculo = new Veiculo();
            setOrigem(new Cidade());
            setDestino(new Cidade());
            viagem = new Viagem();
            context.addMessage("formViagem", new FacesMessage("Viagem foi inserido com sucesso!"));
        } catch (Exception ex) {
            Logger.getLogger(ViagemBean.class.getName()).log(Level.SEVERE, null, ex);
            context.addMessage("formViagem", new FacesMessage("Viagem não pode ser inserido"));
        }
    }

    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            viagem.setMotorista(motorista);
            viagem.setVeiculo(veiculo);
            viagem.setUsuario(usuario);
            viagem.setOrigem(getOrigem());
            viagem.setDestino(getDestino());            
            daoViagem.edit(viagem);
            motorista = new Motorista();
            veiculo = new Veiculo();
            usuario = new Usuario2();
            setOrigem(new Cidade());
            setDestino(new Cidade());
            viagem = new Viagem();
            context.addMessage("formViagem", new FacesMessage("Viagem foi alterado com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formViagem", new FacesMessage("Viagem não pode ser alterado"));
            Logger.getLogger(ViagemBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formViagem", new FacesMessage("Viagem não pode ser alterado"));
            Logger.getLogger(ViagemBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir() {
        FacesContext context = FacesContext.getCurrentInstance();/*é usado para acessar informações relacionadas 
        ao processamento de cada requisição JSF e a renderização da resposta correspondente.)*/
        try {
            daoViagem.destroy(viagem.getId());
            motorista = new Motorista();
            veiculo = new Veiculo();
            usuario = new Usuario2();
            setOrigem(new Cidade());
            setDestino(new Cidade());
            viagem = new Viagem();
            context.addMessage("formViagem", new FacesMessage("Viagem foi excluido com sucesso!"));
        } catch (Exception ex) {
            context.addMessage("formViagem", new FacesMessage("Viagem não pode ser excluido"));
            Logger.getLogger(ViagemBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        setMotorista(viagem.getMotorista());
        setUsuario(viagem.getUsuario());
        setVeiculo(new Veiculo());
        setOrigem(viagem.getOrigem());
        setDestino(viagem.getDestino());
        viagem.setObservacao(viagem.getObservacao());
        this.viagem = viagem;
        viagem.setDataSaida(null);
        viagem.setDataRetorno(null);
    }
    
    public void pesquisarPorData(){
        for (Viagem v : daoViagem.findViagemEntities()) {
            if (v.getMotorista().getId() != 1){
                if (v.getDataSaida().getDay() == dat.getDay() && v.getDataSaida().getMonth() == dat.getMonth() && v.getDataSaida().getYear() == dat.getYear()){
                    viagens.add(v);
                }
            }
        }
    }
    
    public void pesquisarTodos(){
        dat = null;
    }

    public List<Viagem> getViagens() {
        if (dat == null){
            return daoViagem.findViagemEntities();
        }
        return viagens;
    }
    
    /**
     * @param viagens the viagens to set
     */
    public void setViagens(List<Viagem> viagens) {
        dat = null;
        this.viagens = viagens;
    }
    
    //public List<Motorista> getMotoristas() {
      //  if (veiculo.equals(new Veiculo())){
        //    return daoMotorista.findMotoristaEntities();
       // }
    //    veiculo = daoVeiculo.findVeiculo(veiculo.getId());
      //  System.out.println(veiculo.getId());
        //List<Motorista> motoristas = new ArrayList<Motorista>();
    //    for (Motorista m : daoMotorista.findMotoristaEntities()){
      //      if (m.getCatCNH().equals(veiculo.getCNHreq())){
        //        motoristas.add(m);
          //  }
    //    }
      //  return motoristas;
    //}
    
    public List<Motorista> mostarMotoristas(){
        if (viagem.getDataSaida() != null && viagem.getDataRetorno() != null){
            List<Motorista> mot = new ArrayList<Motorista>();
            int var2 = 0;
            veiculo = daoVeiculo.findVeiculo(veiculo.getId());
            List<Motorista> motRegeitado = new ArrayList<Motorista>();
            for (Viagem via : daoViagem.findViagemEntities()){
                if (via.getMotorista().getId() != 1){
                    if (via.getStatus().equals("autorizada")){
                        if (!(((viagem.getDataSaida().after(via.getDataSaida())||viagem.getDataSaida().equals(via.getDataSaida())) && (viagem.getDataSaida().before(via.getDataRetorno())||viagem.getDataSaida().equals(via.getDataRetorno()))) ||
                            ((viagem.getDataRetorno().after(via.getDataSaida())||viagem.getDataRetorno().equals(via.getDataSaida())) && (viagem.getDataRetorno().before(via.getDataRetorno())||viagem.getDataRetorno().equals(via.getDataRetorno()))) ||
                            ((viagem.getDataSaida().before(via.getDataSaida())||viagem.getDataSaida().equals(via.getDataSaida())) && (viagem.getDataRetorno().after(via.getDataRetorno())||viagem.getDataRetorno().equals(via.getDataRetorno()))))){
                            //if (via.getMotorista().getCatCNH().equals(veiculo.getCNHreq())){
                            if (via.getMotorista().getCatCNH().equals("D")){
                                for(Motorista moto : mot){
                                    if (moto.getId() == via.getMotorista().getId()){
                                        var2 = 1;
                                    }
                                }
                                if (var2 == 0){
                                    mot.add(daoMotorista.findMotorista(via.getMotorista().getId()));
                                }else{
                                    var2 = 0;
                                }
                            }else{
                                if(via.getMotorista().getCatCNH().equals("B")){
                                    if (via.getMotorista().getCatCNH().equals(veiculo.getCNHreq())){
                                        for(Motorista moto : mot){
                                            if (moto.getId() == via.getMotorista().getId()){
                                                var2 = 1;
                                            }
                                        }
                                        if (var2 == 0){
                                            mot.add(daoMotorista.findMotorista(via.getMotorista().getId()));
                                        }else{
                                            var2 = 0;
                                        }
                                    }
                                }
                            }
                        }else{
                            motRegeitado.add(via.getMotorista());
                        }
                    }
                }
            }
            int var = 0;
            for (Motorista motor : daoMotorista.findMotoristaEntities()){
                if (motor.getId() != 1){
                    var = 0;
                    for (Viagem viag : daoViagem.findViagemEntities()){
                        //System.out.println("Eu Te Amo DEUS");
                        //System.out.println(viag.getMotorista().getId());
                        if (viag.getMotorista().getId() != 1){
                            //System.out.println(motor.getId());
                            //System.out.println(viag.getMotorista().getId());
                            if (motor.getId() == viag.getMotorista().getId()) {
                                var = var + 1;
                            }
                            if (!viag.getStatus().equals("autorizada")){
                                var = 0;
                            }
                        }
                    }
                    veiculo = daoVeiculo.findVeiculo(veiculo.getId());
                    if ((var == 0 && veiculo.getCNHreq().equals(motor.getCatCNH())) || (var == 0 && veiculo.getCNHreq().equals("B"))){
                        mot.add(motor);
                    }
                }
            }
            List<Motorista> m6 = new ArrayList<Motorista>();
            var = 0;
            for (Motorista m5 : mot){
                for (Motorista m4 : motRegeitado){
                    if (m4.getId() == m5.getId()){
                        var = 1;
                    }
                }
                if (var == 0){
                    m6.add(m5);
                }else{
                    var = 0;
                }
            }
            List<Motorista> m3 = new ArrayList<Motorista>();
            Set<Motorista> set = new HashSet<>();
            set = new HashSet<>();
            for (Motorista m1 : m6){
                set.add(m1);
            }
            for (Motorista m2 : set){
                m3.add(m2);
            }
            return m3;
        }
        List<Motorista> mResult = new ArrayList<Motorista>();
        for (Motorista m : daoMotorista.findMotoristaEntities()){
            if (m.getId() != 1){
                mResult.add(m);
            }
        }
        return mResult;
    }
    
    public List<Veiculo> mostarVeiculosPorData(){
        if (viagem.getDataSaida() != null && viagem.getDataRetorno() != null){
            List<Veiculo> vei = new ArrayList<Veiculo>();
            List<Veiculo> veiRegeitado = new ArrayList<Veiculo>();
            int var2 = 0;
            for (Viagem via : daoViagem.findViagemEntities()){
                if (via.getMotorista().getId() != 1){
                    if (via.getStatus().equals("autorizada")){
                        if (!(((viagem.getDataSaida().after(via.getDataSaida())||viagem.getDataSaida().equals(via.getDataSaida())) && (viagem.getDataSaida().before(via.getDataRetorno())||viagem.getDataSaida().equals(via.getDataRetorno()))) ||
                            ((viagem.getDataRetorno().after(via.getDataSaida())||viagem.getDataRetorno().equals(via.getDataSaida())) && (viagem.getDataRetorno().before(via.getDataRetorno())||viagem.getDataRetorno().equals(via.getDataRetorno()))) ||
                            ((viagem.getDataSaida().before(via.getDataSaida())||viagem.getDataSaida().equals(via.getDataSaida())) && (viagem.getDataRetorno().after(via.getDataRetorno())||viagem.getDataRetorno().equals(via.getDataRetorno()))))){
                            for (Veiculo vTeste : veiRegeitado){
                                if(via.getVeiculo().getId() == vTeste.getId()){
                                    var2 = 1;
                                }
                            }
                            if (var2 == 0){
                                vei.add(daoVeiculo.findVeiculo(via.getVeiculo().getId()));
                            }else{
                                var2 = 0;
                            }
                        }
                        else{
                            veiRegeitado.add(via.getVeiculo());
                        }
                    }
                }
            }
            List<Veiculo> v3 = new ArrayList<Veiculo>();
            Set<Veiculo> set = new HashSet<>();
            for (Veiculo v1 : vei){
                set.add(v1);
            }
            for (Veiculo v2 : set){
                v3.add(v2);
            }
            int var = 0;
            for (Veiculo veic : daoVeiculo.findVeiculoEntities()){
                var = 0;
                for (Viagem viag : daoViagem.findViagemEntities()){
                    if (viag.getMotorista().getId() != 1){
                        if (veic.getId() == viag.getVeiculo().getId()) {
                            var = var + 1;
                        }
                        if (!viag.getStatus().equals("autorizada")){
                            var = 0;
                        }
                    }
                }
                if (var == 0){
                    v3.add(veic);
                }
            }
            List<Veiculo> v6 = new ArrayList<Veiculo>();
            var = 0;
            for (Veiculo v5 : v3){
                for (Veiculo v4 : veiRegeitado){
                    if (v4.getId() == v5.getId()){
                        var = 1;
                    }
                }
                if (var == 0){
                    v6.add(v5);
                }else{
                    var = 0;
                }
            }
            set = new HashSet<>();
            for (Veiculo v1 : v6){
                set.add(v1);
            }
            v3 = new ArrayList<Veiculo>();
            for (Veiculo v2 : set){
                v3.add(v2);
            }
            return v3;
        }
        List<Veiculo> vResult = new ArrayList<Veiculo>();
        for (Veiculo v : daoVeiculo.findVeiculoEntities()){
            if (v.getId() != 1){
                vResult.add(v);
            }
        }
        return vResult;
    }

    public Motorista getMotorista() {
        return motorista;
    }
    
    public void zerarCamposIndex() throws IOException{
        viagem = new Viagem();
        veiculo = new Veiculo();
        motorista = new Motorista();
        origem = new Cidade();
        destino = new Cidade();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
    }
    
    public void zerarCamposLogin() throws IOException{
        viagem = new Viagem();
        veiculo = new Veiculo();
        motorista = new Motorista();
        origem = new Cidade();
        destino = new Cidade();
        FacesContext.getCurrentInstance().getExternalContext().redirect("../faces/login.xhtml");
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Usuario2 getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario2 usuario) {
        this.usuario = usuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * @return the origem
     */
    public Cidade getOrigem() {
        return origem;
    }

    /**
     * @param origem the origem to set
     */
    public void setOrigem(Cidade origem) {
        this.origem = origem;
    }

    /**
     * @return the destino
     */
    public Cidade getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(Cidade destino) {
        this.destino = destino;
    }

    /**
     * @return the dat
     */
    public Date getDat() {
        return dat;
    }

    /**
     * @param dat the dat to set
     */
    public void setDat(Date dat) {
        this.dat = dat;
    }
}
