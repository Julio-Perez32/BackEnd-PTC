package ptc2025.backend.Entities.Notification;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;

import java.time.LocalDate;

@Entity
@Table(name = "NOTIFICATIONS")
@Getter
@Setter
@EqualsAndHashCode
public class NotificationEntity {
    @Id
    @GenericGenerator(name = "notificationID", strategy = "guid")
    @GeneratedValue(generator = "notificationID")
    @Column(name = "NOTIFICATIONID")
    private String notificationID;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "BODY")
    private String body;
    @Column(name = "SENTAT")
    private LocalDate sentAt;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private UsersEntity user;

    @Override
    public String toString() {
        return "NotificationEntity{" +
                "notificationID='" + notificationID + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", sentAt=" + sentAt ;
    }
    //se me cerro el push bro
}
/**NOTIFICATIONID
 USERID
 TITLE
 BODY
 SENTAT
 READAT*/

/**notificationID
 userID not null
 title not null
 body
 sentAt
 readAt*/
