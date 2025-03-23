package CrudPractice.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReportRequest {

    private Long reviewId;
    private String reason;
    private String reporter;

    // 유효성 검증 메서드
    public boolean isValid() {
        return reviewId != null && reason != null && !reason.trim().isEmpty();
    }
}
