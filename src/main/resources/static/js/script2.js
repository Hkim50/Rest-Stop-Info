document.addEventListener("DOMContentLoaded", function () {
    // main 객체가 없다면 생성
    window.main = window.main || {};

    // 이미지 미리보기 기능 추가
    const reviewImageInput = document.getElementById('review-image');
    if (reviewImageInput) {
        reviewImageInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                // 이미지 미리보기 생성
                const previewContainer = document.getElementById('image-preview-container');
                previewContainer.innerHTML = ''; // 기존 미리보기 제거

                const previewWrapper = document.createElement('div');
                previewWrapper.className = 'image-preview-wrapper';

                const previewImage = document.createElement('img');
                previewImage.className = 'preview-image';
                previewImage.src = URL.createObjectURL(file);
                previewImage.alt = '리뷰 이미지 미리보기';

                // 삭제 버튼 추가
                const removeButton = document.createElement('button');
                removeButton.className = 'remove-preview-button';
                removeButton.innerHTML = '<i class="fas fa-times"></i>';
                removeButton.onclick = function() {
                    previewContainer.innerHTML = '';
                    reviewImageInput.value = ''; // 파일 입력 초기화
                };

                previewWrapper.appendChild(previewImage);
                previewWrapper.appendChild(removeButton);
                previewContainer.appendChild(previewWrapper);
            }
        });
    }

    // 리뷰 저장 함수 - 이미지 업로드 기능 추가
    window.main = window.main || {}; // main 변수 선언 및 초기화
    main.reviewSave = function () {
        const ratingElement = document.getElementById('rating');
        const contentElement = document.getElementById('review-content');
        const restNmElement = document.getElementById('restNm');
        const imageElement = document.getElementById('review-image');

        if (!ratingElement || !contentElement || !restNmElement) {
            console.error("리뷰 관련 요소를 찾을 수 없습니다.");
            return;
        }

        // 공백 체크
        if (!contentElement.value || contentElement.value.trim() === "") {
            alert("리뷰 내용을 입력해주세요.");
            return;
        }

        // FormData 객체 생성 (파일 업로드를 위해)
        const formData = new FormData();
        formData.append('rating', ratingElement.value);
        formData.append('content', contentElement.value);
        formData.append('restNm', restNmElement.value);

        // 이미지가 선택되었으면 추가
        if (imageElement && imageElement.files.length > 0) {
            formData.append('image', imageElement.files[0]);
        }

        fetch('/api/save', {
            method: 'POST',
            body: formData // JSON 대신 FormData 사용
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

    // 리뷰 수정 함수 - 이미지 업로드/삭제 기능 추가
    main.reviewEdit = function (reviewId, rating, content) {
        // 공백 체크
        if (!content || content.trim() === "") {
            alert("리뷰 내용을 입력해주세요.");
            return;
        }

        // 수정 폼 요소 가져오기
        const editForm = document.querySelector(`.review-item:has(#review-id[value="${reviewId}"]) .review-edit-form`);
        const editImageInput = editForm.querySelector('#edit-image');
        const deleteImageFlag = editForm.querySelector('#delete-image-flag');

        // FormData 객체 생성 (파일 업로드를 위해)
        const formData = new FormData();
        formData.append('id', reviewId);
        formData.append('rating', rating);
        formData.append('content', content);

        // 이미지 삭제 플래그가 true인 경우
        if (deleteImageFlag && deleteImageFlag.value === 'true') {
            formData.append('deleteImage', 'true');
        }

        // 새 이미지가 선택된 경우
        if (editImageInput && editImageInput.files.length > 0) {
            formData.append('image', editImageInput.files[0]);
        }

        fetch(`/api/modify`, {
            method: 'PUT',
            body: formData // JSON 대신 FormData 사용
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.message); });
            }
            return response.json();
        })
        .then(() => {
            alert('리뷰가 수정되었습니다.');
            window.location.reload(); // 페이지 새로고침
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

    // 리뷰 수정 관련 이벤트 처리
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

                // 현재 리뷰 데이터 가져오��
                const rating = reviewItem.querySelector('input[name="rating"]').value;
                const content = reviewItem.querySelector('input[name="content"]').value;

                // 수정 폼에 현재 데이터 설정
                const editRating = editForm.querySelector('#edit-rating');
                const editContent = editForm.querySelector('#edit-content');

                editRating.value = rating;
                editContent.value = content;

                // 이미지 삭제 플래그 초기화
                const deleteImageFlag = editForm.querySelector('#delete-image-flag');
                if (deleteImageFlag) {
                    deleteImageFlag.value = 'false';
                }

                // 이미지 미리보기 초기화
                const editImagePreviewContainer = editForm.querySelector('#edit-image-preview-container');
                if (editImagePreviewContainer) {
                    editImagePreviewContainer.innerHTML = '';
                }

                // 이미지 입력 필드 초기화
                const editImageInput = editForm.querySelector('#edit-image');
                if (editImageInput) {
                    editImageInput.value = '';
                }
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

        // 현재 이미지 삭제 버튼 클릭 이벤트 처리
        reviewActionsContainer.addEventListener('click', function (event) {
            const deleteImageBtn = event.target.closest('#delete-current-image');
            if (deleteImageBtn) {
                const confirmation = confirm("정말로 이 이미지를 삭제하시겠습니까?");
                if (confirmation) {
                    // 이미지 컨테이너 숨기기
                    const currentImageContainer = deleteImageBtn.closest('.current-image-container');
                    if (currentImageContainer) {
                        currentImageContainer.style.display = 'none';
                    }

                    // 이미지 삭제 플래그 설정
                    const editForm = deleteImageBtn.closest('.review-edit-form');
                    const deleteImageFlag = editForm.querySelector('#delete-image-flag');
                    if (deleteImageFlag) {
                        deleteImageFlag.value = 'true';
                    }

                    // 이미지 추가 라벨 텍스트 변경
                    const imageUploadLabel = editForm.querySelector('.image-upload-label span');
                    if (imageUploadLabel) {
                        imageUploadLabel.textContent = '이미지 추가';
                    }
                }
            }
        });

        // 수정 폼 이미지 미리보기 기능
        reviewActionsContainer.addEventListener('change', function (event) {
            const editImageInput = event.target.closest('#edit-image');
            if (editImageInput) {
                const file = editImageInput.files[0];
                if (file) {
                    // 이미지 미리보기 생성
                    const editForm = editImageInput.closest('.review-edit-form');
                    const previewContainer = editForm.querySelector('#edit-image-preview-container');
                    previewContainer.innerHTML = ''; // 기존 미리보기 제거

                    const previewWrapper = document.createElement('div');
                    previewWrapper.className = 'image-preview-wrapper';

                    const previewImage = document.createElement('img');
                    previewImage.className = 'preview-image';
                    previewImage.src = URL.createObjectURL(file);
                    previewImage.alt = '리뷰 이미지 미리보기';

                    // 삭제 버튼 추가
                    const removeButton = document.createElement('button');
                    removeButton.className = 'remove-preview-button';
                    removeButton.innerHTML = '<i class="fas fa-times"></i>';
                    removeButton.onclick = function() {
                        previewContainer.innerHTML = '';
                        editImageInput.value = ''; // 파일 입력 초기화
                    };

                    previewWrapper.appendChild(previewImage);
                    previewWrapper.appendChild(removeButton);
                    previewContainer.appendChild(previewWrapper);

                    // 기존 이미지가 있는 경우 숨기기
                    const currentImageContainer = editForm.querySelector('.current-image-container');
                    if (currentImageContainer) {
                        currentImageContainer.style.display = 'none';
                    }

                    // 이미지 삭제 플래그 설정 (새 이미지로 대체하므로)
                    const deleteImageFlag = editForm.querySelector('#delete-image-flag');
                    if (deleteImageFlag) {
                        deleteImageFlag.value = 'false'; // 새 이미지로 대체하므로 삭제 플래그는 false
                    }
                }
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