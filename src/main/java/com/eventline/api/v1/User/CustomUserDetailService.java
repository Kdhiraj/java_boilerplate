package com.eventline.api.v1.User;

import com.eventline.api.v1.User.DAO.UserDao;
import com.eventline.api.v1.User.model.User;
import com.eventline.shared.constants.Messages;
import com.eventline.shared.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public CustomUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException, UnauthorizedException {
        User user = userDao.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        if (!user.isEnabled()) throw new UnauthorizedException(Messages.BLOCKED);
        return user;
    }


    public User loadUserById(String userId) throws UsernameNotFoundException, UnauthorizedException {
        User user = userDao.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        if (!user.isEnabled()) {
            throw new UnauthorizedException(Messages.BLOCKED);
        }
        return user;
    }


}
