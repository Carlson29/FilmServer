package business;

import java.util.*;

public class FilmManager {
    HashMap<String, Film> films;

    public FilmManager() {
        films = new HashMap<>();
    }

    public boolean add(Film f) {
        if (!films.containsKey(f.getTitle())) {
            films.put(f.getTitle(), f);
            return true;
        }
        return false;
    }

    public boolean remove(String title) {
        if (films.containsKey(title)) {
            films.remove(title);
            return true;
        }
        return false;
    }

    public boolean rateFilm(String title) {
        if (!films.containsKey(title)) {
            throw new IllegalArgumentException("no film available with the specified title");
        }
        if (films.containsKey(title)) {
            Film film = films.get(title);
            double ratings = film.getTotalRatings();
            film.setNumberOfRatings(film.getNumberOfRatings() + 1);
            film.setTotalRatings(ratings / film.getNumberOfRatings());
            films.put(title, film);
            return true;
        }
        return false;
    }

    public Film searchByTitle(String title) {
        Film f = null;
        f = films.get(title);
        return f;
    }

    public ArrayList<Film> searchByGenre(String genre) {
        ArrayList<Film> filmList = new ArrayList<>();
        for (Map.Entry<String, Film> film : films.entrySet()) {
            if (film.getValue().getGenre().equalsIgnoreCase(genre)) {
                filmList.add(film.getValue());
            }
        }
        Collections.sort(filmList, new FilmComparator());
        return filmList;
    }

}
