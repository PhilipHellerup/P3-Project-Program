document.addEventListener('DOMContentLoaded', function () {
    // Ensure document.body exists (some browsers fire DOMContentLoaded early with null body in edge cases)
    if (!document.body) {
        document.addEventListener('readystatechange', function onReady() {
            if (document.readyState === 'complete' && document.body) {
                document.removeEventListener('readystatechange', onReady);
                initNavbar();
            }
        });
        return;
    }
    initNavbar();

    function initNavbar() {
        // Load Font Awesome (keep as you had it; watch CSP)
        const faScript = document.createElement('script');
        faScript.src = 'https://kit.fontawesome.com/ffe0f0379f.js';
        faScript.crossOrigin = 'anonymous';
        document.head.appendChild(faScript);

        // Utility: ensure the CSS link exists and load it, return a Promise
        function ensureNavbarCss() {
            return new Promise((resolve, reject) => {
                // Use root-relative path to avoid page-relative resolutions
                const href = '/css/navbar.css';

                // If already present, resolve when it's loaded (or immediately)
                const existing = Array.from(
                    document.head.querySelectorAll('link[rel="stylesheet"]'),
                ).find((l) => (l.getAttribute('href') || l.href) === href);
                if (existing) {
                    // If it's already loaded (sheet available), resolve immediately
                    if (existing.sheet) {
                        return resolve();
                    }
                    // otherwise wait for load/error
                    existing.addEventListener('load', () => resolve(), { once: true });
                    existing.addEventListener('error', (e) => reject(e), { once: true });
                    // and give a fallback timeout
                    setTimeout(() => resolve(), 1500);
                    return;
                }

                const link = document.createElement('link');
                link.rel = 'stylesheet';
                link.href = href;
                link.addEventListener('load', () => resolve(), { once: true });
                link.addEventListener('error', (e) => reject(e), { once: true });
                document.head.appendChild(link);

                // safety fallback: if load doesn't fire in reasonable time, resolve so UI still appears
                setTimeout(() => {
                    if (!link.sheet) {
                        console.warn('navbar.css did not load within timeout — continuing anyway');
                    }
                    resolve();
                }, 2500);
            });
        }

        function insertNavbar() {
            // Prevent duplicates
            if (document.querySelector('.sidebar-nav')) return;

          const onCalendar = (location.pathname || '').startsWith('/kalender');
          const legendHTML = onCalendar
            ? `
              <div class="status-legend" role="region" aria-label="Job status farveguide">
                <div class="legend-title">Status guide</div>
                <ul class="legend-list">
                  <li class="legend-item"><span class="legend-dot" style="--dot:#f65d60"></span><span class="legend-label">Ikke Indleveret</span></li>
                  <li class="legend-item"><span class="legend-dot" style="--dot:#feb568"></span><span class="legend-label">Indleveret</span></li>
                  <li class="legend-item"><span class="legend-dot" style="--dot:#0088ff"></span><span class="legend-label">Igangværende</span></li>
                  <li class="legend-item"><span class="legend-dot" style="--dot:#4b41c8"></span><span class="legend-label">Mangler Del</span></li>
                  <li class="legend-item"><span class="legend-dot" style="--dot:#32a759"></span><span class="legend-label">Færdig</span></li>
                  <li class="legend-item"><span class="legend-dot" style="--dot:#939292"></span><span class="legend-label">Afhentet</span></li>
                </ul>
              </div>
            `
            : '';

            const navbarHTML = `
            <nav class="sidebar-nav" aria-hidden="false">
                <div class="nav-header">
                  <a href="/kalender">
                    <img src="/images/Perform_logo_white.avif" alt="Perform" class="logo">
                  </a>
                </div>
                <ul class="nav-menu">
                    <!-- <li class="nav-item"><a class="nav-link" href="/"><span class="nav-text">Hjem</span></a></li> -->
                    <li class="nav-item"><a class="nav-link" href="/kalender"><span class="nav-text">Kalender</span></a></li>
                    <li class="nav-item"><a class="nav-link" href="/jobliste"><span class="nav-text">Reparationsliste</span></a></li>
                    <li class="nav-item"><a class="nav-link" href="/products"><span class="nav-text">Produkter</span></a></li>
                </ul>
                ${legendHTML}
            </nav>
        `;

            document.body.insertAdjacentHTML('afterbegin', navbarHTML);
            const navbar = document.querySelector('.sidebar-nav');
            document.body.classList.add('with-navbar', 'content-push');

            navbar.style.transform = 'translateX(0)';

            // Dynamic navbar
            /** const closeBtn = document.getElementById('close-navbar');
      if (closeBtn) {
        closeBtn.addEventListener('click', function () {
          const navbar = document.querySelector('.sidebar-nav');
          if (navbar) {
            navbar.style.transform = 'translateX(-100%)';
            document.body.classList.remove('content-push');

            setTimeout(() => {
              navbar.remove();
              document.body.classList.remove('with-navbar');
              showOpenButton();
            }, 300);
          }
        });
      } **/
        }

        /** function showOpenButton() {
      if (!document.getElementById('open-navbar')) {
        const openBtn = document.createElement('button');
        openBtn.id = 'open-navbar';
        openBtn.className = 'open-navbar-btn fa-solid fa-angles-right';
        openBtn.setAttribute('aria-label', 'Open navbar');
        document.body.appendChild(openBtn);

        openBtn.addEventListener('click', function () {
          openBtn.remove();
          // ensure CSS is present before re-inserting
          ensureNavbarCss()
            .then(insertNavbar)
            .catch(() => insertNavbar());
        });
      }
    } **/

        // Ensure CSS is loaded before inserting the navbar (prevents flash / 404 issues due to relative path)
        ensureNavbarCss()
            .then(() => insertNavbar())
            .catch((err) => {
                console.error('Failed to load navbar CSS:', err);
                // Still insert navbar so functionality isn't blocked — user will see unstyled until stylesheet loads
                insertNavbar();
            });
    }
});
