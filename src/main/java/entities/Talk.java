package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "talks")
public class Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "duration", nullable = false)
    private String duration;

    @OneToMany(mappedBy = "talk")
    private List<Prop> props;

    @ManyToMany(mappedBy = "talks",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "speakers_talks",
            joinColumns = @JoinColumn(name = "talk", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "speaker", referencedColumnName = "id"))
    private List<Speaker> speakers;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(name = "conference", nullable = true)
    private Conference conference;

    public Talk() {
    }

    public Talk(String topic, String duration, Conference conference) {
        this.speakers = new ArrayList<>();
        this.props = new ArrayList<>();
        this.topic = topic;
        this.duration = duration;
        conference.addTalk(this);
    }

    public Integer getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getDuration() {
        return duration;
    }

    public List<Prop> getProps() {
        return props;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public void addSpeaker(Speaker speaker) {
        if (speaker != null) {
            this.speakers.add(speaker);
            speaker.getTalks().add(this);
        }
    }

    public void removeSpeaker(Speaker speaker) {
        if (speaker != null) {
            this.speakers.remove(speaker);
            speaker.getTalks().remove(this);
        }
    }

    public void removeAllSpeakers() {
        Iterator<Speaker> iterator = speakers.iterator();
        while (iterator.hasNext()) {
            Speaker speaker = iterator.next();
            if (speaker != null) {
                iterator.remove();
                speaker.getTalks().remove(this);
            }
        }
    }
}
