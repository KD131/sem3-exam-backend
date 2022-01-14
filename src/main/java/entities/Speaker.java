package entities;

import dtos.SpeakerDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "speakers")
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profession", nullable = false)
    private String profession;

    @Column(name = "gender", nullable = false)
    private String gender;

    @ManyToMany
    private List<Talk> talks;

    public Speaker() {
    }

    public Speaker(SpeakerDTO dto) {
        this.name = dto.getName();
        this.profession = dto.getProfession();
        this.gender = dto.getGender();
        this.talks = new ArrayList<>();
    }

    public Speaker(String name, String profession, String gender) {
        this.name = name;
        this.profession = profession;
        this.gender = gender;
        this.talks = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public String getGender() {
        return gender;
    }

    public List<Talk> getTalks() {
        return talks;
    }
}
