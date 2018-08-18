package br.com.luisclaudio.controladorcobranca.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.luisclaudio.controladorcobranca.model.Servidor;
import br.com.luisclaudio.controladorcobranca.util.Util;

@RestController
public class ServidorController {
	
	private static List<Servidor> listaServidor = new ArrayList<Servidor>();
	private static final Double valorUnitario = new Double(2);
	
	/**
	 * Método responsável por iniciar o consumo de um servidor 
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 17:49
	 * @param uuid : {@link String}
	 * @param horasConsumidas : {@link String}
	 * @return {@link List}<{@link Servidor}>
	 */
	@RequestMapping("/iniciarConsumo")
	public List<Servidor> iniciarUtilizacaoServidor(@RequestParam(value="uuid") String uuid, @RequestParam(value="horasConsumidas") String horasConsumidas) {
		Util.validarUUID(uuid);
		Util.validarConversaoDouble(horasConsumidas);
		
		Boolean servidorJaExistente = false;
		Double horasConsumidasCalculado = new Double(Double.parseDouble(horasConsumidas));
		
		for (Servidor servidor : listaServidor) {
			if(servidor.getUuid().equals(uuid)) {
				servidorJaExistente = true;
				servidor.setHorasConsumidas(servidor.getHorasConsumidas() + horasConsumidasCalculado);
				servidor.setTotalConsumido(servidor.getTotalConsumido() + calcularValorTotal(horasConsumidasCalculado));
			} 
		}
		
		if(Boolean.FALSE.equals(servidorJaExistente)) {
			listaServidor.add(new Servidor(uuid, horasConsumidasCalculado, calcularValorTotal(horasConsumidasCalculado)));
		}
		
		return listaServidor;
	}
	
	/**
	 * Método responsável por calcular o consumo de um servidor específico informado por parametro
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 17:48
	 * @param uuid : {@link String}
	 * @return {@link Map}<{@link String}, {@link Object}>
	 */
	@RequestMapping("/consumoServidor")
	public Map<String, Object> calcularConsumoServidor(@RequestParam(value="uuid") String uuid) {
		Map<String, Object> mapServidor = new HashMap<String, Object>();
		
		for (Servidor servidor : listaServidor) {
			if(servidor.getUuid().equals(uuid)) {
				mapServidor.put(Util.bundleMensagens.getString("key.consumo.horas"), servidor.getHorasConsumidas());
				mapServidor.put(Util.bundleMensagens.getString("key.valor.cobranca"), servidor.getTotalConsumido());
				
				return mapServidor;
			}
		}
		
		mapServidor.put(Util.bundleMensagens.getString("key.consumo.horas"), 
				Util.bundleMensagens.getString("mensagem.servidor.nao.encontrado"));
		return mapServidor;
		
		
	}
	
	/**
	 * Método responsável por receber a chamada externa e calcular o consumo total de todos os servidores
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 17:44
	 * @return {@link Map}<{@link String}, {@link Object}>
	 */
	@RequestMapping("/consumoTodosServidores")
	public Map<String, Object> calcularConsumoTodosServidores() {
		Map<String, Object> mapServidor = new HashMap<String, Object>();
		
		if(CollectionUtils.isEmpty(listaServidor)) {
			mapServidor.put(Util.bundleMensagens.getString("key.consumo.horas"), 
					Util.bundleMensagens.getString("mensagem.servidores.inexistentes"));
			
			return mapServidor;
		}
		
		Double valorTotalHorasConsumo = 0.0;
		
		for (Servidor servidor : listaServidor) {
			valorTotalHorasConsumo += servidor.getTotalConsumido();
		}
		
		mapServidor.put(Util.bundleMensagens.getString("key.consumo.horas"), valorTotalHorasConsumo);
		return mapServidor;
	}

	/**
	 * Método responsável por calcular o valor total de acordo com as horas consumidas 
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 17:43
	 * @param horasConsumidas : {@link Double}
	 * @return {@link Double}
	 */
	private Double calcularValorTotal(Double horasConsumidas) {
		return horasConsumidas * valorUnitario;
	}
}
