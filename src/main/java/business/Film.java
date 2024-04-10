package business;

import java.util.Objects;

public class Film implements Comparable<Film> {
    private String title;
    private String genre;
    private double totalRatings;
    private int numberOfRaters;

    public Film() {
    }

    public Film(String title, String genre) {
        this.title = title;
        this.genre = genre;
        this.totalRatings = 0;
        this.numberOfRaters = 0;
    }

    public Film(String title, String genre, int totalRatings, int numberOfRaters) {
        this.title = title;
        this.genre = genre;
        this.totalRatings = totalRatings;
        this.numberOfRaters = numberOfRaters;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(double totalRatings) {
        this.totalRatings = totalRatings;
    }

    public int getNumberOfRaters() {
        return numberOfRaters;
    }

    public void setNumberOfRaters(int numberOfRaters) {
        this.numberOfRaters = numberOfRaters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(title, film.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", totalRatings=" + totalRatings +
                ", numberOfRatings=" + numberOfRaters +
                '}';
    }

    public String encode(String delimiter) {
        return this.title + delimiter + this.genre + delimiter + this.totalRatings + delimiter + this.numberOfRaters;
    }

    public static Film decode(String encoded, String delimiter) {
        String[] components = encoded.split(delimiter);
        Film f = null;
        if (components.length != 4) {
            return null;
        }
        try {
            f = new Film(components[0], components[1], Integer.parseInt(components[2]), Integer.parseInt(components[3]));
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
        return f;
    }

    @Override
    public int compareTo(Film f) {
        return title.compareTo(f.title);
    }
}
