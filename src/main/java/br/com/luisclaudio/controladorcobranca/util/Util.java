package br.com.luisclaudio.controladorcobranca.util;

import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.UUID;

public final class Util {
	
	public static final ResourceBundle bundleMensagens = ResourceBundle.getBundle("mensagens");
	public static final ResourceBundle bundleConfig = ResourceBundle.getBundle("configuracoes");
	
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
			throw new IllegalArgumentException(bundleMensagens.getString("mensagem.uuid.invalido"), e.getCause());
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
			throw new NumberFormatException(bundleMensagens.getString("mensagem.formato.numerico.incorreto"));
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Método responsável por formatar apenas 2 casas decimais do {@link Double} 
	 *
	 * @author Luis Claudio Goncalves Sanches
	 * @since 18/08/2018 - 18:46
	 * @param numero : {@link Double}
	 * @return {@link Double}
	 */
	public static Double formatarDecimais(Double numero) {
		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		String numeroFormatado = decimalFormat.format(numero).replace(",", ".");
		
		return new Double(numeroFormatado);
	}
}
