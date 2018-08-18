package br.com.luisclaudio.controladorcobranca.util;

import java.util.ResourceBundle;
import java.util.UUID;

public final class Util {
	
	public static final ResourceBundle bundle = ResourceBundle.getBundle("mensagens");
	
	/**
	 * Metodo responsavel por validar o atributo identificador do servidor {@link UUID}
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 14:40
	 * @param uuid : {@link String}
	 * @return {@link Boolean}
	 */
	public static Boolean validarUUID(String uuid) {
		try {
			UUID.fromString(uuid);
			return true;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(bundle.getString("mensagem.uuid.invalido"), e.getCause());
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Método responsável por validar a conversão de uma {@link String} para {@link Double} 
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 15:22
	 * @param numero : {@link String}
	 * @return {@link Boolean}
	 */
	public static Boolean validarConversaoDouble(String numero) {
		try {
			Double.parseDouble(numero);
			return true;
		} catch (NumberFormatException e) {
			throw new NumberFormatException(bundle.getString("mensagem.formato.numerico.incorreto"));
		} catch (Exception e) {
			throw e;
		}
	}
}
