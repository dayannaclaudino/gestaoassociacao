package com.api.gestaoassociacao.controller.Exception;

public class NegocioException extends RuntimeException{
    private static final long serialVersionUID = 1L;

	public NegocioException(String mensagem){
		super(mensagem);
	}
	
}
