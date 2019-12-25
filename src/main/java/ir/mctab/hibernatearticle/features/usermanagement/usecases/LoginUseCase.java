package ir.mctab.hibernatearticle.features.usermanagement.usecases;

import ir.mctab.hibernatearticle.entities.User;

public interface LoginUseCase {
    User login(String username, String password);
}
