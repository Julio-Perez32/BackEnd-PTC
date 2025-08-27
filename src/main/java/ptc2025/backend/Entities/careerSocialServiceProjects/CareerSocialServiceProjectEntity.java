package ptc2025.backend.Entities.careerSocialServiceProjects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptc2025.backend.Entities.SocialService.SocialServiceEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.careers.CareerEntity;

//careerSocialServiceProjects
@Entity
@Table(name = "CAREERSOCIALSERVICEPROJECTS")
@Getter
@Setter
public class CareerSocialServiceProjectEntity {

    @Id
    @Column(name = "CAREERPROJECTID")
    private String id;

    //Llave de career
    @ManyToOne
    @JoinColumn(name = "CAREERID", referencedColumnName = "CAREERID")
    private CareerEntity career;

    //llave de socialserviceproyects
    @ManyToOne
    @JoinColumn(name = "SOCIALSERVICEPROJECTID", referencedColumnName = "SOCIALSERVICEPROJECTID")
    private SocialServiceEntity socialService;




}
