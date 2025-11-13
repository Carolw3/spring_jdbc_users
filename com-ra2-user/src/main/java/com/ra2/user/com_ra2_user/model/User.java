package com.ra2.user.com_ra2_user.model;

import java.time.LocalDateTime;

public class User {
    private long id;
    private String name;
    private String descripcion;
    private String email;
    private String password;
    private String imageP;
    private LocalDateTime ultimAcces;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;

    

    public User(long id, String name, String descripcion, String email, String password, String imageP, LocalDateTime ultimAcces,
            LocalDateTime dataCreated, LocalDateTime dataUpdated) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.email = email;
        this.password = password;
        this.imageP = imageP;
        this.ultimAcces = ultimAcces;
        this.dataCreated = dataCreated;
        this.dataUpdated = dataUpdated;
    }

    public User(String name, String descripcion, String email, String password) {
        this.name = name;
        this.descripcion = descripcion;
        this.email = email;
        this.password = password;
        this.ultimAcces = null;
        this.dataCreated = null;
        this.dataUpdated = null;
    }
    
    public User() {
    }


    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getImage() {
        return imageP;
    }
    public void setImage(String imageP) {
        this.imageP = imageP;
    }
    public LocalDateTime getUltimAcces() {
        return ultimAcces;
    }
    public void setUltimAcces(LocalDateTime ultimAcces) {
        this.ultimAcces = ultimAcces;
    }
    public LocalDateTime getDataCreated() {
        return dataCreated;
    }
    public void setDataCreated(LocalDateTime dataCreated) {
        this.dataCreated = dataCreated;
    }
    public LocalDateTime getDataUpdated() {
        return dataUpdated;
    }
    public void setDataUpdated(LocalDateTime dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "User [id= " + id + ", name= " + name + ", descripcion= " + descripcion + ", email= " + email + ", password= "
                + password + ", image_path= " + imageP + ", ultimAcces= " + ultimAcces + ", dataCreated= " + dataCreated + ", dataUpdated= "
                + dataUpdated + "]";
    }

    
    
}

