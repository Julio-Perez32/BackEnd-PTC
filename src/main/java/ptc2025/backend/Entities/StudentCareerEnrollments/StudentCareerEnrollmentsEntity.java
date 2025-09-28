package ptc2025.backend.Entities.StudentCareerEnrollments;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.SocialServiceProjects.SocialServiceProjectsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;
import ptc2025.backend.Entities.careers.CareerEntity;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENTCAREERENROLLMENTS")
@Getter @Setter @EqualsAndHashCode
public class StudentCareerEnrollmentsEntity {

    @Id
    @GenericGenerator(name = "StudentCareerEnrollmentID", strategy = "guid")
    @GeneratedValue(generator = "StudentCareerEnrollmentID")
    @Column(name = "STUDENTCAREERENROLLMENTID")
    private String studentCareerEnrollmentID;

    @ManyToOne
    @JoinColumn(name = "CAREERID", referencedColumnName = "CAREERID")
    private CareerEntity career;

    @ManyToOne
    @JoinColumn(name = "STUDENTID", referencedColumnName = "STUDENTID")
    private StudentsEntity student;

    //la llave de
    @ManyToOne
    @JoinColumn(name = "SOCIALSERVICEPROJECTID", referencedColumnName = "SOCIALSERVICEPROJECTID")
    private SocialServiceProjectsEntity socialServiceProject;

    @Column(name = "STARTDATE")
    private LocalDate startDate;

    @Column(name = "ENDDATE")
    private LocalDate endDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "STATUSDATE")
    private LocalDate statusDate;

    @Column(name = "SERVICESTARTDATE")
    private LocalDate serviceStartDate;

    @Column(name = "SERVICEENDDATE")
    private LocalDate serviceEndDate;

    @Column(name = "SERVICESTATUS")
    private String serviceStatus;

    @Column(name = "SERVICESTATUSDATE")
    private LocalDate serviceStatusDate;

    @Override
    public String toString() {
        return "StudentCareerEnrollmentsEntity{" +
                "studentCareerEnrollmentID='" + studentCareerEnrollmentID + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", statusDate=" + statusDate +
                ", serviceStartDate=" + serviceStartDate +
                ", serviceEndDate=" + serviceEndDate +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", serviceStatusDate=" + serviceStatusDate +
                '}';
    }
    //se me cerro el push bro
}
