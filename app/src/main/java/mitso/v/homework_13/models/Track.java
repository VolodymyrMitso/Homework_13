package mitso.v.homework_13.models;

public class Track {

    String Artist;
    String Song;
    String Duration;

    public Track(String artist, String song, String duration) {
        Artist = artist;
        Song = song;
        Duration = duration;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getSong() {
        return Song;
    }

    public void setSong(String song) {
        Song = song;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}
