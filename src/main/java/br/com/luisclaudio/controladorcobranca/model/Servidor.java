package br.com.luisclaudio.controladorcobranca.model;

public class Servidor {
	private String uuid;
	private Double horasConsumidas;
	private Double totalConsumido;
	
	public Servidor(String uuid, Double horasConsumidas, Double totalConsumido) {
		super();
		this.uuid = uuid;
		this.horasConsumidas = horasConsumidas;
		this.totalConsumido = totalConsumido;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Double getHorasConsumidas() {
		return horasConsumidas;
	}
	public void setHorasConsumidas(Double horasConsumidas) {
		this.horasConsumidas = horasConsumidas;
	}

	public Double getTotalConsumido() {
		return totalConsumido;
	}

	public void setTotalConsumido(Double totalConsumido) {
		this.totalConsumido = totalConsumido;
	}
}
