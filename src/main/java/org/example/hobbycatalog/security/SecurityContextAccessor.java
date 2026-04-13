package org.example.hobbycatalog.security;

import org.example.hobbycatalog.entity.UsersInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class SecurityContextAccessor {

    public Long getCurrentUserId() throws AccessDeniedException {
        Object principal = checkAuthorisation();

        if(principal instanceof UsersInfo){
            return ((UsersInfo) principal).getIdUser();
        }
        throw new AccessDeniedException("Unexpected principal type: " +principal.getClass().getName());
    }

    public String getCurrentUSerEmail() throws AccessDeniedException {
        Object principal = checkAuthorisation();
        if(principal instanceof UsersInfo){
            return ((UsersInfo) principal).getEmail();
        }
        throw new AccessDeniedException("Unexpected principal type: " +principal.getClass().getName());
    }

    public UsersInfo getCurrentUser() throws AccessDeniedException {
        Object principal = checkAuthorisation();
        if(principal instanceof UsersInfo){
            return ((UsersInfo) principal);
        }
        throw new AccessDeniedException("Unexpected principal type: " +principal.getClass().getName());
    }
    private Object checkAuthorisation() throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null && !auth.isAuthenticated()){
            throw new AccessDeniedException("Authenticate first");
        }
        return auth.getPrincipal();
    }
}
