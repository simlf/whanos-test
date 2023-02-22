import jenkins.model.*
import hudson.tasks.*
import hudson.triggers.*
import com.cloudbees.hudson.plugins.folder.Folder

def jenkins = Jenkins.getInstance()

// Create 'Whanos base images' folder
def whanosFolder = jenkins.createProject(Folder.class, "Whanos base images")

// Create 'Projects' folder
def projectFolder = jenkins.createProject(Folder.class, "Projects")

def whanosC = whanosFolder.createProject(hudson.model.FreeStyleProject, "whanos-c")
def whanosJava = whanosFolder.createProject(hudson.model.FreeStyleProject, "whanos-java")
def whanosBefunge = whanosFolder.createProject(hudson.model.FreeStyleProject, "whanos-befunge")
def whanosJavascript = whanosFolder.createProject(hudson.model.FreeStyleProject, "whanos-javascript")
def whanosPython = whanosFolder.createProject(hudson.model.FreeStyleProject, "whanos-python")

whanosC.getBuildersList().add(new hudson.tasks.Shell("docker build -t whanos-c /home/Ansible/images/c -f /home/Ansible/images/c/Dockerfile.base"))
whanosJava.getBuildersList().add(new hudson.tasks.Shell("docker build -t whanos-java /home/Ansible/images/java -f /home/Ansible/images/java/Dockerfile.base"))
whanosJavascript.getBuildersList().add(new hudson.tasks.Shell("docker build -t whanos-javascript /home/Ansible/images/javascript -f /home/Ansible/images/javascript/Dockerfile.base"))
whanosBefunge.getBuildersList().add(new hudson.tasks.Shell("docker build -t whanos-befunge /home/Ansible/images/befunge -f /home/Ansible/images/befunge/Dockerfile.base"))
whanosPython.getBuildersList().add(new hudson.tasks.Shell("docker build -t whanos-python /home/Ansible/images/python -f /home/Ansible/images/python/Dockerfile.base"))

job = new FreeStyleProject(Hudson.instance, 'link-project')
job.addProperty(new ParametersDefinitionProperty(
  new StringParameterDefinition('GIT_URL', null, null),
  new StringParameterDefinition('FOLDER_PATH', null, null)
))
job.getBuildersList().add(new Shell(
'''
    /scripts/detect_language.sh "$FOLDER_PATH"
'''))
job.addTrigger(new SCMTrigger('* * * * *'))
job.setScm(new GitSCM('$GIT_URL'))

job.save()
