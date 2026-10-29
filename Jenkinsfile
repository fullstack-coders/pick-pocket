pipeline {
    agent any

    environment {
        // Generate a unique, immutable tag using Build Number and short Git SHA
        IMAGE_TAG = "${env.BUILD_NUMBER}-${env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : 'local'}"
        COMPOSE_PROJECT_NAME = 'local_deployment'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

//        stage('Lint & Unit Tests') {
//            steps {
//                // Execute your testing suite before building containers
//                echo "Running linter and unit tests..."
//                // Example: sh 'npm test' or 'pytest'
//            }
//        }

        stage('Build Containers via Compose') {
            steps {
                script {
                    // Build images defined in docker-compose using the dynamic tag
                    sh "TAG=${IMAGE_TAG} docker compose build --no-cache"
                }
            }
        }

 /*       stage('Security Scan') {
            steps {
                script {
                    // Optional: Scan the built image for vulnerabilities using Trivy or similar
                    echo "Scanning container image for vulnerabilities..."
                    // Example: sh "trivy image my-app:${IMAGE_TAG}"
                }
            }
        }*/

        stage('Deploy to Localhost') {
            steps {
                script {
                    // Gracefully update the stack without dropping external networks
                    sh """
                        TAG=${IMAGE_TAG} docker compose down
                        TAG=${IMAGE_TAG} docker compose up -d --remove-orphans
                    """
                }
            }
        }

        stage('Smoke Test & Health Check') {
            steps {
                script {
                    // Verify the service is actually responding after startup
                    echo "Performing health check on container..."
                    sleep time: 5, unit: 'SECONDS'
                    sh 'curl -f http://localhost:8081/health || exit 1'
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline passed! Successfully deployed stack version ${IMAGE_TAG}."
        }
        failure {
            echo "Pipeline failed. Tearing down failed stack and rolling back..."
            script {
                // Optional automated rollback or cleanup logic on failure
                sh 'docker compose down || true'
            }
        }
        always {
            // Clean up dangling build artifacts if needed to save disk space
            sh 'docker image prune -f --filter "until=24h" || true'
        }
    }
}