package dominio;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;

import repositorio.Identificable;

@Entity
@Table(name="incidencia")
public class Incidencia implements Identificable {
	@Id
	private String id;
	@JoinColumn(name = "bicicleta")
	private Bicicleta bicicleta;
	@Column(name = "fechaCreacion", columnDefinition = "DATE")
	private LocalDateTime fechaCreacion;
	@Lob
	@Column(name = "descripcion")
	private String descripcion;
	@Column(name = "estado")
	private EstadoIncidencia estado;
	@Column(name = "fechaCierre", columnDefinition = "DATE")
	private LocalDateTime fechaCierre;
	@Lob
	@Column(name = "motivoCierre")
	private String motivoCierre;
	@Column(name = "operario")
	private String operario;

	public Incidencia(Bicicleta bici, String descripcion) {
		super();
		this.id = UUID.randomUUID().toString();
		this.bicicleta =bici;
		this.fechaCreacion = LocalDateTime.now();
		this.fechaCierre = null;
		this.motivoCierre = "";
		this.operario = "";
		this.descripcion = descripcion;
		this.estado = EstadoIncidencia.PENDIENTE;
	}
	public Incidencia() {
		
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id=id;
	}

	public Bicicleta getBicicleta() {
		return bicicleta;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public EstadoIncidencia getEstado() {
		return estado;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setEstado(EstadoIncidencia estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(LocalDateTime fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public String getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	public String getOperario() {
		return operario;
	}

	public void setOperario(String operario) {
		this.operario = operario;
	}
	
	public String toString() {
		return "Incidencia{" +
	            "id='" + id + '\'' +
	            ", bicicleta=" + bicicleta.toString() +
	            ", fechaCreacion=" + fechaCreacion +
	            ", descripcion='" + descripcion + '\'' +
	            ", estado=" + estado +
	            ", fechaCierre=" + fechaCierre +
	            ", motivoCierre='" + motivoCierre + '\'' +
	            ", operario='" + operario + '\'' +
	            '}';
	}

}
