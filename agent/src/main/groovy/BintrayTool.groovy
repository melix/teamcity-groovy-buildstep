def buildDir = new File(system.get('teamcity.build.checkoutDir'))

// load properties
def props = new Properties()
props.load(new File('/var/teamcity/bintray.properties').newInputStream())
props.load(new File(buildDir, 'gradle.properties'))
def user = props.getProperty('bintrayUser')
def key = props.getProperty('bintrayPassword')

def version=props.getProperty('groovyVersion')

log.message "Releasing Groovy $version with user: $user"

def desc = "Test release of $version"
def date = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
def url = "https://api.bintray.com/packages/groovy/maven/groovy/versions"
def contents = """{
"name": "$version",
"released": "$date",
"desc": "$desc",
"vcs_tag": "GROOVY_${version.replace('.','_')}"
}"""
def file = File.createTempFile('data','json')
file << contents
def p = ["curl", "-u", "$user:$key", '-H', 'Content-Type: application/json', "--request","POST" ,"--data", "@${file.absolutePath}", url].execute()
p.waitFor()
log.message "${p.text}"
file.delete()

def distDir = new File(new File(buildDir, 'target'), '/distributions')
distDir.listFiles { f->
    log.message "Uploading $f"
    url = "https://api.bintray.com/content/groovy/maven/groovy/$version/${f.name};publish=1"
    p = ["curl", "-u", "$user:$key", "--request","PUT" ,"--data-binary", "@${f.absolutePath}", url].execute()
    p.waitFor()
    log.message "${p.text}"
}