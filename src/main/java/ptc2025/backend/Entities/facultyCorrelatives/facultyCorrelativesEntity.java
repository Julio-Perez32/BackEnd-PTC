package ptc2025.backend.Entities.facultyCorrelatives;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.CodeGenerators.CodeGeneratorsEntity;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FACULTYCORRELATIVES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class facultyCorrelativesEntity {
    @Id
    @GenericGenerator(name = "correlativeID", strategy = "guid")
    @GeneratedValue(generator = "correlativeID")
    @Column(name = "CORRELATIVEID")
    private String correlativeID;

    @Column(name = "CORRELATIVENUMBER")
    private Integer correlativeNumber;

    @OneToMany(mappedBy = "facultyCorrelative", cascade = CascadeType.ALL)
    private List<CodeGeneratorsEntity> codeGenerators = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "FACULTYID", referencedColumnName = "FACULTYID")
    private FacultiesEntity faculties;
}
