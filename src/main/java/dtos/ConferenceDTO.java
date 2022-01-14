package dtos;

import entities.Conference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConferenceDTO {

    private Integer id;
    private String name;
    private String location;
    private int capacity;
    private String date;
    private String time;
    private List<TalkDTO> talks;

    public ConferenceDTO(Conference conference) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        this.id = conference.getId();
        this.name = conference.getName();
        this.location = conference.getLocation();
        this.capacity = conference.getCapacity();
        this.date = conference.getDate().format(dateFormatter);
        this.time = conference.getTime().format(timeFormatter);
        this.talks = TalkDTO.getDTOs(conference.getTalks());
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public List<TalkDTO> getTalks() {
        return talks;
    }

    public static List<ConferenceDTO> getDTOs(List<Conference> entities) {
        List<ConferenceDTO> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(new ConferenceDTO(e)));
        return dtos;
    }
}
