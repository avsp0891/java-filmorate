package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
public class Mpa {
    private Integer id;
    private MpaName name;

    @JsonCreator
    public Mpa(Integer id) {
        this.id = id;
        switch (id) {
            case 1:
                name = MpaName.G;
                break;
            case 2:
                name = MpaName.PG;
                break;
            case 3:
                name = MpaName.PG_13;
                break;
            case 4:
                name = MpaName.R;
                break;
            case 5:
                name = MpaName.NC_17;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mpa)) return false;
        Mpa mpa = (Mpa) o;
        return Objects.equals(id, mpa.id) && name == mpa.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

enum MpaName {
    G,
    PG,
    PG_13,
    R,
    NC_17
}
