pipeline {
    agent {
        label 'java-node'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '📦 Kodni yuklab olayapti...'
                git 'https://github.com/urinovshokirjon/learning.git'
            }
        }

        stage('Build') {
            steps {
                echo '🔧 Build boshlangan...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Testlar ishlamoqda...'
                // sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploy qilinmoqda...'
                // Deploy jarayoni: bu joyga kerakli deploy script yoziladi
                // Masalan: sh './deploy.sh' yoki war faylni nusxalash
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline muvaffaqiyatli yakunlandi!'
        }
        failure {
            echo '❌ Pipeline bajarilmadi!'
        }
    }
}
