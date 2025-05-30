pipeline {
    agent any
    environment {
        WORKSPACE = '/workspaces/micro-ecom'
        DOCKER_USER = 'lamph11'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Detect Changed Services') {
            steps {
                script {
                    def changedServices = []

                    def productServiceChanged = sh(script: 'git diff --name-only HEAD~1..HEAD | grep "^product/"', returnStatus: true)
                    if (productServiceChanged == 0) {
                        changedServices.add('product-service')
                    }

                    def webConfigServiceChanged = sh(script: 'git diff --name-only HEAD~1..HEAD | grep "^webconfig/"', returnStatus: true)
                    if (webConfigServiceChanged == 0) {
                        changedServices.add('webconfig-service')
                    }

                    def authServiceChanged = sh(script: 'git diff --name-only HEAD~1..HEAD | grep "^auth/"', returnStatus: true)
                    if (authServiceChanged == 0) {
                        changedServices.add('auth-service')
                    }

                    env.CHANGED_SERVICES = changedServices.join(',')
                }
            }
        }

        stage('Build Images') {
            steps {
                script {
                    def services = env.CHANGED_SERVICES.split(',')
                    withCredentials([usernamePassword(credentialsId: 'DockerHub_Lamph', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"

                        services.each { service ->
                            if (service == 'product-service') {
                                echo "Building image for product-service"
                                sh "docker build -f product/Dockerfile -t ${DOCKER_USER}/product-service:latest ."

                                echo "Pushing product-service image"
                                sh "docker push ${DOCKER_USER}/product-service:latest"
                            }

                            if (service == 'webconfig-service') {
                                echo "Building image for webconfig-service"
                                sh "docker build -f webconfig/Dockerfile -t ${DOCKER_USER}/webconfig-service:latest ."

                                echo "Pushing webconfig-service image"
                                sh "docker push ${DOCKER_USER}/webconfig-service:latest"
                            }

                            if (service == 'auth-service') {
                                echo "Building image for auth-service"
                                sh "docker build -f auth/Dockerfile -t ${DOCKER_USER}/auth-service:latest ."

                                echo "Pushing auth-service image"
                                sh "docker push ${DOCKER_USER}/auth-service:latest"
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy Changed Services') {
            steps {
                script {
                    def services = env.CHANGED_SERVICES.split(',')
                    services.each { service ->
                        if (service == 'product-service') {
                            echo "Stopping current product-service"
                            sh "docker-compose -f ${WORKSPACE}/docker-compose.yml down product-service"

                            echo "Starting new product-service image"
                            sh "docker-compose -f ${WORKSPACE}/docker-compose.yml up -d --pull always product-service"
                        }

                        if (service == 'webconfig-service') {
                            echo "Stopping current webconfig-service"
                            sh "docker-compose -f ${WORKSPACE}/docker-compose.yml down webconfig-service"

                            echo "Starting new webconfig-service image"
                            sh "docker-compose -f ${WORKSPACE}/docker-compose.yml up -d --pull always webconfig-service"
                        }

                        if (service == 'auth-service') {
                            echo "Stopping current auth-service"
                            sh "docker-compose -f ${WORKSPACE}/docker-compose.yml down auth-service"

                            echo "Starting new auth-service image"
                            sh "docker-compose -f ${WORKSPACE}/docker-compose.yml up -d --pull always auth-service"
                        }

                    }
                }
            }
        }

        stage('Integration Test') {
            steps {
                script {
                    echo "Running integration tests"
                    // Add your integration test commands here
                }
            }
        }

        stage('Clean') {
            steps {
                script {
                    echo "Cleaning up unused Docker images"
                    sh 'docker image prune -a -f'
                }
            }
        }
    }
}
