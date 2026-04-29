# Student Management System

A full-stack Student Management System built with Spring Boot and deployed using a complete DevOps pipeline. The project covers version control, CI/CD, containerization, orchestration, and automation using Git, Jenkins, Maven, Docker, Kubernetes, and Ansible.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Features](#features)
4. [Folder Structure](#folder-structure)
5. [Prerequisites](#prerequisites)
6. [Run Locally with Docker](#run-locally-with-docker)
7. [API Endpoints](#api-endpoints)
8. [Jenkins Pipeline](#jenkins-pipeline)
9. [Kubernetes Deployment](#kubernetes-deployment)
10. [Ansible Automation](#ansible-automation)
11. [Screenshots](#screenshots)

---

## Project Overview

This project implements a Student Management System as a REST API with a vanilla HTML/CSS/JS frontend. The main focus is the DevOps pipeline: code is built with Maven, packaged into a Docker image, pushed to DockerHub via Jenkins, and deployed on Kubernetes using Ansible.

---

## Tech Stack

| Layer | Tool |
|---|---|
| Backend | Spring Boot 3, Spring Data JPA |
| Database | H2 (in-memory) |
| Build | Maven |
| Frontend | Vanilla HTML, CSS, JavaScript |
| Version Control | Git |
| CI/CD | Jenkins |
| Containerization | Docker |
| Orchestration | Kubernetes (Minikube) |
| Automation | Ansible |

---

## Features

- Add, view, edit, and delete students
- Search students by name or ID
- Filter by class, section, and status
- Upload and display student profile photo
- Store contact and parent/guardian details
- Mark attendance per student per date (present or absent)
- View attendance summary with percentage
- Add subject-wise marks with exam type
- Auto-calculate grade (A+, A, B, C, D, F)
- Activate or deactivate student status
- Paginated student list
- 4-page frontend with sidebar navigation

---

## Folder Structure

```
student-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/sms/
│   │   │   ├── controller/        REST controllers
│   │   │   ├── service/           Business logic
│   │   │   ├── repository/        JPA repositories
│   │   │   ├── model/             JPA entities and enums
│   │   │   ├── dto/               Data transfer objects
│   │   │   └── StudentManagementApplication.java
│   │   └── resources/
│   │       ├── static/            Frontend HTML pages
│   │       └── application.properties
│   └── test/java/com/sms/
│       └── StudentServiceTest.java
├── k8s/
│   ├── namespace.yaml
│   ├── deployment.yaml
│   └── service.yaml
├── ansible/
│   ├── playbook.yml
│   └── inventory
├── Dockerfile
├── Jenkinsfile
├── deploy.sh
├── pom.xml
└── README.md
```

---

## Prerequisites

| Tool | Version |
|---|---|
| Java | 17+ |
| Maven | 3.9+ (optional if using Docker) |
| Docker Desktop | 24+ |
| Jenkins | 2.400+ |
| Minikube | 1.30+ |
| kubectl | 1.28+ |
| Ansible | 2.14+ |

---

## Run Locally with Docker

No Maven installation needed. Docker handles the build inside the container.

```bash
git clone https://github.com/meowmeow99/student-management-system.git
cd student-management-system

docker build -t meowmeow99/student-management-system:latest .
docker run -p 8080:8080 meowmeow99/student-management-system:latest
```

Open the app: http://localhost:8080

The H2 database console is available at: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:smsdb`
- Username: `sa`
- Password: (leave empty)

---

## API Endpoints

### Students

| Method | Endpoint | Description |
|---|---|---|
| POST | `/students` | Add new student |
| GET | `/students` | Get all students (supports `?search=&className=&section=&status=&page=&size=`) |
| GET | `/students/{id}` | Get student by ID |
| PUT | `/students/{id}` | Update student |
| DELETE | `/students/{id}` | Delete student |
| PATCH | `/students/{id}/status?status=ACTIVE` | Toggle active/inactive |
| POST | `/students/{id}/photo` | Upload profile photo (multipart) |

### Attendance

| Method | Endpoint | Description |
|---|---|---|
| POST | `/attendance` | Mark attendance |
| GET | `/attendance/{studentId}` | Get records (supports `?from=&to=`) |
| GET | `/attendance/{studentId}/summary` | Get attendance summary |

### Marks

| Method | Endpoint | Description |
|---|---|---|
| POST | `/marks` | Add marks |
| GET | `/marks/{studentId}` | Get marks (supports `?examType=`) |
| GET | `/marks/{studentId}/summary` | Get marks summary with grades |
| DELETE | `/marks/{id}` | Delete a mark entry |

### Grade Scale

| Percentage | Grade |
|---|---|
| 90 and above | A+ |
| 80 to 89 | A |
| 70 to 79 | B |
| 60 to 69 | C |
| 50 to 59 | D |
| Below 50 | F |

---

## Jenkins Pipeline

### Setup Steps

1. Make sure Docker and Ansible are installed on the Jenkins agent machine.

2. Add DockerHub credentials in Jenkins:
   - Go to Manage Jenkins > Credentials > System > Global
   - Kind: Username with password
   - ID: `dockerhub-creds`
   - Enter your DockerHub username and password

3. Create a new Pipeline job:
   - New Item > Pipeline
   - Under Pipeline, select "Pipeline script from SCM"
   - SCM: Git
   - Enter your GitHub repository URL
   - Script Path: `Jenkinsfile`

4. Click Build Now.

### Pipeline Stages

| Stage | What it does |
|---|---|
| Checkout | Pulls latest code from Git |
| Build | Runs `mvn clean package -DskipTests` |
| Test | Runs `mvn test` and publishes JUnit results |
| Docker Build | Builds image tagged as `latest` and `$BUILD_NUMBER` |
| Docker Push | Pushes both tags to DockerHub |
| Ansible Deploy | Runs `ansible/playbook.yml` to deploy on Kubernetes |

---

## Kubernetes Deployment

### Start Minikube

```bash
minikube start
```

### Deploy using the script

```bash
chmod +x deploy.sh
./deploy.sh
```

Or apply manifests manually:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

### Verify deployment

```bash
kubectl get pods -n student-ns
kubectl get svc -n student-ns
```

### Access the app

```bash
minikube ip
```

Open: `http://<minikube-ip>:30080`

### Kubernetes Configuration Summary

| Setting | Value |
|---|---|
| Namespace | student-ns |
| Replicas | 2 |
| Memory limit | 256Mi |
| CPU limit | 500m |
| Service type | NodePort |
| NodePort | 30080 |
| Liveness probe | GET /students every 10s |

---

## Ansible Automation

The Ansible playbook applies all Kubernetes manifests in order and prints pod and service status.

```bash
cd student-management-system
ansible-playbook ansible/playbook.yml -i ansible/inventory
```

### Playbook tasks in order

1. Apply namespace.yaml
2. Apply deployment.yaml
3. Apply service.yaml
4. Wait 30 seconds for pods to start
5. Print pod status (`kubectl get pods -n student-ns`)
6. Print service status (`kubectl get svc -n student-ns`)

---

## Screenshots

Add screenshots here after running the project:

- [ ] Jenkins pipeline stages passing
- [ ] Docker image build output
- [ ] DockerHub showing pushed image
- [ ] `kubectl get pods -n student-ns` output
- [ ] Application running in browser (student list page)
- [ ] Application running in browser (profile page)
- [ ] Ansible playbook output

---

## Group Members

| Name | Role |
|---|---|
| Member 1 | Backend (Models, Repositories, Application setup) |
| Member 2 | Services, Controllers, Unit Tests |
| Member 3 | Frontend UI, Docker, Kubernetes, Ansible, Jenkins |

---

## Submission Info

- Topic: Student Management System
- Tools: Git, Jenkins, Maven, Docker, Kubernetes, Ansible
- DockerHub Image: `meowmeow99/student-management-system`
