package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.PostObject;
import com.dev.objects.UserObject;
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
    private  List<UserObject> userObjects;


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {
        userObjects = new ArrayList<>();
        UserObject userObject = new UserObject();
        userObject.setUsername("Shai");
        userObject.setPassword("1234");
        userObject.setToken(createHash(userObject.getUsername(), userObject.getPassword()));
        this.userObjects.add(userObject);

    }



    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test () {
        return "Adi";
    }

    @RequestMapping(value = "/get-random-value", method = {RequestMethod.GET, RequestMethod.POST})
    public int random () {
        Random random = new Random();
        return random.nextInt();
    }

    @RequestMapping(value = "/get-post", method = {RequestMethod.GET, RequestMethod.POST})
    public PostObject getPost () {
        PostObject postObject = new PostObject();
        postObject.setSenderName("Shai Givati");
        postObject.setContent("This is my first post.");
        postObject.setDate("01-01-2021 10:04:05");
        return postObject;
    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = persist.doesUserExists(username, password);
        return token;
    }

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        boolean alreadyExists = false;
        for (UserObject userObject : this.userObjects) {
            if (userObject.getUsername().equals(username)) {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists) {
            UserObject userObject = new UserObject();
            userObject.setUsername(username);
            userObject.setPassword(password);
            String hash = createHash(username, password);
            userObject.setToken(hash);
            this.userObjects.add(userObject);
            success = true;
        }

        return success;
    }

    public String createHash (String username, String password) {
        String myHash = null;
        try {
            String hash = "35454B055CC325EA1AF2126E27707052";

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((username + password).getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return myHash;
    }

    @RequestMapping("add-post")
    public boolean addPost (String token, String content) {
        boolean success = false;
        UserObject userObject = getUserByToken(token);
        if (userObject != null) {
            success = true;
            userObject.addPost(content);
        }
        return success;
    }

    private UserObject getUserByToken (String token) {
        UserObject found = null;
        for (UserObject userObject : this.userObjects) {
            if (userObject.getToken().equals(token)) {
                found = userObject;
                break;
            }
        }
        return found;
    }

    @RequestMapping("get-posts")
    public List<PostObject> getPosts (String token) {
        UserObject userObject = getUserByToken(token);
        return userObject.getPosts();
    }






}
