package edureka.devajyoti.com.tmdb.model;

/**
 * Created by devajyoti on 23/9/17.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {

    @SerializedName("id")
    private Integer id;

    @SerializedName("cast")
    private List<Cast> cast = null;

    @SerializedName("crew")
    private List<Crew> crew = null;

    /**
     * No args constructor for use in serialization
     */
    public Credits() {
    }

    /**
     * @param id
     * @param cast
     * @param crew
     */
    public Credits(Integer id, List<Cast> cast, List<Crew> crew) {
        super();
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

}