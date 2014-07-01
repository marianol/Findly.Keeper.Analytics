Findly.Keeper.Analytics
=======================

Custom library that allows a single Mutli tenancy instance of Jasperserver to authenticate against multiple SSO-SAML 2.0 Identify Providers.

The library comprised of source provided and maintained by [http://onelogin.com](onelogin.com) and [http://jaspersoft.com](http://jaspersoft.com)

The onelogin.com code base is maintained within a public repository: [https://github.com/onelogin/java-saml](https://github.com/onelogin/java-saml "Java SAML Toolkit")


Jaspersoft maintains a private repository for commercial users

## Configure Local Maven Repository ##

As Jaspersoft does not provide an public Maven repository, you will need to configure a local repository with the Japsersoft dependency libraries.


    mvn install:install-file -Dfile=external-libs/jasperserver-api-common-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-common -Dversion=5.6.0 -Dpackaging=jar
    mvn install:install-file -Dfile=external-libs/jasperserver-api-externalAuth-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-externalAuth-impl -Dversion=5.6.0 -Dpackaging=jar
    mvn install:install-file -Dfile=external-libs/jasperserver-api-metadata-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata -Dversion=5.6.0 -Dpackaging=jar
    mvn install:install-file -Dfile=external-libs/jasperserver-api-metadata-impl-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=jasperserver-api-metadata-impl -Dversion=5.6.0 -Dpackaging=jar
    mvn install:install-file -Dfile=external-libs/ji-multi-tenancy-5.6.0.jar -DgroupId=com.jasperserver -DartifactId=ji-multi-tenancy -Dversion=5.6.0 -Dpackaging=jar

## Packaging ##

    mvn clean package

    


