package it.exprivia.service;

import io.smallrye.mutiny.Uni;
import it.exprivia.models.dtos.PagedResponse;
import it.exprivia.models.dtos.user.UserDTO;

public interface IUser {
    public Uni<UserDTO> getUserById(String codiceFiscale);
    public Uni<PagedResponse<UserDTO>> getAllUsers(int page, int size);
    public Uni<UserDTO> createUser(UserDTO newUser);

}
