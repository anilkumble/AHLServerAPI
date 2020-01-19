package com.ahl.server.entity;

import com.ahl.server.enums.TournamentConfig;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.StringJoiner;

@Document(collection = "tournaments")
public class Tournament {

    @Id
    private ObjectId id;

    @NotEmpty
    private String season;

    @NotEmpty
    private String theme;

    @NotEmpty
    private String tagLine;

    @NotEmpty
    private String tournamentName;

    private TournamentConfig tournamentType;

    private String tournamentLogo;

    private boolean isLive = true;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentLogo() {
        return tournamentLogo;
    }

    public void setTournamentLogo(String tournamentLogo) {
        this.tournamentLogo = tournamentLogo;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public TournamentConfig getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(TournamentConfig tournamentType) {
        this.tournamentType = tournamentType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Tournament.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("season='" + season + "'")
                .add("theme='" + theme + "'")
                .add("tagLine='" + tagLine + "'")
                .add("tournamentName='" + tournamentName + "'")
                .add("tournamentLogo='" + tournamentLogo + "'")
                .add("isLive=" + isLive)
                .toString();
    }
}
