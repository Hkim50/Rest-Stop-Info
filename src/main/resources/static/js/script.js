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
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has("error")) {
            alert("로그인 실패! 이메일 또는 비밀번호를 확인하세요.");
        }

});