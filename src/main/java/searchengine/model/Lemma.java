package searchengine.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "lemma", indexes = {@javax.persistence.Index(name = "lemma_list", columnList = "lemma")})
@NoArgsConstructor
public class Lemma implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private Site siteEntityId;
    private String lemma;
    private int frequency;

    @OneToMany(mappedBy = "lemma", cascade = CascadeType.ALL)
    private List<Index> index = new ArrayList<>();


    public Lemma(String lemma, int frequency, Site siteEntityId) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.siteEntityId = siteEntityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lemma that = (Lemma) o;
        return id == that.id && frequency == that.frequency &&
                siteEntityId.equals(that.siteEntityId) &&
                lemma.equals(that.lemma) &&
                index.equals(that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteEntityId, lemma, frequency, index);
    }

    @Override
    public String toString() {
        return "LemmaEntity{" +
                "id=" + id +
                ", siteEntityId=" + siteEntityId +
                ", lemma='" + lemma + '\'' +
                ", frequency=" + frequency +
                ", index=" + index +
                '}';
    }
}
