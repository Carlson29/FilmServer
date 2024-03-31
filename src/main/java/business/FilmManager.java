package business;

import java.util.*;

public class FilmManager {
    HashMap<String, Film> films;

    public FilmManager() {
        films = new HashMap<>();
        bootstrapUserList();
    }
    private void bootstrapUserList()
    {
        Film f1 = new Film("aaa", "fiction", 20, 4);
        Film f2 = new Film("bbb", "horror", 5, 4);
        Film f3 = new Film("ccc", "fiction", 5, 4);
        Film f4 = new Film("zzz", "fiction", 5, 4);
        films.put(f3.getTitle(), f3);
        films.put(f1.getTitle(), f1);
        films.put(f2.getTitle(), f2);
        films.put(f4.getTitle(), f4);

    }

    /**
     * Adds a new film to the hashmap
     *
     * @param f a Film
     * @return true if the film was added or false if there's a film with the same title in the hashMap
     * @throws NullPointerException if the film is null
     **/

    public boolean add(Film f) {
        if (f == null) {
            throw new NullPointerException("film can't be null");
        }
        if (!films.containsKey(f.getTitle())) {
            films.put(f.getTitle(), f);
            return true;
        }
        return false;
    }

    /**
     * removes a film from the hashMap
     *
     * @param title, the title of the film
     * @return true if the film exist, and it was removed successfully or false if there's no film with the title
     **/
    public boolean remove(String title) {
        if (films.containsKey(title)) {
            films.remove(title);
            return true;
        }
        return false;
    }

    /**
     * Rates a films
     *
     * @param title,  the title of the film
     * @param rating, the rating
     * @return true if the film exist ,and it was successfully rated or false if the film doesn't exist
     **/
    public boolean rateFilm(String title, double rating) {
        if (films.containsKey(title)) {
            Film film = films.get(title);
            double ratings = film.getTotalRatings() + rating;
            film.setNumberOfRatings(film.getNumberOfRatings() + 1);
            film.setTotalRatings(ratings / film.getNumberOfRatings());
            films.put(title, film);
            return true;
        }
        return false;
    }

    /**
     * Searches for a film based on it's title
     *
     * @param title the films title
     * @return a film if it was found or null if no film was found
     **/
    public Film searchByTitle(String title) {
        Film f = null;
        f = films.get(title);
        return f;
    }

    /**
     * Searches for all films that matches a particular genre
     *
     * @param genre the genre to search for
     * @return an arraylist of films sorted in ascending order by the film titles
     **/
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
