package dtos;

import entities.Prop;
import entities.Talk;

import java.util.ArrayList;
import java.util.List;

public class TalkDTO {
    private Integer id;
    private String topic;
    private String duration;
    private List<Prop> props;
    private List<SpeakerDTO> speakers;

    public TalkDTO(Talk talk) {
        this.id = talk.getId();
        this.topic = talk.getTopic();
        this.duration = talk.getDuration();
        this.speakers = SpeakerDTO.getDTOs(talk.getSpeakers());
    }

    public static List<TalkDTO> getDTOs(List<Talk> entities) {
        List<TalkDTO> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(new TalkDTO(e)));
        return dtos;
    }
}
