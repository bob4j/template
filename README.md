# template

* Main technologies used:
 * eclipse IDE
 * apache tomcat 8
 * spring (core, jpa, security, web)
 * jpa (via hibernate and spring)
 * jsf (apache myfaces)

## how to run
###### this section is in progress, so maybe a few steps are missing or are not accurate...
* install postgreSQL (schema: openu; username: openu; password: openu)
 * yum install postgresql-server
 * postgresql-setup initdb
 * systemctl start postgresql
 * su - postgres
 * createuser -P openu (when prompted for password type "openu")
 * createdb --owner=openu openu
 * systemctl stop postgresql
 * edit /var/lib/pgsql/data/pg_hba.conf:
    * #host    all             all             127.0.0.1/32            ident
    * host    all             all             0.0.0.0/0               md5
 * systemctl start postgresql
* install apache tomcat 8
* install maven
 * cd {project}
 * mvn clean install
* create dir "static" - will contain all static images and uploads
 * linux - /home/{user}/static
 * windows - c:/users/{user}/static
* copy {project_dir}/src/main/webapp/static/* into /home/{user}/static/ (c:/users/{user}/static on windows)
* edit {tomcat_home}/conf/server.xml 
 * ```<Host appBase="webapps"> <Context docBase="/home/{user}/static" path="/template/static" /> </Host>``` (linux)
 * ```<Host appBase="webapps"> <Context docBase="c:/users/{user}/static" path="/template/static" /> </Host>``` (windows)
* run application
 * option 1
  * add the installed tomcat as a server in eclipse
  * deploy "template" to tomcat and run it
 * option 2
  * copy {project_dir}/target/template.war into {tomcat_home}/webapps/
  * {tomcat_home}/bin/startup.sh
* browse to http://localhost:8080/template


* import to eclipse the formatter properties - {project}/extra/eclipse.prefs