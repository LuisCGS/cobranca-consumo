package br.com.luisclaudio.controladorcobranca;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.junit.Test;

import br.com.luisclaudio.controladorcobranca.controller.JSONController;
import br.com.luisclaudio.controladorcobranca.util.Util;

public class ServidorControllerTeste {

	JSONController servidorController = new JSONController();
	private static final ResourceBundle bundle = ResourceBundle.getBundle("mensagens");
	private static final ResourceBundle bundleConfig = ResourceBundle.getBundle("configuracoes");
	private static final Double valorUnitario = new Double(Util.bundleConfig.getString("valor.unitario"));
	
	@Test
	public void testIniciarUtilizacaoServidor() {
		assertTrue(servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440000", "10") instanceof Map);
	}
	
	@Test
	public void testIniciarUtilizacaoServidorValorComVirgula() {
		assertTrue(servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440000", "10,9") instanceof Map);
	}
	
	@Test
	public void testIniciarUtilizacaoServidorUUIDException() {
		Map<String, Object> mapRetorno = servidorController.iniciarUtilizacaoServidor("uuidIncorreto", "10");
		assertTrue(mapRetorno.get(bundle.getString("codigo.status")).equals(bundleConfig.getString("codigo.status.erro.cliente")));
	}
	
	@Test
	public void testIniciarUtilizacaoServidorHorasConsumidasException() {
		Map<String, Object> mapRetorno = servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440000", "naoNumerico");
		assertTrue(mapRetorno.get(bundle.getString("codigo.status")).equals(bundleConfig.getString("codigo.status.erro.cliente")));
	}
	
	@Test
	public void testRetornoValidoConsumoServidor() {
		assertTrue(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440000") instanceof Map);
		assertEquals(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440999")
				.get(bundle.getString("codigo.status")), bundleConfig.getString("codigo.status.ok"));
	}
	
	@Test
	public void testCalcularConsumoServidorInexistente() {
		Map<String, Object> mapServidores = servidorController.calcularConsumoServidor(UUID.randomUUID().toString());
		
		assertEquals(mapServidores.get(bundle.getString("codigo.status")), bundleConfig.getString("codigo.status.ok"));
		assertEquals(mapServidores.get(bundle.getString("mensagem.padrao")), bundle.getString("mensagem.servidor.nao.encontrado"));
	}
	
	@Test
	public void testCalcularConsumoServidorNovo() {
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440001", "10.5");
		Map<String, Object> mapServidores = servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440001");
		
		assertEquals(mapServidores.get(bundle.getString("codigo.status")), bundleConfig.getString("codigo.status.ok"));
		assertEquals(mapServidores.get(bundle.getString("key.consumo.horas")), 10.5);
		assertEquals(mapServidores.get(bundle.getString("key.valor.cobranca")), 10.5 * valorUnitario);
	}
	
	@Test
	public void testCalcularConsumoServidorRepetido() {
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440002", "10.5");
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440002", "37.1");
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440002", "8590.99");
		
		// 10.5 + 37.1 . 8590.99 = 8638.59
		Map<String, Object> mapServidores = servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440002");
		
		assertEquals(mapServidores.get(bundle.getString("key.consumo.horas")), 8638.59);
		assertEquals(mapServidores.get(bundle.getString("key.valor.cobranca")), 8638.59 * valorUnitario);
	}
	
	@Test
	public void testRetornoValidoCalcularConsumoTodosServidores() {
		assertTrue(servidorController.calcularConsumoTodosServidores() instanceof Map);
		assertEquals(servidorController.calcularConsumoTodosServidores()
				.get(bundle.getString("codigo.status")), bundleConfig.getString("codigo.status.ok"));
	}
	
	@Test
	public void testCalcularConsumoTodosServidores() {
		Double valorTotalHoras = new Double(0.0);
		Double valorTotalCobranca = new Double(0.0);
		
		for(int i = 0; i < 10; i++) {
			servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-42665544000" + i, String.valueOf(i * 5));
			valorTotalHoras += (Double) servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-42665544000" + i)
					.get(bundle.getString("key.consumo.horas"));
			
			valorTotalCobranca += (Double) servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-42665544000" + i)
					.get(bundle.getString("key.valor.cobranca"));
		}
		
		assertEquals(servidorController.calcularConsumoTodosServidores()
				.get(bundle.getString("key.consumo.horas")), valorTotalHoras);
		assertEquals(servidorController.calcularConsumoTodosServidores()
				.get(bundle.getString("key.valor.cobranca")), valorTotalCobranca);
	}
}
