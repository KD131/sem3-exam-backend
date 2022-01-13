package entities;

import javax.persistence.*;

@Entity
@Table(name = "props")
public class Prop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "talk", nullable = false)
    private Talk talk;

}
