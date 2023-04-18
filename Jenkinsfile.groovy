pipeline {
    agent { label 'Agent ' }
   // tools {
        //maven 'mvn-default'
        //  CMake
   // }

    stages {
        stage('checkout') {
            steps {
                echo 'Hello World'
                cleanWs()
                 checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Hello World'
                 cmakeBuild buildDir: 'build', installation: 'InSearchPath', sourceDir: '.'
                 dir('build') { 
                 sh 'make'
                  }    
               // sh 'chmod +x $(pwd)/ Bash_script.sh'
               // sh '$(pwd)/Bash_script.sh'  
                // sh '$(pwd)/Script_Yocto_SDV_EVAL.sh'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Hello World'
            }
        }
    }
    
    post { 
        always { 
            echo 'I am in recordIssues'
            recordIssues(tools: [gcc()])
            
            
            //Directory/**/*.* -> All the files recursively under Directory
            //archiveArtifacts artifacts: 'build/**/*.*'
            
            //**/*.* -> all the files in the workspace
            archiveArtifacts artifacts:'**/*.* '
            
            emailext body: 'HI THIS A TEST MAIL ', subject: 'TEST', to: 'latifho2007@gmail.com' 
        }
    }
}
