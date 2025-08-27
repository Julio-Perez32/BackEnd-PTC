package ptc2025.backend.Entities.Faculties;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;
import ptc2025.backend.Entities.SubjectFamilies.SubjectFamiliesEntity;
import ptc2025.backend.Entities.facultyCorrelatives.facultyCorrelativesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FACULTIES")
@Getter @Setter @EqualsAndHashCode @ToString
public class FacultiesEntity {

    @Id
    @GenericGenerator(name = "facultyID", strategy = "guid")
    @GeneratedValue(generator = "facultyID")
    @Column(name = "FACULTYID")
    private String facultyID;
    @Column(name = "FACULTYNAME")
    private String facultyName;
    @Column(name = "FACULTYCODE")
    private String facultyCode;
    @Column(name = "CONTACTPHONE")
    private String contactPhone;
    @Column(name = "CORRELATIVECODE")
    private String correlativeCode;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<facultyCorrelativesEntity> academicLevels = new ArrayList<>();

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<SubjectFamiliesEntity> academicYear = new ArrayList<>();

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<FacultyDeansEntity> cycleTypes = new ArrayList<>();

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<DepartmentsEntity> degreeTypes = new ArrayList<>();

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<DocumentCategoriesEntity> documentCategories = new ArrayList<>();
}
