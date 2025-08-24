package ptc2025.backend.Entities.DocumentCategories;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Universities.UniversityEntity;


@Entity
@Table(name = "DOCUMENTCATEGORIES")
@Getter @Setter @ToString @EqualsAndHashCode
public class DocumentCategoriesEntity {
    @Id
    @GenericGenerator(name = "IDdocumentCategory", strategy = "guid")
    @GeneratedValue(generator = "IDdocumentCategory")
    @Column(name = "DOCUMENTCATEGORYID")
    private String id;
    @Column(name = "DOCUMENTCATEGORY")
    private String documentCategory;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

}
