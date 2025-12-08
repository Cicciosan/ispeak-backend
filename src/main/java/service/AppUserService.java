package service;

import entity.AppUserEntity;
import exception.UserNotRegisteredException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AppUserService {

    @Inject
    JsonWebToken jwt;

    public AppUserEntity getMySelf() throws UserNotRegisteredException {
        final var optionalMe = AppUserEntity.findByIdOptional(jwt.getSubject());
        if (optionalMe.isEmpty())
            throw new UserNotRegisteredException();
        return (AppUserEntity) optionalMe.get();
    }
}
