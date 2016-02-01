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
/auth/login.json
```

### After Login ###
Foreach Request send the headers
- X-App: teacher
- X-Access-Token: {$token_stored}

To get successful response 

### Get Complete Profile (after login) ###
```
/teacher/profile.json
```

### Update Profile ###
POST Request parameters
- action = saveUser
```
/teacher/settings.json
```

### Manage Courses ###
```
/teacher/courses.json
```

### Create Assignment ###
POST Request parameter
- title
- description
- deadline (YYYY-MM-DD)
- action: assignment
- attachment: send attachment in this key
```
/assignments/create/{$course_id}/{$classroom_id}.json
```

### Manage Assignment ###
- $course_id (optional) argument. If not given then all assignments will be displayed
```
/assignments/manage/{$course_id}.json
```

### Assignment Submissions ###
This API will be the submissions for the assignment
- $assignment_id (required): id of the assignment
```
/assignments/submissions/{$assignment_id}.json
```

### Grade Assignment ###
Send a POST Request
- grade: On a scale of 1 to 5 with 5 being best
- remarks: Remarks about the submission of student (optional)
- action: 'saveMarks'

Params:
- $submission_id
```
/assignments/gradeIt/{$submission_id}.json
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
/teacher/manageAttendance.json
```

### Weekly Performance ###
This API will allow the teacher to give weekly score to students based on their performance

Send a GET request to see the response
- $course_id: (required)
- $classroom_id: (required)
These values can be fetched from the /courses API

POST Request parameters
- user_id[]: Array
- grade[]: Array [Accepted values: 1-10] 10 being best scale
- action: 'grade'

On successfully submission 'message' key will be set
```
/teacher/weeklyStudentsPerf/{$course_id}/{$classroom_id}.json
```

### Conversation Create ###
Send a GET request to see the response
- Will be given a array of objects containing info about students

POST Request parameters
- display: Name that will be displayed on conversation
- identifier: Username of the student
- user_id: User_id of the student
- action: 'newConv'
```
/conversation/create.json
```

On successfully submission you will be redirected to this url
```
/conversation/view/{$conversation_id}.json
```

### Viewing Active Conversations ###
Send a GET Request to see the response
- conversations: This key will contain different conversation

```
/conversation/all.json
```

### Viewing Specific conversation ###
Here different keys will be set
- users: Users in the conversation
- conversation: Info about conversation
- messages: Messages in the conversation
```
/conversation/view/{$conversation_id}.json
```

### Message in the conversation ###
Send a POST request with
- action: sendMessage
- message: Message user want to send in the conversation

```
/conversation/message/{$conversation_id}.json
```
