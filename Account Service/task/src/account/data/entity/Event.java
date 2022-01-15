package account.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "action")
    private String action;

    @Column(name = "subject")
    private String subject;

    @Column(name = "object")
    private String object;

    @Column(name = "path")
    private String path;
}
