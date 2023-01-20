package com.cydeo.service.impl;

import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {  // we need a constructor because we will return a Use object
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //get user, that we want to auth from DB and convert to user spring user,
        //we need UserPrinciple(mapper)

        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        if(user==null){
             throw new UsernameNotFoundException(username); //Spring requires this check
         }

        return new UserPrincipal(user);
    }
}
