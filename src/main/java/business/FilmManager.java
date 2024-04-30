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
        Film f1 = new Film("aaa", "fiction", 10, 4);
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
        synchronized (films) {
        if (!films.containsKey(f.getTitle())) {
            films.put(f.getTitle(), f);
            return true;
        }
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
        synchronized (films) {
            if (films.containsKey(title)) {
                Film film = films.get(title);
                double ratings = film.getTotalRatings() + rating;
                film.setNumberOfRaters(film.getNumberOfRaters() + 1);
                film.setTotalRatings(ratings / film.getNumberOfRaters());
                films.put(title, film);
                return true;
            }
            return false;
        }
    }

    /**
     * Searches for a film based on it's title
     *
     * @param title the films title
     * @return a film if it was found or null if no film was found
     **/
    public Film searchByTitle(String title) {
        Film f = null;
        synchronized (films) {
            f = films.get(title);
        }
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
        synchronized (films) {
            for (Map.Entry<String, Film> film : films.entrySet()) {
                if (film.getValue().getGenre().equalsIgnoreCase(genre)) {
                    filmList.add(film.getValue());
                }
            }
        }
        Collections.sort(filmList, new FilmComparator());
        return filmList;
    }

    /**
     * Searches for all films that matches a particular rating
     *
     * @param rating the rating to search for
     * @return an arraylist of films sorted in ascending order by the film titles
     **/
    public ArrayList<Film> searchByRating(int rating) {
        ArrayList<Film> filmList = new ArrayList<>();
        synchronized (films) {
            for (Map.Entry<String, Film> film : films.entrySet()) {
                if (film.getValue().getTotalRatings()==rating) {
                    filmList.add(film.getValue());
                }
            }
        }
        Collections.sort(filmList, new FilmComparator());
        return filmList;
    }

    public static String encode(String filmDelimiter, String filmComponentDelimiter, ArrayList<Film> films ){
      if(films.isEmpty()){
          return "";
      }
      String encoded= films.get(0).encode(filmComponentDelimiter);
      for(int i=1; i<films.size(); i++){
              encoded += filmDelimiter + films.get(i).encode(filmComponentDelimiter);
      }
      return encoded;
    }
    public static ArrayList<Film> decode(String filmDelimiter, String filmComponentDelimiter, String encoded ){
        ArrayList <Film> filtered = new ArrayList();
        if(encoded== null){
            return null;
        }
        String [] films= encoded.split(filmDelimiter);
        for(int i=0; i<films.length; i++){
          String [] film=  films[i].split(filmComponentDelimiter);
          if(film.length==4) {
              try {
                  Double dRating = Double.parseDouble(film[2]);
                 int rating= dRating.intValue();
                  int raters =Integer.parseInt(film[3]);
                  Film f = new Film(film[0],film[1],rating,raters);
                  filtered.add(f);
              }
              catch(NumberFormatException ex){
              }
          }
        }
        return filtered;
    }



}
