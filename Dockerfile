FROM dryseawind/wildfly14jdk8

MAINTAINER sami.volotinen@gmail.com

ADD ./deployments /opt/jboss/wildfly-14.0.1.Final/standalone/deployments

CMD ["/opt/jboss/wildfly-14.0.1.Final/bin/standalone.sh", "-c", "standalone.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

