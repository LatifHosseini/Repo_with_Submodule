pipeline {
    agent { label 'Agent ' }
    stages {
        stage('checkout') {
            steps {
                echo 'Hello World'
                cleanWs()                
                checkout scm: [
                     $class: 'GitSCM',
                     branches: scm.branches,
                     extensions: [
                        [$class: 'SubmoduleOption',
                        disableSubmodules: false,
                        parentCredentials: false,
                        recursiveSubmodules: true,
                        reference: 'https://github.com/LatifHosseini/Repo_with_Submodule.git',
                        shallow: true,
                        trackingSubmodules: true]
                    ],
                    submoduleCfg: [],
                    userRemoteConfigs: scm.userRemoteConfigs
                ]
            }
        }        
        stage('Build') {
            steps {
                echo 'Hello Build step'
                dir('Repo_as_Submodule') {
                    cmakeBuild buildDir: 'build', installation: 'InSearchPath', sourceDir: '.'
                    dir('build') { 
                        sh 'make'
                  }    
                }                                                 
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
        }
    }
}
