package ptc2025.backend.Entities.SecurityAnswers;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SECURITYANSWERS")
@Getter @Setter @ToString @EqualsAndHashCode
public class SecurityAnswersEntity {
    @Id
    @GenericGenerator(name = "idSecurityQuestion", strategy = "guid")
    @GeneratedValue(generator = "idSecurityQuestion")
    @Column(name = "ANSWERID")
    private String id;
    @Column(name = "QUESTIONID")
    private String questionID;
    @Column(name = "USERID")
    private String userID;
    @Column(name = "ANSWER")
    private String answer;
}
