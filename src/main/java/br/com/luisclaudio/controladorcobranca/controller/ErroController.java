package br.com.luisclaudio.controladorcobranca.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.luisclaudio.controladorcobranca.util.Util;

@Controller
public class ErroController implements ErrorController {
	
	@RequestMapping("/error")
	@ResponseBody
	public String tratarErro(HttpServletRequest request) {
		Integer codigoStatus = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		sb.append("<body>");
		sb.append("<h2>");
		sb.append(Util.bundleMensagens.getString("pagina.erro"));
		sb.append("</h2>");
		sb.append("<div>");
		sb.append(Util.bundleMensagens.getString("codigo.status"));
		sb.append("<b>");
		sb.append("&nbsp;");
		sb.append(codigoStatus);
		sb.append("</b>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append(Util.bundleMensagens.getString("mensagem.excecao"));
		sb.append("<b>");
		sb.append("&nbsp;");
		sb.append(exception==null? "N/A": exception.getMessage());
		sb.append("</b>");
		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");
		
		return sb.toString();
	}
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
}