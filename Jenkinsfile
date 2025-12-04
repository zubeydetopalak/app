pipeline {
    agent any
    stages {
        stage('Build & Test') {
            steps {
                // Maven Wrapper ile test
                sh './mvnw clean test'
            }
        }

        stage('Deploy') {
            steps {
                // Maven Wrapper ile test
                echo 'Deploy ediliyor...'
           }
       }

    }
    post {
        always {
            // Test sonuçlarını archive et
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
