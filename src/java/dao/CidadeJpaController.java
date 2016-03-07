/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelos.Estado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.Cidade;

/**
 *
 * @author maycon
 */
public class CidadeJpaController implements Serializable {

    public CidadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cidade cidade) {
        //if (cidade.getEnderecos() == null) {
          //  cidade.setEnderecos(new ArrayList<Endereco>());
        //}
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado estado = cidade.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getId());
                cidade.setEstado(estado);
            }
            //List<Endereco> attachedEnderecos = new ArrayList<Endereco>();
            //for (Endereco enderecosEnderecoToAttach : cidade.getEnderecos()) {
              //  enderecosEnderecoToAttach = em.getReference(enderecosEnderecoToAttach.getClass(), enderecosEnderecoToAttach.getId());
                //attachedEnderecos.add(enderecosEnderecoToAttach);
            //}
            //cidade.setEnderecos(attachedEnderecos);
            em.persist(cidade);
            if (estado != null) {
                estado.getCidades().add(cidade);
                estado = em.merge(estado);
            }
            //for (Endereco enderecosEndereco : cidade.getEnderecos()) {
              //  Cidade oldCidadeOfEnderecosEndereco = enderecosEndereco.getCidade();
                //enderecosEndereco.setCidade(cidade);
            //    enderecosEndereco = em.merge(enderecosEndereco);
              //  if (oldCidadeOfEnderecosEndereco != null) {
                //    oldCidadeOfEnderecosEndereco.getEnderecos().remove(enderecosEndereco);
                  //  oldCidadeOfEnderecosEndereco = em.merge(oldCidadeOfEnderecosEndereco);
                //}
            //}
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /*public void edit(Cidade cidade) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cidade persistentCidade = em.find(Cidade.class, cidade.getId());
            Estado estadoOld = persistentCidade.getEstado();
            Estado estadoNew = cidade.getEstado();
            List<Endereco> enderecosOld = persistentCidade.getEnderecos();
            List<Endereco> enderecosNew = cidade.getEnderecos();
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getId());
                cidade.setEstado(estadoNew);
            }
            List<Endereco> attachedEnderecosNew = new ArrayList<Endereco>();
            for (Endereco enderecosNewEnderecoToAttach : enderecosNew) {
                enderecosNewEnderecoToAttach = em.getReference(enderecosNewEnderecoToAttach.getClass(), enderecosNewEnderecoToAttach.getId());
                attachedEnderecosNew.add(enderecosNewEnderecoToAttach);
            }
            enderecosNew = attachedEnderecosNew;
            cidade.setEnderecos(enderecosNew);
            cidade = em.merge(cidade);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getCidades().remove(cidade);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getCidades().add(cidade);
                estadoNew = em.merge(estadoNew);
            }
            for (Endereco enderecosOldEndereco : enderecosOld) {
                if (!enderecosNew.contains(enderecosOldEndereco)) {
                    enderecosOldEndereco.setCidade(null);
                    enderecosOldEndereco = em.merge(enderecosOldEndereco);
                }
            }
            for (Endereco enderecosNewEndereco : enderecosNew) {
                if (!enderecosOld.contains(enderecosNewEndereco)) {
                    Cidade oldCidadeOfEnderecosNewEndereco = enderecosNewEndereco.getCidade();
                    enderecosNewEndereco.setCidade(cidade);
                    enderecosNewEndereco = em.merge(enderecosNewEndereco);
                    if (oldCidadeOfEnderecosNewEndereco != null && !oldCidadeOfEnderecosNewEndereco.equals(cidade)) {
                        oldCidadeOfEnderecosNewEndereco.getEnderecos().remove(enderecosNewEndereco);
                        oldCidadeOfEnderecosNewEndereco = em.merge(oldCidadeOfEnderecosNewEndereco);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cidade.getId();
                if (findCidade(id) == null) {
                    throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }*/
    
    public void edit(Cidade cidade) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cidade = em.merge(cidade);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cidade.getId();
                if (findCidade(id) == null) {
                    throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cidade cidade;
            try {
                cidade = em.getReference(Cidade.class, id);
                cidade.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.", enfe);
            }
            Estado estado = cidade.getEstado();
            if (estado != null) {
                estado.getCidades().remove(cidade);
                estado = em.merge(estado);
            }
            //List<Endereco> enderecos = cidade.getEnderecos();
            //for (Endereco enderecosEndereco : enderecos) {
             //   enderecosEndereco.setCidade(null);
               // enderecosEndereco = em.merge(enderecosEndereco);
            //}
            em.remove(cidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cidade> findCidadeEntities() {
        return findCidadeEntities(true, -1, -1);
    }

    public List<Cidade> findCidadeEntities(int maxResults, int firstResult) {
        return findCidadeEntities(false, maxResults, firstResult);
    }

    private List<Cidade> findCidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cidade.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Cidade findCidade(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getCidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cidade> rt = cq.from(Cidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
