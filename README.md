# TeacherApp
Teacher app for school

## Pages/Views ##
- Login Page for Teachers
- Assignment Management for Teachers
- Attendance Management
- Notification System
 
## API Request URL ##
### Login ###
POST Request: 

#### Headers ####
- X-Teacher-App: true

#### Parameters ####
- username
- password
- action: 'logmein'

On successful request you will receive json data 
```
http://cloudeducate.com/auth/login.json
```

### After Login ###
Foreach Request send the headers
- X-App: teacher
- X-Access-Token: {$token_stored}

To get successful response 

### Get Complete Profile (after login) ###
```
http://cloudeducate.com/teacher/profile.json
```

### Update Profile ###
POST Request parameters
- action = saveUser
```
http://cloudeducate.com/teacher/settings.json
```

### Manage Courses ###
```
http://cloudeducate.com/teacher/courses.json
```

### Manage Assignment ###
- $course_id (optional) argument. If not given then all assignments will be displayed
```
http://cloudeducate.com/assignments/manage/{$course_id}.json
```

### Create Assignment ###
POST Request parameter
- title
- description
- deadline (YYYY-MM-DD)
- action: assignment
```
http://cloudeducate.com/assignments/create/{$course_id}/{$classroom_id}.json
```

### Manage Attendance ###
Send GET request to see the response
- If attendance already saved for the day 'message' key will be already set

POST Request parameters
- user_id[]: Array
- presence[]: Array, each element 1: present, 0: absent
- action: saveAttendance

On successfully submission 'message' key will be set
```
http://cloudeducate.com/teacher/manageAttendance.json
```
