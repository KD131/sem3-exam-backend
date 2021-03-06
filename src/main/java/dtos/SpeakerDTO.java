package dtos;

import entities.Speaker;

import java.util.ArrayList;
import java.util.List;

public class SpeakerDTO {
    private Integer id;
    private String name;
    private String profession;
    private String gender;

    public SpeakerDTO(String name, String profession, String gender) {
        this.name = name;
        this.profession = profession;
        this.gender = gender;
    }

    public SpeakerDTO(Speaker speaker) {
        this.id = speaker.getId();
        this.name = speaker.getName();
        this.profession = speaker.getProfession();
        this.gender = speaker.getGender();
    }

    public static List<SpeakerDTO> getDTOs(List<Speaker> entities) {
        List<SpeakerDTO> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(new SpeakerDTO(e)));
        return dtos;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
