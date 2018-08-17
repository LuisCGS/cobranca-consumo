package br.com.luisclaudio.controladorcobranca.model;

public class Servidor {
	private String uuid;
	private Double horasConsumidas;
	
	public Servidor(String uuid, Double horasConsumidas) {
		super();
		this.uuid = uuid;
		this.horasConsumidas = horasConsumidas;
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
}
