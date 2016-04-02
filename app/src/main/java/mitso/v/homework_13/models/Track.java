package mitso.v.homework_13.models;

public class Track implements Comparable<Track> {

    private long id;
    private String artist;
    private String song;
    private int duration;

    public Track(long id, String artist, String song, int duration) {
        this.id = id;
        this.artist = artist;
        this.song = song;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(Track another) {

        if (this.getArtist().compareTo(another.getArtist()) > 1)
            return 1;
        else if (this.getArtist().compareTo(another.getArtist()) < -1)
            return -1;
        else {
            if (this.getSong().compareTo(another.getSong()) > 1)
                return 1;
            else if (this.getSong().compareTo(another.getSong()) < -1)
                return -1;
            else
                return 0;
        }
    }
}
