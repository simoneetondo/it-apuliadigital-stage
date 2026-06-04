package it.exprivia.Scuola.services;

import it.exprivia.Scuola.models.dto.LoginRequest;
import it.exprivia.Scuola.models.dto.LoginResponse;

public interface ILogin {


    public LoginResponse login(LoginRequest loginRequest);
}
