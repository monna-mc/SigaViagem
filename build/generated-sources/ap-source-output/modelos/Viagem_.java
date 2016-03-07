package modelos;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Viagem.class)
public abstract class Viagem_ {

	public static volatile SingularAttribute<Viagem, Veiculo> veiculo;
	public static volatile SingularAttribute<Viagem, String> observacao;
	public static volatile SingularAttribute<Viagem, Motorista> motorista;
	public static volatile SingularAttribute<Viagem, Date> dataRetorno;
	public static volatile SingularAttribute<Viagem, Cidade> origem;
	public static volatile SingularAttribute<Viagem, Usuario2> usuario;
	public static volatile SingularAttribute<Viagem, Long> id;
	public static volatile SingularAttribute<Viagem, Cidade> destino;
	public static volatile SingularAttribute<Viagem, Date> dataSaida;
	public static volatile SingularAttribute<Viagem, String> status;

}

