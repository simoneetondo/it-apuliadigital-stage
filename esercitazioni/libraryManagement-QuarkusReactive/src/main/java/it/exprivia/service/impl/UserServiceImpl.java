package it.exprivia.service.impl;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import it.exprivia.exception.user.DuplicateUserException;
import it.exprivia.exception.user.UserNotFoundException;
import it.exprivia.mapper.UserMapper;
import it.exprivia.models.dtos.PagedResponse;
import it.exprivia.models.dtos.user.UserDTO;
import it.exprivia.models.entity.User;
import it.exprivia.repository.UserRepository;
import it.exprivia.service.IUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements IUser {

    @Inject
    UserRepository repo;

    @Inject
    UserMapper mapper;


    @WithSession
    @Override
    public Uni<UserDTO> getUserById(String codiceFiscale) {
        return repo.findById(codiceFiscale)
                .onItem().ifNull().failWith(() -> new UserNotFoundException(codiceFiscale))
                .map(user -> mapper.toDTO(user));
    }

    @WithSession
    @Override
    public Uni<PagedResponse<UserDTO>> getAllUsers(int page, int size) {
        PanacheQuery<User> query = repo.findAll().page(page, size);
        return query.list()
                .chain(users ->
                        query.count()
                                .map(total ->
                                        new PagedResponse<>(
                                                mapper.toDTOList(users),
                                                total,
                                                total / size,
                                                page,
                                                size
                                        )
                                )
                );

    }

    @WithTransaction
    @Override
    public Uni<UserDTO> createUser(UserDTO newUser) {
        return repo.findById(newUser.codiceFiscale())
                .onItem().ifNotNull().failWith(() -> new DuplicateUserException(newUser.codiceFiscale()))
                .chain(existingUser -> {
                    User userToSave = mapper.toEntity(newUser);
                    return repo.persist(userToSave)
                            .map(user -> mapper.toDTO(user));

                });
    }

}