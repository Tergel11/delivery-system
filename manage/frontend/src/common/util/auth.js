export const checkAuthRole = (role, applicationRoles) => {
  // console.log(role, 'role')
  // console.log(applicationRoles, 'applicationRoles')
  if (!role || !applicationRoles)
    return false

  if (Array.isArray(role)) {
    for (let i = 0; i < applicationRoles.length; i++) {
      const appRole = applicationRoles[i]
      if (role.indexOf(appRole) >= 0) {
        // console.log('yey ' + role + ' -> ' + appRole)
        return true
      }
    }
  }

  if (role === applicationRoles)
    return true

  return applicationRoles.indexOf(role) >= 0
}
