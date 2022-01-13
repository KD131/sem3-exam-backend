package dtos;

import entities.Conference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConferenceDTO {

    private Integer id;
    private String name;
    private String location;
    private int capacity;
    private LocalDate date;
    private LocalTime time;
    private List<TalkDTO> talks;

    public ConferenceDTO(Conference conference) {
        this.id = conference.getId();
        this.name = conference.getName();
        this.location = conference.getLocation();
        this.capacity = conference.getCapacity();
        this.date = conference.getDate();
        this.time = conference.getTime();
        this.talks = TalkDTO.getDTOs(conference.getTalks());
    }

    public static List<ConferenceDTO> getDTOs(List<Conference> entities) {
        List<ConferenceDTO> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(new ConferenceDTO(e)));
        return dtos;
    }
}
