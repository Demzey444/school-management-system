// ============================================
// Layout — renders sidebar + header per role
// ============================================

const MENUS = {
  ADMIN: [
    { href: '/admin/dashboard.html',  label: 'Dashboard' },
    { href: '/admin/students.html',   label: 'Students' },
    { href: '/admin/teachers.html',   label: 'Teachers' },
    { href: '/admin/classes.html',    label: 'Classes' },
    { href: '/admin/subjects.html',   label: 'Subjects' },
    { href: '/admin/results.html',    label: 'Results' },
    { href: '/admin/attendance.html', label: 'Attendance' },
    { href: '/admin/profile.html',    label: 'Profile' }
  ],
  TEACHER: [
    { href: '/teacher/dashboard.html',  label: 'Dashboard' },
    { href: '/teacher/results.html',    label: 'Results' },
    { href: '/teacher/attendance.html', label: 'Attendance' },
    { href: '/teacher/profile.html',    label: 'Profile' }
  ],
  STUDENT: [
    { href: '/student/dashboard.html',  label: 'Dashboard' },
    { href: '/student/results.html',    label: 'My Results' },
    { href: '/student/attendance.html', label: 'My Attendance' },
    { href: '/student/profile.html',    label: 'Profile' }
  ]
};

function renderLayout(user) {
  const menu = MENUS[user.role] || [];
  const currentPath = window.location.pathname;

  const navItems = menu.map(item => {
    const active = currentPath === item.href ? 'class="active"' : '';
    return `<a href="${item.href}" ${active}>${item.label}</a>`;
  }).join('');

  const layout = document.createElement('div');
  layout.className = 'app-layout';
  layout.innerHTML = `
    <aside class="sidebar">
      <div class="sidebar-brand">
        <span class="brand-mark">Greenfield</span>
        International School
      </div>
      <nav>${navItems}</nav>
      <div class="sidebar-footer">
        <div style="font-weight:600;">${user.fullName || user.username}</div>
        <div>${user.role}</div>
        <button class="btn btn-secondary btn-sm" style="margin-top:10px;width:100%;" onclick="logout()">Log out</button>
      </div>
    </aside>
    <main class="main">
      <div id="page-content"></div>
    </main>
  `;

  document.body.innerHTML = '';
  document.body.appendChild(layout);
  return document.getElementById('page-content');
}