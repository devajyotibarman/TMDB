package edureka.devajyoti.com.tmdb.model;

/**
 * Created by devajyoti on 21/9/17.
 */

import com.google.gson.annotations.SerializedName;

public class ProductionCompany {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private Integer id;

    public ProductionCompany(String name, Integer id) {
        super();
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}