# TaskFlow

TaskFlow is a modern Android productivity application designed to help users organize their tasks, projects, and daily activities in one place.

Users can create tasks, organize them into projects and categories, set priorities and due dates, receive deadline reminders, track productivity, and manage their profile. The app also uses local storage and Firebase services to provide a reliable and connected experience.

## ✨ Features

### 📝 Task Management
- Create, edit, delete, and complete tasks
- Add task descriptions
- Set Low, Medium, or High priority
- Assign tasks to projects and categories
- Set due dates
- Search and filter tasks

### 📁 Project Management
- Create, edit, and delete projects
- Organize tasks by project
- View project-specific tasks
- Track project progress

### 🏷️ Categories
- Create custom categories
- Assign categories to tasks
- Delete categories when no longer needed

### 📅 Calendar
- View tasks by date
- Navigate between months
- Select a date to view related tasks
- Display task project and category information

### 📊 Statistics
- Track completed and pending tasks
- View productivity progress
- Weekly productivity overview
- Category-based task statistics

### 🔔 Task Reminders
- Receive notifications for upcoming due dates
- Reminder scheduling using WorkManager
- Cancel reminders when tasks are completed or deleted
- Automatically reschedule reminders when task dates change

### 👤 Profile
- Firebase Authentication
- View and edit profile information
- Upload profile picture
- Store profile image using Firebase Storage

### 🌙 Theme & Settings
- Light and Dark mode
- Dark mode preference saved using DataStore
- Notification settings
- Privacy Policy
- About TaskFlow

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Android development |
| Jetpack Compose | UI development |
| Material 3 | Modern UI components |
| MVVM | Presentation architecture |
| Clean Architecture | Project structure |
| Hilt | Dependency Injection |
| Room | Local database |
| Firebase Authentication | User authentication |
| Firebase Firestore | Cloud data |
| Firebase Storage | Profile image storage |
| Coroutines & Flow | Asynchronous operations |
| DataStore | Preference storage |
| WorkManager | Background task reminders |
| Navigation Compose | Screen navigation |
| Coil | Image loading |

## 🏗️ Architecture

TaskFlow follows a layered architecture to keep the application maintainable and scalable.
Presentation
     ↓
ViewModel
     ↓
Use Cases
     ↓
Repository
     ↓
Room / Firebase


# 📱 Main Screens
- Splash
- Login
- Register
- Forgot Password
- Home
- Tasks
- Projects
- Calendar
- Statistics
- Profile

# ☁️ Firebase Integration
TaskFlow uses Firebase for:
- User authentication
- Cloud Firestore data
- Profile image storage
- Cloud synchronization

# 🔔 Notification Flow
Create / Update Task
        ↓
Task Reminder Scheduler
        ↓
WorkManager
        ↓
Android Notification

# Future Improvements
- Improved offline/cloud synchronization
- Better conflict handling
- More notification customization
- Task details from notification clicks

# 👨‍💻 Author
Adarsh Patel
Android Developer | Kotlin | Jetpack Compose

