package com.dev.objects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "posts")
public class PostObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private Date date;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn (name = "author_id")
    private UserObject userObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }
}
