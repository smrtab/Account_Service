package account.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "role")
public class Role {

    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String ACCOUNTANT = "ACCOUNTANT";
    public static final String AUDITOR = "AUDITOR";
    public static final String USER = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public boolean isAdmin() {
        return getName().equals(ADMINISTRATOR);
    }
    public boolean isBusiness() {
        return List.of(ACCOUNTANT, AUDITOR, USER).contains(getName());
    }
}