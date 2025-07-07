package ptc2025.backend.Entities.securityQuestions;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SECURITYQUESTIONS")
@Getter @Setter @ToString @EqualsAndHashCode
public class securityQuestionsEntity {
    @Id
    @GenericGenerator(name = "IDsecurityQuestions", strategy = "guid")
    @GeneratedValue(generator = "IDsecurityQuestions")
    @Column(name = "QUESTIONID")
    private String id;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "QUESTION")
    private String question;
}
