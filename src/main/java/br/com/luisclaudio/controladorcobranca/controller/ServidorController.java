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
	
	@RequestMapping("/iniciarConsumo")
	public List<Servidor> iniciarUtilizacaoServidor(@RequestParam(value="uuid") String uuid, @RequestParam(value="horasConsumidas") String horasConsumidas) throws NumberFormatException {
		Util.validarUUID(uuid);
		Util.validarConversaoDouble(horasConsumidas);
		
		Boolean servidorJaExistente = false;
		Double horasConsumidasCalculado = new Double(Double.parseDouble(horasConsumidas));
		
		for (Servidor servidor : listaServidor) {
			if(servidor.getUuid().equals(uuid)) {
				servidorJaExistente = true;
				servidor.setHorasConsumidas(servidor.getHorasConsumidas() + horasConsumidasCalculado);
			} 
		}
		
		if(Boolean.FALSE.equals(servidorJaExistente)) {
			listaServidor.add(new Servidor(uuid, horasConsumidasCalculado));
		}
		
		return listaServidor;
	}
	
	@RequestMapping("/consumoServidor")
	public Map<String, Object> calcularConsumoServidor(@RequestParam(value="uuid") String uuid) {
		Map<String, Object> mapServidor = new HashMap<String, Object>();
		
		for (Servidor servidor : listaServidor) {
			if(servidor.getUuid().equals(uuid)) {
				mapServidor.put("consumoCalculado", String.valueOf(servidor.getHorasConsumidas() * valorUnitario));
				return mapServidor;
			}
		}
		
		mapServidor.put("consumoCalculado", Util.bundle.getString("mensagem.servidor.nao.encontrado"));
		return mapServidor;
		
		
	}
	
	@RequestMapping("/consumoTodosServidores")
	public Map<String, Object> calcularConsumoTodosServidores() {
		Map<String, Object> mapServidor = new HashMap<String, Object>();
		
		if(CollectionUtils.isEmpty(listaServidor)) {
			mapServidor.put("consumoCalculado", Util.bundle.getString("mensagem.servidores.inexistentes"));
			return mapServidor;
		}
		
		Double valorTotalHorasConsumo = 0.0;
		
		for (Servidor servidor : listaServidor) {
			valorTotalHorasConsumo += (servidor.getHorasConsumidas() * valorUnitario);
		}
		
		mapServidor.put("consumoCalculado", valorTotalHorasConsumo.toString());
		return mapServidor;
	}
}
