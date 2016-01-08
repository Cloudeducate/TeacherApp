# TeacherApp
Teacher app for school

## Pages/Views ##
- Login Page for Teachers
- Assignment Management for Teachers
- Attendance Management
- Notification System
 
## API Request URL ##
### Login ###
POST Request parameter
- username
- password
- action: 'logmein'
```
http://cloudeducate.com/auth/login.json
```

### Get Complete Profile (after login) ###
```
http://cloudeducate.com/teacher/profile.json
```

### Update Profile ###
POST Request parameter
- name
- phone
- address
- city
- action = saveUser
```
http://cloudeducate.com/teacher/settings.json
```

### Manage Courses ###

```
http://cloudeducate.com/teacher/courses.json
```

### Manage Assignment ###
```
http://cloudeducate.com/assignments/manage.json
```

### Create Assignment ###
POST Request parameter
- title
- description
- deadline (YYYY-MM-DD)
```
http://cloudeducate.com/assignments/create/{$course_id}/{$classroom_id}.json
```
