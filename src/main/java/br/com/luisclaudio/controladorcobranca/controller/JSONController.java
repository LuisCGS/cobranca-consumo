package br.com.luisclaudio.controladorcobranca.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.luisclaudio.controladorcobranca.model.Servidor;
import br.com.luisclaudio.controladorcobranca.util.Util;

@RestController
public class JSONController {
	
	private static final Double valorUnitario = new Double(Util.bundleConfig.getString("valor.unitario"));
	private static final String caminhoInicioNovoConsumo = "/iniciarConsumo";
	private static final String caminhoCalcularConsumoServidor = "/consumoServidor";
	private static final String caminhoCalcularConsumoTodosServidores = "/consumoTodosServidores";
	private static List<Servidor> listaServidor = new ArrayList<Servidor>();
	
	@RequestMapping("/")
	public Map<String, String> inicio() {
		Map<String, String> listaServicos = new LinkedHashMap<String, String>();
		
		listaServicos.put(Util.bundleMensagens.getString("codigo.status"), 
				Util.bundleConfig.getString("codigo.status.ok"));
		listaServicos.put(Util.bundleMensagens.getString("mensagem.inicial.novo.cosumo"), 
				caminhoInicioNovoConsumo);
		listaServicos.put(Util.bundleMensagens.getString("mensagem.inicial.consulta.servidor"), 
				caminhoCalcularConsumoServidor);
		listaServicos.put(Util.bundleMensagens.getString("mensagem.inicial.consulta.servidores"), 
				caminhoCalcularConsumoTodosServidores);
		listaServicos.put(Util.bundleMensagens.getString("mensagem.inicial.ajuda"), 
				Util.bundleMensagens.getString("mensagem.inicial.documentacao.api"));
		
		return listaServicos;
	}
	
	/**
	 * Método responsável por iniciar o consumo de um servidor 
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 17:49
	 * @param uuid : {@link String}
	 * @param horasConsumidas : {@link String}
	 * @return {@link List}<{@link Servidor}>
	 */
	@RequestMapping(caminhoInicioNovoConsumo)
	public Map<String, Object> iniciarUtilizacaoServidor(@RequestParam(value="uuid") String uuid, @RequestParam(value="horasConsumidas") String horasConsumidas) {
		Map<String, Object> listaMensagensRetorno = new LinkedHashMap<String, Object>();
		horasConsumidas = horasConsumidas.replace(",", ".");
		
		if(Boolean.FALSE.equals(Util.validarUUID(uuid))) {
			adicionarExcecaoUUID(listaMensagensRetorno);
			return listaMensagensRetorno;
		}
		
		if(Boolean.FALSE.equals(Util.validarConversaoDouble(horasConsumidas))) {
			adicionarExcecaoHorasConsumidas(listaMensagensRetorno);
			return listaMensagensRetorno;
		}
		
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
		
		listaMensagensRetorno.put(Util.bundleMensagens.getString("codigo.status"), 
				Util.bundleConfig.getString("codigo.status.ok"));
		listaMensagensRetorno.put(Util.bundleMensagens.getString("mensagem.padrao"), 
				Util.bundleMensagens.getString("mensagem.sucesso").replace("{0}", uuid));
		
		return listaMensagensRetorno;
	}
	
	/**
	 * Método responsável por calcular o consumo de um servidor específico informado por parametro
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 17:48
	 * @param uuid : {@link String}
	 * @return {@link Map}<{@link String}, {@link Object}>
	 */
	@RequestMapping(caminhoCalcularConsumoServidor)
	public Map<String, Object> calcularConsumoServidor(@RequestParam(value="uuid") String uuid) {
		Map<String, Object> mapServidor = new LinkedHashMap<String, Object>();
		
		if(Boolean.FALSE.equals(Util.validarUUID(uuid))) {
			adicionarExcecaoUUID(mapServidor);
			return mapServidor;
		}
		
		mapServidor.put(Util.bundleMensagens.getString("codigo.status"), 
				Util.bundleConfig.getString("codigo.status.ok"));
		
		for (Servidor servidor : listaServidor) {
			if(servidor.getUuid().equals(uuid)) {
				mapServidor.put(Util.bundleMensagens.getString("key.consumo.horas"), Util.formatarDecimais(servidor.getHorasConsumidas()));
				mapServidor.put(Util.bundleMensagens.getString("key.valor.cobranca"), Util.formatarDecimais(servidor.getTotalConsumido()));
				
				return mapServidor;
			}
		}
		
		mapServidor.put(Util.bundleMensagens.getString("mensagem.padrao"), 
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
	@RequestMapping(caminhoCalcularConsumoTodosServidores)
	public Map<String, Object> calcularConsumoTodosServidores() {
		Map<String, Object> mapServidor = new LinkedHashMap<String, Object>();
		
		if(CollectionUtils.isEmpty(listaServidor)) {
			mapServidor.put(Util.bundleMensagens.getString("codigo.status"), 
					Util.bundleConfig.getString("codigo.status.ok"));
			mapServidor.put(Util.bundleMensagens.getString("mensagem.padrao"), 
					Util.bundleMensagens.getString("mensagem.servidores.inexistentes"));
			
			return mapServidor;
		}
		
		Double valorTotalHorasConsumo = 0.0;
		Double valorTotalCobrar = 0.0;
		
		for (Servidor servidor : listaServidor) {
			valorTotalHorasConsumo += servidor.getHorasConsumidas();
			valorTotalCobrar += servidor.getTotalConsumido();
		}
		mapServidor.put(Util.bundleMensagens.getString("codigo.status"), 
				Util.bundleConfig.getString("codigo.status.ok"));
		mapServidor.put(Util.bundleMensagens.getString("key.consumo.horas"), Util.formatarDecimais(valorTotalHorasConsumo));
		mapServidor.put(Util.bundleMensagens.getString("key.valor.cobranca"), Util.formatarDecimais(valorTotalCobrar));
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
	
	/**
	 * Método responsável por adicionar a mensagem de excecao para um numero invalido 
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 20:06
	 * @param listaMensagensRetorno : {@link Map}<{@link String}, {@link Object}>
	 */
	private void adicionarExcecaoHorasConsumidas(Map<String, Object> listaMensagensRetorno) {
		listaMensagensRetorno.put(Util.bundleMensagens.getString("codigo.status"), 
				Util.bundleConfig.getString("codigo.status.erro.cliente"));
		listaMensagensRetorno.put( Util.bundleMensagens.getString("mensagem.excecao"), 
				Util.bundleMensagens.getString("mensagem.formato.numerico.incorreto"));
	}
	
	/**
	 * Método responsável por adicionar a mensagem de excecao para um UUID invalido
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 20:07
	 * @param listaMensagensRetorno : {@link Map}<{@link String}, {@link Object}>
	 */
	private void adicionarExcecaoUUID(Map<String, Object> listaMensagensRetorno) {
		listaMensagensRetorno.put(Util.bundleMensagens.getString("codigo.status"), 
				Util.bundleConfig.getString("codigo.status.erro.cliente"));
		listaMensagensRetorno.put( Util.bundleMensagens.getString("mensagem.excecao"), 
				Util.bundleMensagens.getString("mensagem.uuid.invalido"));
	}
}
