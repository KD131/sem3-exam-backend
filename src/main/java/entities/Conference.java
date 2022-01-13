package entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conference")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @OneToMany(mappedBy = "conference",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private List<Talk> talks;

    public Conference() {
    }

    public Conference(String name, String location, int capacity, LocalDate date, LocalTime time) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.date = date;
        this.time = time;
        this.talks = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public List<Talk> getTalks() {
        return talks;
    }

    public void addTalk(Talk talk) {
        if (talk != null) {
            this.talks.add(talk);
            talk.setConference(this);
        }
    }

    public void removeTalk(Talk talk) {
        if (talk != null) {
            this.talks.remove(talk);
            talk.setConference(null);
        }
    }
}
