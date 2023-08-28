package com.notifier.app.db;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity 
@Table(name = "usuarios") 
public class User {

    @Id
    @Column(name = "id")
    private String id; 

    @Column(name = "time") 
    private String time;


    public String getTime() {
        return time;
    }


    public String getId() {
        return id;
    }


    public void setId(String userid) {
        this.id = userid;
    }


    public void setTime(String time) {
        this.time = time;
    }

}
