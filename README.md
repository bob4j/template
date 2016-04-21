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
 * copy target/{project}.war into {tomcat_home}/webapps/
* create dir /home/{user}/static - will contain all static images and uploads
* copy {project_dir}/src/main/webapp/static/* into /home/{user}/static/
* edit {tomcat_home}/conf/server.xml 
 * ```<Host appBase="webapps"> <Context docBase="/home/{user}/static" path="/{project}/static" /> </Host>```
* run tomcat
 * {tomcat_home}/bin/startup.sh
* browse to http://localhost:8080/template


