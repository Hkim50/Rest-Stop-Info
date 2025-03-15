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

    // 리뷰 수정 함수 (새로 추가)
    main.reviewEdit = function (reviewId, rating, content) {
        // 공백 체크
        if (!content || content.trim() === "") {
            alert("리뷰 내용을 입력해주세요.");
            return;
        }

        const data = {
            id: reviewId,
            rating: rating,
            content: content
        };

        fetch(`/api/modify`, {
            method: 'PUT',
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
            alert('리뷰가 수정되었습니다.');
            // 페이지 새로고침 대신 UI만 업데이트
             window.location.reload();
        })
        .catch(error => {
            alert('리뷰 수정 실패: ' + error.message);
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

    // 리뷰 수정 관련 이벤트 처리 (새로 추가)
    if (reviewActionsContainer) {
        // 수정 버튼 클릭 이벤트 처리
        reviewActionsContainer.addEventListener('click', function (event) {
            const editBtn = event.target.closest('#btn-review-edit');
            if (editBtn) {
                const reviewItem = editBtn.closest('.review-item');
                const editForm = reviewItem.querySelector('.review-edit-form');

                // 모든 수정 폼 닫기
                document.querySelectorAll('.review-edit-form').forEach(form => {
                    form.style.display = 'none';
                });

                // 현재 수정 폼 열기
                editForm.style.display = 'block';

                // 현재 리뷰 데이터 가져오기
                const rating = reviewItem.querySelector('input[name="rating"]').value;
                const content = reviewItem.querySelector('input[name="content"]').value;

                // 수정 폼에 현재 데이터 설정
                const editRating = editForm.querySelector('#edit-rating');
                const editContent = editForm.querySelector('#edit-content');

                editRating.value = rating;
                editContent.value = content;
            }
        });

        // 수정 취소 버튼 클릭 이벤트 처리
        reviewActionsContainer.addEventListener('click', function (event) {
            const cancelBtn = event.target.closest('#btn-review-cancel');
            if (cancelBtn) {
                const editForm = cancelBtn.closest('.review-edit-form');
                editForm.style.display = 'none';
            }
        });

        // 수정 완료 버튼 클릭 이벤트 처리
        reviewActionsContainer.addEventListener('click', function (event) {
            const updateBtn = event.target.closest('#btn-review-update');
            if (updateBtn) {
                const editForm = updateBtn.closest('.review-edit-form');
                const reviewItem = editForm.closest('.review-item');

                // 수정된 데이터 가져오기
                const newRating = editForm.querySelector('#edit-rating').value;
                const newContent = editForm.querySelector('#edit-content').value;

                // 리뷰 ID 가져오기
                const reviewId = editForm.querySelector('#review-id2').value;
                console.log(reviewId)

                // UI 업데이트
                updateReviewUI(reviewItem, newRating, newContent);

                // 서버에 업데이트 요청 보내기
                main.reviewEdit(reviewId, newRating, newContent);

                // 수정 폼 숨기기
                editForm.style.display = 'none';
            }
        });
    }

    // 리뷰 UI 업데이트 함수
    function updateReviewUI(reviewItem, rating, content) {
        // 별점 업데이트
        const starsContainer = reviewItem.querySelector('.stars');
        starsContainer.innerHTML = '';

        // 별점 표시 업데이트
        for (let i = 1; i <= 5; i++) {
            const star = document.createElement('span');
            star.textContent = i <= rating ? '★' : '☆';
            starsContainer.appendChild(star);
        }

        // 리뷰 내용 업데이트
        reviewItem.querySelector('.review-text').textContent = content;

        // 숨겨진 입력 필드 업데이트
        reviewItem.querySelector('input[name="rating"]').value = rating;
        reviewItem.querySelector('input[name="content"]').value = content;
    }
});