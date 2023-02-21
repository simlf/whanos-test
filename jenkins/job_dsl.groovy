folder("Whanos base images") {
}

folder("Projects") {
}

language = ["befunge", "c", "java", "javascript", "python"]

// Create a job for each language
language.each { language ->
	freeStyleJob("Whanos base images/whanos-$language") {
		steps {
			// Specify the Dockerfile to use
			shell("docker build -t whanos-$language /images/$language -f /images/$language/Dockerfile.base")
		}
	}
}

// Create a job to build all base images
freeStyleJob("Whanos base images/Build all base images") {
	publishers {
		downstream(
			language.collect { language -> "Whanos base images/whanos-$language" }
		)
	}
}

// Create a job to link a project to the CI
// Polls the git repository every minute
// Run shell script to detect the language
freeStyleJob("link-project") {
	parameters {
		stringParam("GIT_URL", null, null)
		stringParam("FOLDER_PATH", null, null)
	}
	steps {
		dsl {
			text('''
				freeStyleJob("Projects/$FOLDER_PATH") {
					scm {
						git {
							remote {
								url("$GIT_URL")
							}
						}
					}
					triggers {
						scm("* * * * *")
					}
					steps {
						shell("/scripts/detect_language.sh \\"$FOLDER_PATH\\"")
					}
				}
			''')
		}
	}
}
