document.addEventListener("DOMContentLoaded", function() {
    const restAreaBtn = document.getElementById("btn-rest-area");
    const restaurantBtn = document.getElementById("btn-restaurant");

    const restAreaSearch = document.getElementById("search-rest-area");
    const restaurantSearch = document.getElementById("search-restaurant");

    // Toggle between search forms
    restAreaBtn.addEventListener("click", function() {
        restAreaBtn.classList.add("active");
        restaurantBtn.classList.remove("active");

        restAreaSearch.style.display = "block";
        restaurantSearch.style.display = "none";
    });

    restaurantBtn.addEventListener("click", function() {
        restaurantBtn.classList.add("active");
        restAreaBtn.classList.remove("active");

        restaurantSearch.style.display = "block";
        restAreaSearch.style.display = "none";
    });

    // Restaurant search API request
    document.getElementById("btn-search-restaurant").addEventListener("click", function() {
        const query = document.getElementById("restaurant-name").value;

        if (!query.trim()) {
            alert("검색어를 입력해주세요!");
            return;
        }

        fetch(`/api?name=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                console.log("검색 결과:", data);
                alert(`"${query}" 검색 완료! (콘솔을 확인하세요.)`);
            })
            .catch(error => {
                console.error("오류 발생:", error);
                alert("검색 중 오류가 발생했습니다.");
            });
    });
});