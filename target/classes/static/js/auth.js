// ============================================
// Auth guard — checks session, redirects by role
// ============================================

async function getCurrentUser() {
  try {
    return await API.get('/api/auth/me');
  } catch (e) {
    return null;
  }
}

function redirectToDashboard(role) {
  if (role === 'ADMIN')   window.location.href = '/admin/dashboard.html';
  if (role === 'TEACHER') window.location.href = '/teacher/dashboard.html';
  if (role === 'STUDENT') window.location.href = '/student/dashboard.html';
}

// Call on any protected page — enforces role, redirects otherwise.
async function requireRole(expectedRole) {
  const user = await getCurrentUser();
  if (!user) {
    window.location.href = '/index.html';
    return null;
  }
  if (expectedRole && user.role !== expectedRole) {
    redirectToDashboard(user.role);
    return null;
  }
  return user;
}

async function logout() {
  try { await API.post('/api/auth/logout'); } catch (e) {}
  window.location.href = '/index.html';
}