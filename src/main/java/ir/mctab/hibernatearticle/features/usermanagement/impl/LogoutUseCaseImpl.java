package ir.mctab.hibernatearticle.features.usermanagement.impl;

import ir.mctab.hibernatearticle.core.share.AuthenticationService;
import ir.mctab.hibernatearticle.features.usermanagement.usecases.LogoutUseCase;

public class LogoutUseCaseImpl implements LogoutUseCase {
    @Override
    public void logout() {
        AuthenticationService.getInstance().setLoginUser(null);
    }
}
