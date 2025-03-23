package CrudPractice.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name="reported")
@Getter
@NoArgsConstructor
public class ReportedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewsEntity review;

    private String reason;

    private String reporterName;

    public ReportedEntity(ReviewsEntity review, String reason, String reporterName) {
        this.review = review;
        this.reason = reason;
        this.reporterName = reporterName;
    }
}
