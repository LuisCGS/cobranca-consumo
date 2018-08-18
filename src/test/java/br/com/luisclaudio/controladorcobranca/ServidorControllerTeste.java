package br.com.luisclaudio.controladorcobranca;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import br.com.luisclaudio.controladorcobranca.controller.ServidorController;
import br.com.luisclaudio.controladorcobranca.util.Util;

public class ServidorControllerTeste {

	ServidorController servidorController = new ServidorController();
	private static final ResourceBundle bundle = ResourceBundle.getBundle("mensagens");
	private static final Double valorUnitario = new Double(Util.bundleConfig.getString("valor.unitario"));
	
	@Test
	public void testIniciarUtilizacaoServidor() {
		assertTrue(servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440000", "10") instanceof List);
	}
	
	@Test
	public void testIniciarUtilizacaoServidorValorComVirgula() {
		assertTrue(servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440000", "10,9") instanceof List);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIniciarUtilizacaoServidorUUIDException() {
		servidorController.iniciarUtilizacaoServidor("uuidIncorreto", "10");
	}
	
	@Test(expected=NumberFormatException.class)
	public void testIniciarUtilizacaoServidorHorasConsumidasException() {
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440000", "naoNumerico");
	}
	
	@Test
	public void testCalcularConsumoServidorInexistente() {
		assertTrue(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440000") instanceof Map);
		assertEquals(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440999")
				.get(bundle.getString("key.consumo.horas")), bundle.getString("mensagem.servidor.nao.encontrado"));
	}
	
	@Test
	public void testCalcularConsumoServidorNovo() {
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440001", "10.5");
		assertEquals(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440001")
				.get(bundle.getString("key.consumo.horas")), 10.5);
		assertEquals(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440001")
				.get(bundle.getString("key.valor.cobranca")), 10.5 * valorUnitario);
	}
	
	@Test
	public void testCalcularConsumoServidorRepetido() {
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440002", "10.5");
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440002", "37.1");
		servidorController.iniciarUtilizacaoServidor("123e4567-e89b-12d3-a456-426655440002", "8590.99");
		
		// 10.5 + 37.1 . 8590.99 = 8638.59
		
		assertEquals(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440002")
				.get(bundle.getString("key.consumo.horas")), 8638.59);
		assertEquals(servidorController.calcularConsumoServidor("123e4567-e89b-12d3-a456-426655440002")
				.get(bundle.getString("key.valor.cobranca")), 8638.59 * valorUnitario);
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
