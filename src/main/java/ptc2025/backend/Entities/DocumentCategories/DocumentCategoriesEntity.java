package ptc2025.backend.Entities.DocumentCategories;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "DOCUMENTCATEGORIES")
@Getter @Setter @ToString @EqualsAndHashCode
public class DocumentCategoriesEntity {
    @Id
    @GenericGenerator(name = "IDdocumentCategory", strategy = "guid")
    @GeneratedValue(generator = "IDdocumentCategory")
    @Column(name = "DOCUMENTCATEGORYID")
    private String id;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "DOCUMENTCATEGORY")
    private String documentCategory;

}
