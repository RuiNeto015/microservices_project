#!/usr/bin/env groovy

import groovy.transform.Field
import java.text.SimpleDateFormat

@Field
def branch

@Field
def directory

@Field
def image

def load_exec_properties(String payload) {
    def payloadObj = readJSON text: payload
    branch = payloadObj.branch
    directory = payloadObj.service
    image = directory.replaceAll("service", "msvc")
}

pipeline {
    agent any

    triggers {
        GenericTrigger(
                genericVariables: [
                        [key: 'PAYLOAD', value: '$', expressionType: 'JSONPath']
                ]
        )
    }

    environment  {
        BUILD = "${env.BUILD_ID}_${new SimpleDateFormat("yyyy-MM-dd_HH'h'mm").format(new Date())}"
    }

    stages {
        stage('Checkout') {
            steps {
                load_exec_properties(env.PAYLOAD)
                echo "Checking out branch $branch"
                checkout scmGit(
                        branches: [[name: "$branch"]],
                        extensions: [],
                        userRemoteConfigs: [
                                [credentialsId: "arqsoft_ssh_key",
                                 url          : 'git@bitbucket.org:arqsoft-23-24-m1a-g09/arqsoft-23-24-m1a-g09.git']
                        ])
            }
        }
        stage('Build') {
            steps {
                dir("${env.WORKSPACE}/$directory"){
                    bat "mvn install"
                    bat "mvn compile"
                }
            }
        }
        stage('Test') {
            steps {
                dir("${env.WORKSPACE}/$directory"){
                    bat "mvn test"
                }
            }
        }
        stage('Build jar') {
            steps {
                dir("${env.WORKSPACE}/$directory"){
                    bat "mvn package"
                }
            }
        }
        stage('Build docker image') {
            steps {
                dir("${env.WORKSPACE}/$directory"){
                    bat "docker build -t $image:$env.BUILD ."
                    bat "minikube image load $image:$env.BUILD"
                }
            }
        }
        stage('Deploy') {
            steps {
                bat "kubectl set image deployment/d-$image $image=$image:$env.BUILD"
            }
        }
    }
}