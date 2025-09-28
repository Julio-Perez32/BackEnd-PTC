package ptc2025.backend.Entities.SocialServiceProjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;

import java.util.ArrayList;
import java.util.List;

//SocialServiceProjects
@Entity
@Table(name = "SOCIALSERVICEPROJECTS")
@Getter @Setter
@EqualsAndHashCode
public class SocialServiceProjectsEntity
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
    @OneToMany(mappedBy = "SocialServiceProject", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CareerSocialServiceProjectEntity> careerSocialService = new ArrayList<>();

    @OneToMany(mappedBy = "socialServiceProject", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentCareerEnrollmentsEntity> studentCareerEnrollments = new ArrayList<>();

    @Override
    public String toString() {
        return "SocialServiceProjectsEntity{" +
                "socialServiceProjectID='" + socialServiceProjectID + '\'' +
                ", socialServiceProjectName='" + socialServiceProjectName + '\'' +
                ", description='" + description ;
    }
}
