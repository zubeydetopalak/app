pipeline {
    agent any

    environment {
        // Keep Maven memory reasonable for CI
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Checkout') {
            steps {
                // Use SCM configured in the Jenkins job
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Use the project's Maven wrapper when available, otherwise fall back to system mvn
                sh '''
if [ -x "./mvnw" ] && [ -f "./.mvn/wrapper/maven-wrapper.properties" ]; then
  ./mvnw -B -V -e clean verify
else
  echo "Maven wrapper not found or incomplete; falling back to system mvn"
  mvn -B -V -e clean verify
fi
'''
            }
        }

        stage('Package (skip tests)') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                sh '''
if [ -x "./mvnw" ] && [ -f "./.mvn/wrapper/maven-wrapper.properties" ]; then
  ./mvnw -B -DskipTests package
else
  mvn -B -DskipTests package
fi
'''
            }
        }

        stage('Archive artifacts') {
            steps {
                // Archive JAR/WAR and site/jacoco artifacts if present
                archiveArtifacts artifacts: 'target/*.jar, target/*.war', allowEmptyArchive: true, fingerprint: true
                archiveArtifacts artifacts: 'target/site/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/jacoco.exec', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            // Publish JUnit test results to Jenkins (surefire/failsafe)
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'

            // Keep workspace clean between runs (optional; remove if you need workspace retained)
            cleanWs()
        }

        success {
            echo "Build succeeded"
        }

        failure {
            echo "Build failed"
        }
    }
}
