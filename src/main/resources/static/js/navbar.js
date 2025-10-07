document.addEventListener("DOMContentLoaded", function () {
  const link = document.createElement("link");
  link.rel = "stylesheet";
  link.href = "css/navbar.css";
  document.head.appendChild(link);

  const navbarHTML = `
    <nav class="sidebar-nav">
      <div class="nav-header">
        <h2>Bike Shop</h2>
      </div>
      <ul class="nav-menu">
        <li class="nav-item">
          <a class="nav-link" href="/calendar">
            <span class="nav-text">Calendar</span>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/jobs">
            <span class="nav-text">Job List</span>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/products">
            <span class="nav-text">Products</span>
          </a>
        </li>
      </ul>
    </nav>
    `;

  // Insert navbar at the top of the body
  document.body.insertAdjacentHTML("afterbegin", navbarHTML);
  document.body.classList.add("with-navbar");
});