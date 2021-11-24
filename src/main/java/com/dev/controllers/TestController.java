package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.PostObject;
import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
public class TestController {


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {

    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = persist.getTokenByUsernameAndPassword(username, password);
        return token;
    }

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        boolean alreadyExists = persist.getTokenByUsernameAndPassword(username, password) != null;
        if (!alreadyExists) {
            UserObject userObject = new UserObject();
            userObject.setUsername(username);
            userObject.setPassword(password);
            String hash = Utils.createHash(username, password);
            userObject.setToken(hash);
            success = persist.addAccount(userObject);
        }

        return success;
    }


    @RequestMapping("add-post")
    public boolean addPost (String token, String content) {
        return persist.addPost(token, content);
    }


    @RequestMapping("get-posts")
    public List<PostObject> getPosts (String token) {
        return persist.getPostsByUser(token);
    }

    @RequestMapping("remove-post")
    public boolean removePost (String token, int postId) {
        return persist.removePost(token, postId);
    }







}
