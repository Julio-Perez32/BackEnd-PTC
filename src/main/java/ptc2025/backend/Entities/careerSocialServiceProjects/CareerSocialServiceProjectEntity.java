package ptc2025.backend.Entities.careerSocialServiceProjects;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.SocialServiceProjects.SocialServiceProjectsEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.careers.CareerEntity;

import java.util.ArrayList;
import java.util.List;

//careerSocialServiceProjects
@Entity
@Table(name = "CAREERSOCIALSERVICEPROJECTS")
@Getter @Setter @EqualsAndHashCode @ToString
public class CareerSocialServiceProjectEntity {

    @Id
    @GenericGenerator(name = "careerProjectid", strategy = "guid")
    @GeneratedValue(generator = "careerProjectid")
    @Column(name = "CAREERPROJECTID")
    private String id;


    //Llave de career
    @ManyToOne
    @JoinColumn(name = "CAREERID", referencedColumnName = "CAREERID")
    private CareerEntity career;

    //llave de socialserviceproyects
    @ManyToOne
    @JoinColumn(name = "SOCIALSERVICEPROJECTID", referencedColumnName = "SOCIALSERVICEPROJECTID")
    private SocialServiceProjectsEntity SocialServiceProject;




}
