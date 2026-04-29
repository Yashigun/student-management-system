pipeline {
    agent any

    environment {
        DOCKERHUB_IMAGE = 'meowmeow99/student-management-system'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building application with Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKERHUB_IMAGE}:latest -t ${DOCKERHUB_IMAGE}:${BUILD_NUMBER} ."
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing Docker image to DockerHub...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh "docker push ${DOCKERHUB_IMAGE}:latest"
                    sh "docker push ${DOCKERHUB_IMAGE}:${BUILD_NUMBER}"
                }
            }
        }

        stage('Ansible Deploy') {
            steps {
                echo 'Deploying to Kubernetes via Ansible...'
                sh 'ansible-playbook ansible/playbook.yml -i ansible/inventory'
            }
        }

    }

    post {
        always {
            echo "Pipeline finished. Build status: ${currentBuild.currentResult}"
        }
        failure {
            echo 'BUILD FAILED'
        }
    }
}
