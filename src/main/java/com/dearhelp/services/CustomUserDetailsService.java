package com.dearhelp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dearhelp.models.DearHelpUsers;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        DearHelpUsers user =
                mongoTemplate.findOne(query, DearHelpUsers.class);
        System.out.println(user);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Email %s not found", email));
        }

        

        return new User(user.getEmail(), user.getPassword(),
        		AuthorityUtils.createAuthorityList(user.getRole()));
    }
}                         
                       