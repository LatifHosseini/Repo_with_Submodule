pipeline {
    agent { label 'Agent ' }

    stages {
        stage('checkout') {
            steps {
                echo 'Hello World'
                cleanWs()
                // checkout scm $class: 'SurroundSCM'
                checkout scm: [
                     $class: 'GitSCM',
                     branches: scm.branches,
                     extensions: [
                        [$class: 'SubmoduleOption',
                        disableSubmodules: false,
                        parentCredentials: false,
                        recursiveSubmodules: true,
                        reference: 'https://github.com/softwareschneiderei/ADS.git',
                        shallow: true,
                        trackingSubmodules: false]
                    ],
                    submoduleCfg: [],
                    userRemoteConfigs: scm.userRemoteConfigs
                ]
            }
        }
        
        stage('Build') {
            steps {
                echo 'Hello World'
                 cmakeBuild buildDir: 'build', installation: 'InSearchPath', sourceDir: '.'
                 dir('build') { 
                 sh 'make'
                  }    
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
