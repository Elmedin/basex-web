BaseX Web
=========
This is a sneak-peak at an XQuery-driven Web Application Server.

Requirements
------------
[Java 1.6](http://java.com/getjava/index.jsp "Download Free Java Software") and [Maven](http://maven.apache.org/ "Maven - 
    Welcome to Apache Maven") are highly recommended to run the project. 

News
----
For the next few days you will need the latest BaseX 6.6.2-Snapshot from our [github](https://github.com/BaseXdb/basex),
it is not yet available via maven. `git clone git://github.com/BaseXdb/basex.git`, once cloned, `cd basex` and then `mvn install`, now you are set.

Usage
-----
To get the demo application up and running 
either clone this project (or even better fork it):
<code>
$ git clone git@github.com:micheee/basex-web.git
</code> 
Once cloned, change to the checked out project and run
<code>
$ mvn jetty:run
</code>
this will fetch all needed packages and start a webserver at [localhost:8080](http://localhost:8080 "Inline XQuery in your Browser").

The default page will contain some help information to get you started. 

The source files that power your webapplication reside in `src/main/webapp`. 

BaseX is able to parse both, `*.htm(l)` files that contain *inline xquery* like 

* `<?xq 1 to 10 ?>` (see [index.html](http://localhost:8080/index.html) for an example)

as well as *.xq(y) files in pure functional mode.

* see [index.xq](http://localhost:8080/index.xq) for an example
