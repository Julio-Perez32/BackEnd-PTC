package ptc2025.backend.Entities.SocialService;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;

import java.util.ArrayList;
import java.util.List;

//SocialServiceProjects
@Entity
@Table(name = "SOCIALSERVICEPROJECTS")
@Getter @Setter @ToString
@EqualsAndHashCode
public class SocialServiceEntity
{
    @Id
    @GenericGenerator(name = "idSocialService", strategy = "guid")
    @GeneratedValue (generator = "idSocialService")
    @Column(name = "SOCIALSERVICEPROJECTID")
    private String socialServiceProjectID;
    @Column (name = "SOCIALSERVICEPROJECTNAME")
    private String socialServiceProjectName;
    @Column(name = "DESCRIPTION")
    private String description;
    //Llave de university
    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

    //Dando sus llaves
    @OneToMany(mappedBy = "socialService", cascade = CascadeType.ALL)
    private List<CareerSocialServiceProjectEntity> careerSocialService = new ArrayList<>();

    @OneToMany(mappedBy = "socialService", cascade = CascadeType.ALL)
    private List<StudentCareerEnrollmentsEntity> studentCareerEnrollments = new ArrayList<>();

}
