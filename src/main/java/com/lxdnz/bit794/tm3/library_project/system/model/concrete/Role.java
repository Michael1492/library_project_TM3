package com.lxdnz.bit794.tm3.library_project.system.model.concrete;

import com.lxdnz.bit794.tm3.library_project.system.model.AbstractModelClass;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role extends AbstractModelClass {

    private String role;
    /*
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    // ~ defaults to @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "role_id"),
    //     inverseJoinColumns = @joinColumn(name = "user_id"))
    */
    @Transient
    private List<User> users = new ArrayList<>();

    /**
     * Default Role constructor
     */
    public Role() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        if(!this.users.contains(user)){
            this.users.add(user);
        }

        if(!user.getRoles().contains(this)){
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user){
        this.users.remove(user);
        user.getRoles().remove(this);
    }
}
