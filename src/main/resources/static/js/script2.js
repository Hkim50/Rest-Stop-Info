document.addEventListener("DOMContentLoaded", function () {
    // main 객체가 없다면 생성
    window.main = window.main || {};

    // 리뷰 저장 함수
    main.reviewSave = function () {
        const ratingElement = document.getElementById('rating');
        const contentElement = document.getElementById('review-content');
        const restNmElement = document.getElementById('restNm');

        if (!ratingElement || !contentElement || !restNmElement) {
            console.error("리뷰 관련 요소를 찾을 수 없습니다.");
            return;
        }

        const data = {
            rating: ratingElement.value,
            content: contentElement.value,
            restNm: restNmElement.value
        };

        // 공백 체크
        if (!data.content || data.content.trim() === "") {
            alert("리뷰 내용을 입력해주세요.");
            return;
        }

        fetch('/api/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.message); });
            }
            return response.json();
        })
        .then(() => {
            alert('리뷰가 등록되었습니다.');
            window.location.reload();
        })
        .catch(error => {
            alert('리뷰 등록 실패: ' + error.message);
        });
    };

    // 리뷰 삭제 함수
    main.reviewDelete = function (reviewId) {

    const confirmation = confirm("정말로 이 리뷰를 삭제하시겠습니까?");
            if (!confirmation) {
                return;  // 사용자가 취소를 클릭하면 삭제하지 않음
            }

        fetch(`/api/delete/${reviewId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.message); });
            }
            return response.json();
        })
        .then(() => {
            alert('리뷰가 삭제되었습니다.');
            window.location.reload();
        })
        .catch(error => {
            alert('리뷰 삭제 실패: ' + error.message);
        });
    };

    // 리뷰 저장 버튼 이벤트 처리
    const reviewSubmitBtn = document.getElementById('btn-review-submit');
    if (reviewSubmitBtn) {
        reviewSubmitBtn.addEventListener('click', function (event) {
            event.preventDefault();  // 기본 form 제출 방지
            main.reviewSave();
        });
    }

    // 리뷰 삭제 버튼 이벤트 처리 (동적 생성)
    const reviewActionsContainer = document.querySelector('.reviews-list');
    if (reviewActionsContainer) {
        reviewActionsContainer.addEventListener('click', function (event) {
            const deleteBtn = event.target.closest('#btn-review-delete');
            if (deleteBtn) {
                const reviewId = deleteBtn.closest('.review-item').querySelector('#review-id').value;
                main.reviewDelete(reviewId);
            }
        });
    }
});


