package br.com.luisclaudio.controladorcobranca;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.junit.Test;

import br.com.luisclaudio.controladorcobranca.controller.ServidorController;

public class ServidorControllerTeste {

	ServidorController servidorController = new ServidorController();
	private static final ResourceBundle bundle = ResourceBundle.getBundle("mensagens");
	
	@Test
	public void testIniciarUtilizacaoServidor() {
		assertTrue(servidorController.iniciarUtilizacaoServidor("teste1", "10") instanceof List);
		
		UUID.randomUUID();
		UUID.fromString("teste2");
	}
	
	@Test(expected=NumberFormatException.class)
	public void testIniciarUtilizacaoServidorException() {
		servidorController.iniciarUtilizacaoServidor("teste", "naoNumerico");
	}
	
	@Test
	public void testCalcularConsumoServidor() {
		
		assertTrue(servidorController.calcularConsumoServidor("testeCalcularConsumo") instanceof Map);
		assertEquals(servidorController.calcularConsumoServidor("testeCalcularConsumo").get("consumoCalculado"), bundle.getString("mensagem.servidor.nao.encontrado"));
		
		servidorController.iniciarUtilizacaoServidor("testeCalcularConsumo", "10.5");
		assertEquals(servidorController.calcularConsumoServidor("testeCalcularConsumo").get("consumoCalculado"), bundle.getString("mensagem.servidor.nao.encontrado"));
		
	}

	@Test
	public void testCalcularConsumoTodosServidores() {
		fail("Not yet implemented");
	}

}
