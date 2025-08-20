package ptc2025.backend.Entities.PermisionCategory;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PERMISSIONCATEGORIES")
@Getter @Setter @EqualsAndHashCode @ToString
public class PermissionCategoriesEntity {

    @Id
    @GenericGenerator(name = "categoryID", strategy = "guid")
    @GeneratedValue(generator = "categoryID")
    @Column(name = "CATEGORYID")
    private String categoryID;
    @Column(name = "CATEGORYNAME")
    private String categoryName;
}
