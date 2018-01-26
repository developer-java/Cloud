package view.kz.persistence;

import view.kz.persistence.common.Identifier;
import view.kz.persistence.types.UserType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "SYSTEM_USERS", uniqueConstraints = @UniqueConstraint(columnNames={"login","uid"}))
public class SystemUser extends Identifier {
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Column(name = "LASTNAME")
    private String lastname;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "DIR")
    private String parrentDir;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private UserType type;
    @Column(name = "IS_TEST_USER")
    private boolean testUser;
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "CITY_ID")
    private City city;
    @Column(name = "UID")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isTestUser() {
        return testUser;
    }

    public void setTestUser(boolean testUser) {
        this.testUser = testUser;
    }

    public String getParrentDir() {
        return parrentDir;
    }

    public void setParrentDir(String parrentDir) {
        this.parrentDir = parrentDir;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
