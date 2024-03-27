package business;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmManagerTest {
/**when adding a new film**/
    @Test
    void add() {
        FilmManager fm= new FilmManager();
        Film f1= new Film("batman", "fiction",20,4);
        boolean actual =fm.add(f1);
        assertTrue(actual);
    }

    /**when adding a film that already exist**/
    @Test
    void add_FilmExist() {
        FilmManager fm= new FilmManager();
        Film f1= new Film("aaa", "fiction",20,4);
        boolean actual =fm.add(f1);
        assertFalse(actual);
    }
    /**when adding a null**/
    @Test
    void add_NullFilm() {
        FilmManager fm= new FilmManager();
        Film f1= null;
        assertThrows(NullPointerException.class,()->{ fm.add(f1); });
    }
/**when removing an existing film**/
    @Test
    void remove() {
        FilmManager fm= new FilmManager();
       boolean actual= fm.remove("aaa");
       assertTrue(actual);
    }
    /**when removing a film that doesn't existing film**/
    @Test
    void remove_WhenFilmDoesNotExist() {
        FilmManager fm= new FilmManager();
        boolean actual= fm.remove("xxxx");
        assertFalse(actual);
    }
/**Rating an existing film**/
    @Test
    void rateFilm() {
        FilmManager fm= new FilmManager();
        boolean actual= fm.rateFilm("aaa",5);
        assertTrue(actual);
    }
    /**Rating a film that doesn't exist film**/
    @Test
    void rateFilm_WhenFilmDoesNotExist() {
        FilmManager fm= new FilmManager();
        boolean actual= fm.rateFilm("nff",5);
        assertFalse(actual);
    }
/**when the film exist**/
    @Test
    void searchByTitle() {
        FilmManager fm= new FilmManager();
        Film expected= new Film("aaa", "fiction",20,4);
        Film actual= fm.searchByTitle("aaa");
        assertEquals(actual,expected);

    }
    /**when film doesn't exist**/
    @Test
    void searchByTitle_WhenFilmDoesNotExist() {
        FilmManager fm= new FilmManager();
        Film expected= null;
        Film actual= fm.searchByTitle("xxx");
        assertEquals(actual,expected);

    }
/**when the genre exist**/
    @Test
    void searchByGenre() {
        FilmManager fm= new FilmManager();
        ArrayList <Film> expected= new ArrayList<>();
        Film f1= new Film("aaa", "fiction",20,4);
        Film f2= new Film("ccc", "fiction",5,4);
        Film f3= new Film("zzz", "fiction",5,4);
        expected.add(f1);
        expected.add(f2);
        expected.add(f3);
       ArrayList <Film> actual= fm.searchByGenre("fiction");
    }
    /**when the genre doesn't genre exist**/
    @Test
    void searchByGenre_WhenGenreDoesNotExist() {
        FilmManager fm= new FilmManager();
        ArrayList <Film> expected= new ArrayList<>();
        ArrayList <Film> actual= fm.searchByGenre("play");
    }
}