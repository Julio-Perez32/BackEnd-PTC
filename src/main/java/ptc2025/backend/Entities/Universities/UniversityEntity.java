package ptc2025.backend.Entities.Universities;

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
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
import ptc2025.backend.Entities.EntityType.EntityTypesEntity;
import ptc2025.backend.Entities.Localities.LocalitiesEntity;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;
import ptc2025.backend.Entities.SocialServiceProjects.SocialServiceProjectsEntity;
import ptc2025.backend.Entities.Users.UsersEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "UNIVERSITIES")
@Getter @Setter @ToString
@EqualsAndHashCode
public class UniversityEntity {
    @Id
    @GenericGenerator(name = "idUniversity", strategy = "guid")
    @GeneratedValue(generator = "idUniversity")
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "UNIVERSITYNAME")
    private String universityName;
    @Column(name = "RECTOR")
    private String rector;
    @Column(name = "WEBPAGE")
    private String webPage;
    @Column(name = "IMAGENURLUNIVERSITIES")
    private String imageUrlUniversities;

    //1 Universidad -> muchos Academic Levels
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<AcademicLevelsEntity> academicLevels = new ArrayList<>();

    //1 Universidad -> muchos AcademicYears
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<AcademicYearEntity> academicYear = new ArrayList<>();

    //1 Universidad -> muchos CycleTypes
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<CycleTypesEntity> cycleTypes = new ArrayList<>();//me quede hasta aca

    //1 Universidad -> muchos degreeTypes
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<DegreeTypesEntity> degreeTypes = new ArrayList<>();

    //1 Univesidad -> muuchos DocumenteCategories
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<DocumentCategoriesEntity> documentCategories = new ArrayList<>();

    //1 Universidad -> muchos EntityTypes
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<EntityTypesEntity> entityTypes = new ArrayList<>();

    //1 Universoidad -> muchos Localities
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<LocalitiesEntity> entities = new ArrayList<>();

    //1 Universidad -> muchos Modalities
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<ModalitiesEntity> modalities = new ArrayList<>();

    //1 Universidad -> muchos Requirements
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<RequirementsEntity> requirements = new ArrayList<>();

    //1 Universidad -> muchos SocialServiceProjects
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<SocialServiceProjectsEntity> socialService = new ArrayList<>();

    //1 Universidad -> muchos Users
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<UsersEntity> users = new ArrayList<>();


}
