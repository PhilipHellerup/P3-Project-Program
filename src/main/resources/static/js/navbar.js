document.addEventListener("DOMContentLoaded", function () {
  const link = document.createElement("link");
  link.rel = "stylesheet";
  link.href = "css/navbar.css";
  document.head.appendChild(link);

   function insertNavbar() {
    const navbarHTML = `
      <nav class="sidebar-nav">
        <div class="nav-header">
          <h2>Performsport</h2>
          <button id="close-navbar" class="close-navbar-btn" aria-label="Close navbar">&times;</button>
        </div>
        <ul class="nav-menu">
          <li class="nav-item">
            <a class="nav-link" href="/calendar">
              <span class="nav-text">Kalender</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/jobs">
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
      openBtn.className = "open-navbar-btn";
      openBtn.textContent = "+";
      openBtn.setAttribute("aria-label", "Open navbar");
      openBtn.style.position = "fixed";
      openBtn.style.top = "1rem";
      openBtn.style.left = "1rem";
      document.body.appendChild(openBtn);

      openBtn.addEventListener("click", function () {
        openBtn.remove();
        insertNavbar();
      });
    }
  }

  insertNavbar();
});