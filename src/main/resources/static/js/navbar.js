document.addEventListener("DOMContentLoaded", function () {
    // Add Font Awesome script
    const faScript = document.createElement("script");
    faScript.src = "https://kit.fontawesome.com/ffe0f0379f.js";
    faScript.crossOrigin = "anonymous";
    document.head.appendChild(faScript);

    const link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = "css/navbar.css";
    document.head.appendChild(link);

    function insertNavbar() {
        const navbarHTML = `
      <nav class="sidebar-nav w-15">
        <div class="nav-header">
          <h2>Performsport</h2>
          <button id="close-navbar" class="close-navbar-btn fa-solid fa-angles-left" aria-label="Close navbar "></button>
        </div>
        <ul class="nav-menu">
          <li class="nav-item">
            <a class="nav-link" href="/calendar">
              <span class="nav-text">Kalender</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/jobliste">
              <span class="nav-text">Job Liste</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/products">
              <span class="nav-text">Produkter</span>
            </a>
          </li>
        </ul>
      </nav>
    `;

        document.body.insertAdjacentHTML("afterbegin", navbarHTML);
        document.body.classList.add("with-navbar");

        const closeBtn = document.getElementById("close-navbar");
        if (closeBtn) {
            closeBtn.addEventListener("click", function () {
                const navbar = document.querySelector(".sidebar-nav");
                if (navbar) {
                    navbar.remove();
                    document.body.classList.remove("with-navbar");
                    showOpenButton();
                }
            });
        }
    }

    function showOpenButton() {
        if (!document.getElementById("open-navbar")) {
            const openBtn = document.createElement("button");
            openBtn.id = "open-navbar";
            openBtn.className = "open-navbar-btn fa-solid fa-angles-right";
            openBtn.setAttribute("aria-label", "Open navbar");
            document.body.appendChild(openBtn);

            openBtn.addEventListener("click", function () {
                openBtn.remove();
                insertNavbar();
            });
        }
    }

    insertNavbar();
});